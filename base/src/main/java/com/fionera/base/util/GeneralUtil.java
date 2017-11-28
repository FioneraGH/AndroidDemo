package com.fionera.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.fionera.base.BaseApplication;

/**
 * @author fionera
 */
public class GeneralUtil {
    private static final String ARMEABI = "armeabi";
    private static final String ARMEABI_V_7_A = "armeabi-v7a";

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) BaseApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean checkArchSupported() {
        /*
        check if supported by device
         */
        boolean isSupported = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (String supportedAbi : Build.SUPPORTED_ABIS) {
                if (TextUtils.equals(ARMEABI, supportedAbi) || TextUtils.equals(ARMEABI_V_7_A,
                        supportedAbi)) {
                    isSupported = true;
                    break;
                }
            }
        } else {
            if (TextUtils.equals(ARMEABI, Build.CPU_ABI) || TextUtils.equals(ARMEABI_V_7_A,
                    Build.CPU_ABI)) {
                isSupported = true;
            }
        }
        return isSupported;
    }

    /**
     * 获取当前应用的版本名称
      */
    public static String getVersionName() {
        try {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    BaseApplication.getInstance().getPackageName(), 0);
            String version = packInfo.versionName;
            if (!TextUtils.isEmpty(version)) {
                return version;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    /**
     * 获取当前应用的版本序号
      */
    public static int getVersionCode() {
        try {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    BaseApplication.getInstance().getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
