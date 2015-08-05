package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fionera.wechatdemo.MainActivity;
import com.fionera.wechatdemo.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.result.URIResultHandler;
import com.google.zxing.client.result.ResultParser;

import java.util.Collection;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private ViewfinderView viewfinderView;
    private TextView statusView;
    private ImageView closeScanner;
    private Result lastResult;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private BeepManager beepManager;


    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // 获取窗口并保持常亮
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);

        hasSurface = false;
        beepManager = new BeepManager(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        statusView = (TextView) findViewById(R.id.status_view);
        closeScanner = (ImageView) findViewById(R.id.iv_close_scanner);
        closeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        handler = null;
        lastResult = null;

        resetStatusView();

        //SurfaceView和SurfaceHolder
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // Activity 处于暂停但未关闭状态，尽管surfaceview的onSurfaceCreate方法未调用，因此适合初始化相机
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        beepManager.updatePrefs(); //设置震动

        decodeFormats = null;
        characterSet = null;
    }


    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (lastResult != null) {
                //restartPreviewAfterDelay(0L);
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult The contents of the barcode.
     * @param barcode   A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode) {
        lastResult = rawResult;

        URIResultHandler resultHandler = new URIResultHandler(this, ResultParser.parseResult(rawResult));


        if (barcode == null) {
            // This is from history -- no saved barcode
            handleDecodeInternally(rawResult, resultHandler, null);
        } else {
            beepManager.playBeepSoundAndVibrate();
            handleDecodeInternally(rawResult, resultHandler, barcode);
        }
    }


    /**
     * Put up our own UI for how to handle the decoded contents. 处理扫描结果
     *
     * @param rawResult     原始结果
     * @param resultHandler 扫描结果载体
     * @param barcode       条码截图
     */
    private void handleDecodeInternally(Result rawResult, URIResultHandler resultHandler, Bitmap barcode) {
        statusView.setVisibility(View.GONE);
        viewfinderView.setVisibility(View.GONE);
        closeScanner.setVisibility(View.GONE);
        findViewById(R.id.tv_status_view).setVisibility(View.GONE);

        //dialog展示简要信息
        showResult(rawResult, resultHandler, barcode);

        /*处理结果*/

        /*//二维码格式  例：QR_CODE
        String format = rawResult.getBarcodeFormat().toString();

        //二维码扫描结果类型 例： URL
        String type = resultHandler.getType().toString();

        //time
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        String formattedTime = formatter.format(new Date(rawResult.getTimestamp()));

        //url
        CharSequence displayContents = resultHandler.getDisplayContents();

        Log.d(TAG, String.format("formatText=%s typeText=%s timeText=%s displayContents=%s",
                format, type, formattedTime, displayContents));*/

    }

    private void showResult(Result rawResult, URIResultHandler resultHandler, Bitmap barcode) {
        Log.d(TAG, rawResult.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence displayContents = resultHandler.getDisplayContents();
        if (barcode == null) {
            builder.setIcon(R.mipmap.barcode);
        } else {

            Drawable drawable = new BitmapDrawable(barcode);
            builder.setIcon(drawable);
        }

        builder.setTitle("扫描结果");
        builder.setMessage(displayContents);
        builder.setPositiveButton("OK", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CaptureActivity.this, MainActivity.class);
                startActivity(intent);
                CaptureActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CaptureActivity.this.finish();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (Exception e) {
            e.printStackTrace();
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 相机不能使用时 弹出提示
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    private void resetStatusView() {
        statusView.setText(R.string.msg_default_status);
        statusView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
        closeScanner.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }
}
