package com.fionera.demo;

import android.app.Application;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.fionera.demo.util.CrashHandler;
import com.fionera.demo.util.WeexImageLoaderAdapter;
import com.fionera.demo.weex.CustomViewComponent;
import com.fionera.demo.weex.URLHelperModule;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import org.xutils.x;

public class DemoApplication
        extends Application {

    private static WindowManager.LayoutParams wmLayoutParams;
    private static DemoApplication instance;
    private static LocalBroadcastManager localBroadcastManager;

    public static int screenWidth;
    public static int screenHeight;
    public static float screenDensity;
    public static float scaledDensity;

    @Override
    public void onCreate() {

        super.onCreate();
        wmLayoutParams = new WindowManager.LayoutParams();
        instance = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        x.Ext.init(this);
        x.Ext.setDebug(false);

        try {
            WXEnvironment.addCustomOptions("appName", getString(R.string.app_name));
            WXSDKEngine.registerComponent("custom-view-component", CustomViewComponent.class);
            WXSDKEngine.registerModule("URLHelper", URLHelperModule.class);
            WXSDKEngine.initialize(this,
                    new InitConfig.Builder().setImgAdapter(new WeexImageLoaderAdapter()).build());
        } catch (WXException e) {
            e.printStackTrace();
        }

        getDisplayMetrics();

        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog()
                            .penaltyDeathOnNetwork().build());
        } else {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
    }

    public static DemoApplication getInstance() {
        return instance;
    }

    public static LocalBroadcastManager getLocalBroadcastManager() {
        return localBroadcastManager;
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
