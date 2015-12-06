package com.fionera.wechatdemo.application;

import android.app.Application;
import android.view.WindowManager;


public class DemoApplication extends Application {

    private static WindowManager.LayoutParams wmLayoutParams;

    @Override
    public void onCreate() {

        super.onCreate();
        wmLayoutParams = new WindowManager.LayoutParams();
    }


    public static WindowManager.LayoutParams getWmLayoutParams() {
        return wmLayoutParams;
    }
}
