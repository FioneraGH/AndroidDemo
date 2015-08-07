package com.fionera.wechatdemo.application;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by fionera on 15-8-7.
 */
public class DemoApplication extends Application {

    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("Volley",requestQueue.toString());
    }

    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
