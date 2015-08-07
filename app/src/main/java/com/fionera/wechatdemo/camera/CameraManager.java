package com.fionera.wechatdemo.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;


import com.fionera.wechatdemo.camera.open.OpenCameraInterface;

import java.io.IOException;

public class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();

    private final Context context;
    private final CameraConfigurationManager configManager;
    private Camera camera;
    private AutoFocusManager autoFocusManager;

    private boolean initialized;
    private boolean previewing;
    private int requestedCameraId = -1;

    /**
     * 预览回调
     */
    private final PreviewCallback previewCallback;

    public CameraManager(Context context) {
        this.context = context;
        this.configManager = new CameraConfigurationManager(context);
        previewCallback = new PreviewCallback(configManager);
    }

    /**
     * 相机是否开启
     * @return
     */
    public synchronized boolean isOpen() {
        return camera != null;
    }

    /**
     * 开启相机
     * @param holder
     * @throws IOException
     */
    public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        Camera theCamera = camera;
        if (theCamera == null) {

            if (requestedCameraId >= 0) {
                theCamera = OpenCameraInterface.open(requestedCameraId);
            } else {
                theCamera = OpenCameraInterface.open();
            }

            if (theCamera == null) {
                throw new IOException();
            }
            camera = theCamera;
        }
        theCamera.setPreviewDisplay(holder);

        if (!initialized) {
            initialized = true;
            configManager.initFromCameraParameters(theCamera);
        }

        Camera.Parameters parameters = theCamera.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten();

        try {
            configManager.setDesiredCameraParameters(theCamera, false);
        } catch (RuntimeException re) {
            // Driver failed
            Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
            Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
            // Reset:
            if (parametersFlattened != null) {
                parameters = theCamera.getParameters();
                parameters.unflatten(parametersFlattened);
                try {
                    theCamera.setParameters(parameters);
                    configManager.setDesiredCameraParameters(theCamera, true);
                } catch (RuntimeException re2) {
                    // Well, darn. Give up
                    Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
                }
            }
        }

    }

    /**
     * 关闭相机
     */
    public synchronized void closeDriver() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    /**
     * 开启预览
     */
    public synchronized void startPreview() {
        Camera theCamera = camera;
        if (theCamera != null && !previewing) {
            theCamera.startPreview();
            previewing = true;
            autoFocusManager = new AutoFocusManager(context, camera);
        }
    }

    /**
     * 停止预览
     */
    public synchronized void stopPreview() {
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }
        if (camera != null && previewing) {
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    /**
     * 获取当前预览帧，用于二维码扫描结果
     *
     * @param handler 送往的Handler
     * @param message 包含结果的Message
     */
    public synchronized void requestPreviewFrame(Handler handler, int message) {
        Camera theCamera = camera;
        if (theCamera != null && previewing) {
            previewCallback.setHandler(handler, message);
            theCamera.setOneShotPreviewCallback(previewCallback);
        }
    }


    /**
     * 获取相机分辨率
     *
     * @return
     */
    public Point getCameraResolution() {
        return configManager.getCameraResolution();
    }

    /**
     * 获取相机预览尺寸
     * @return
     */
    public Size getPreviewSize() {
        if (null != camera) {
            return camera.getParameters().getPreviewSize();
        }
        return null;
    }
}
