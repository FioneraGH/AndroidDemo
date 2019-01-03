package com.fionera.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.fionera.base.AppContextHolder;

/**
 * @author fionera
 */
public class GeneralUtil {
    private static final String ARMEABI = "armeabi";
    private static final String ARMEABI_V_7_A = "armeabi-v7a";

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) AppContextHolder.getAppContext()
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
}
