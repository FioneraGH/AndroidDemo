package com.fionera.base;

import android.app.Application;

/**
 * BaseApplication
 * Created by fionera on 17-2-6 in AndroidDemo.
 */

public class BaseApplication
        extends Application {
    protected static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    public static int screenWidth;
    public static int screenHeight;
    public static float screenDensity;
    public static float scaledDensity;
}
