package com.fionera.demo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.ViewConfiguration;

/**
 * NavigationBarUtil
 * Created by fionera on 15-10-5.
 */
public class NavigationBarUtil {

    /**
     * 获取导航栏高度
     */
    @SuppressLint("ObsoleteSdkInt")
    public static int getNavigationBarHeight(Context context) {

        /*
          有实体键
         */
        if (ViewConfiguration.get(context).hasPermanentMenuKey()) {
            return 0;
        }
        /*
          版本低于KITKAT
         */
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return 0;
        }

        /*
          获取系统虚拟键高度
         */
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
