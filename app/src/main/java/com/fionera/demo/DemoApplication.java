package com.fionera.demo;

import android.app.ActivityManager;
import android.os.Process;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.fionera.base.BaseApplication;
import com.fionera.demo.util.CrashHandler;
import com.fionera.demo.util.WeexHttpAdapter;
import com.fionera.demo.util.WeexImageLoaderAdapter;
import com.fionera.demo.weex.CustomViewComponent;
import com.fionera.demo.weex.URLHelperModule;
import com.fionera.multipic.common.LocalImageHelper;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import org.xutils.x;

import java.util.List;

public class DemoApplication
        extends BaseApplication {

    private static WindowManager.LayoutParams wmLayoutParams;
    private static LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();

        if(!isMainProcess()){
            return;
        }

        instance = this;
        wmLayoutParams = new WindowManager.LayoutParams();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        x.Ext.init(this);
        x.Ext.setDebug(false);

        try {
            WXEnvironment.addCustomOptions("appName", getString(R.string.app_name));
            WXSDKEngine.registerComponent("custom-view-component", CustomViewComponent.class);
            WXSDKEngine.registerModule("URLHelper", URLHelperModule.class);
            WXSDKEngine.initialize(this, new InitConfig.Builder().setHttpAdapter(
                    new WeexHttpAdapter()).setImgAdapter(new WeexImageLoaderAdapter()).build());
        } catch (WXException e) {
            e.printStackTrace();
        }

        getDisplayMetrics();

        LocalImageHelper.init(instance);

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

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    public boolean isMainProcess() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processes) {
            if (TextUtils.equals(process.processName, getPackageName())) {
                return Process.myPid() == process.pid;
            }
        }
        return false;
    }

    public static WindowManager.LayoutParams getWmLayoutParams() {
        return wmLayoutParams;
    }

    public static LocalBroadcastManager getLocalBroadcastManager() {
        return localBroadcastManager;
    }

    private void getDisplayMetrics() {
        DisplayMetrics metric = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        screenDensity = metric.density;
        scaledDensity = metric.scaledDensity;
    }
}
