package com.fionera.wechatdemo.application;

import android.app.Application;
import android.view.WindowManager;

import org.xutils.x;


public class DemoApplication extends Application {

    private static WindowManager.LayoutParams wmLayoutParams;

    @Override
    public void onCreate() {

        super.onCreate();
        wmLayoutParams = new WindowManager.LayoutParams();

        x.Ext.init(this);
        x.Ext.setDebug(false);
    }


    public static WindowManager.LayoutParams getWmLayoutParams() {
        return wmLayoutParams;
    }
}
