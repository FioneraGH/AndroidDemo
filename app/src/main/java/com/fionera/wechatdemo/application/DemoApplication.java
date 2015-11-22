package com.fionera.wechatdemo.application;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by fionera on 15-8-7.
 */
public class DemoApplication extends Application {

    private static RequestQueue requestQueue;
    private static WindowManager.LayoutParams wmLayoutParams;

    @Override
    public void onCreate() {
        super.onCreate();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        wmLayoutParams = new WindowManager.LayoutParams();
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static WindowManager.LayoutParams getWmLayoutParams() {
        return wmLayoutParams;
    }
}
