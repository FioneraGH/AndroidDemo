package com.fionera.wechatdemo;

import android.app.Application;
import android.view.WindowManager;

import org.xutils.x;


public class DemoApplication extends Application {

    private static WindowManager.LayoutParams wmLayoutParams;
    private static Application instance;

    @Override
    public void onCreate() {

        super.onCreate();
        wmLayoutParams = new WindowManager.LayoutParams();
        instance = this;

        x.Ext.init(this);
        x.Ext.setDebug(false);
    }

    public static Application getInstance() {
        return instance;
    }

    public static WindowManager.LayoutParams getWmLayoutParams() {
        return wmLayoutParams;
    }
}
