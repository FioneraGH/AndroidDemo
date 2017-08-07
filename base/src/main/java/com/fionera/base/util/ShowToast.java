package com.fionera.base.util;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.fionera.base.BaseApplication;

/**
 * ShowToast
 * Created by fionera on 15-12-6.
 */
public class ShowToast {
    private static Toast toast;

    @SuppressLint("ShowToast")
    public static void show(Object info) {
        if (info == null) {
            return;
        }

        if (info instanceof Throwable) {
            if (info instanceof CommonRuntimeException) {
                info = ((Exception) info).getMessage();
            } else {
                ((Throwable) info).printStackTrace();
                info = "未知网络错误";
            }
        }

        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getInstance(), info.toString(),
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(info.toString());
        }
        toast.show();
    }
}
