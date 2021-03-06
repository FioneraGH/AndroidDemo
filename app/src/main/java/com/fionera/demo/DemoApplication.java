package com.fionera.demo;

import android.app.ActivityManager;
import android.os.Process;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.fionera.base.BaseApplication;
import com.fionera.demo.util.CrashHandler;

import java.util.List;

/**
 * @author fionera
 */
public class DemoApplication
        extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (!isMainProcess()) {
            return;
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

    public boolean isMainProcess() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> processes = activityManager
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo process : processes) {
                if (TextUtils.equals(process.processName, getPackageName())) {
                    return Process.myPid() == process.pid;
                }
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
