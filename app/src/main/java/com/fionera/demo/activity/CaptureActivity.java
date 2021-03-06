package com.fionera.demo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.LogCat;
import com.fionera.base.util.ShowToast;
import com.google.zxing.Result;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * @author fionera
 */
public class CaptureActivity
        extends BaseActivity
        implements ZXingScannerView.ResultHandler {

    private final static int ASK_FOR_CAMERA = 1000;

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    ASK_FOR_CAMERA);
        } else {
            ShowToast.show("请对准二维码");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        LogCat.d(rawResult.getText());
        LogCat.d(rawResult.getBarcodeFormat().toString());

        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ASK_FOR_CAMERA == requestCode && Manifest.permission.CAMERA.equals(permissions[0])) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ShowToast.show("授权成功");
            } else {
                ShowToast.show("该功能需要相机权限支持");
                finish();
            }
        }
    }
}
