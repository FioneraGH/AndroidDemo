package com.fionera.demo;

import android.app.ActivityManager;
import android.os.Process;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.fionera.base.BaseApplication;
import com.fionera.demo.util.CrashHandler;
import com.fionera.demo.util.WeexImageLoaderAdapter;
import com.fionera.demo.weex.CustomViewComponent;
import com.fionera.demo.weex.URLHelperModule;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import java.util.List;

public class DemoApplication
        extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (!isMainProcess()) {
            return;
        }

        instance = this;

        try {
            WXEnvironment.addCustomOptions("appName", getString(R.string.app_name));
            WXSDKEngine.registerComponent("custom-view-component", CustomViewComponent.class);
            WXSDKEngine.registerModule("URLHelper", URLHelperModule.class);
            WXSDKEngine.initialize(this, new InitConfig.Builder().setImgAdapter(new WeexImageLoaderAdapter()).build());
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

    private void getDisplayMetrics() {
        DisplayMetrics metric = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        screenDensity = metric.density;
        scaledDensity = metric.scaledDensity;
    }
}
