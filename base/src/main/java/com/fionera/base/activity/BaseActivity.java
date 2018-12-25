package com.fionera.base.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * BaseActivity
 *
 * @author fionera
 * @date 17-1-9 in AndroidDemo
 */

public class BaseActivity
        extends AppCompatActivity {

    protected Context mContext;
    protected boolean isDestroy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        isDestroy = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
