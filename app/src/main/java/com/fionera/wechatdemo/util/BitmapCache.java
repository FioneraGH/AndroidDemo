package com.fionera.wechatdemo.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by fionera on 15-8-7.
 */
public class BitmapCache implements ImageLoader.ImageCache {

    LruCache<String, Bitmap> cache;
    int max = 1024 * 1024 * 8;

    public BitmapCache() {

        cache = new LruCache<String, Bitmap>(max);
    }

    @Override
    public Bitmap getBitmap(String s) {

        return cache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {

        cache.put(s, bitmap);
    }
}
