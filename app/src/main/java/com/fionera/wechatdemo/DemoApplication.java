package com.fionera.wechatdemo;

import android.app.Application;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.xutils.x;


public class DemoApplication extends Application {

    private static WindowManager.LayoutParams wmLayoutParams;
    private static Application instance;

    public static int screenWidth;
    public static int screenHeight;
    public static float screenDensity;
    public static float scaledDensity;

    @Override
    public void onCreate() {

        super.onCreate();
        wmLayoutParams = new WindowManager.LayoutParams();
        instance = this;

        x.Ext.init(this);
        x.Ext.setDebug(false);

        getDisplayMetrics();

        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog()
                            .penaltyDeathOnNetwork().build());
        }
    }

    public static Application getInstance() {
        return instance;
    }

    public static WindowManager.LayoutParams getWmLayoutParams() {
        return wmLayoutParams;
    }

    private void getDisplayMetrics() {

        DisplayMetrics metric = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        screenDensity = metric.density;
        scaledDensity = metric.scaledDensity;
    }
}
