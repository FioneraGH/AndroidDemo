package com.fionera.wechatdemo.util;

import android.widget.Toast;

import com.fionera.wechatdemo.DemoApplication;

/**
 * Created by fionera on 15-12-6.
 */
public class ShowToast {

    private static Toast toast;

    public static void show(String notice) {

        if (toast == null) {
            toast = Toast.makeText(DemoApplication.getInstance(), notice, Toast.LENGTH_SHORT);
        } else {
            toast.setText(notice);
        }
        toast.show();
    }
}
