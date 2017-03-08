package com.fionera.multipic.common;

import android.util.Log;

/**
 * LogUtils
 * Created by fionera on 17-2-6 in AndroidDemo.
 */

class LogUtils {
    public static void d(Object o){
        if(o == null){
            return;
        }
        Log.d("MultiPic",o.toString());
    }
}
