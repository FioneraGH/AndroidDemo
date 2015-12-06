package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.fionera.wechatdemo.R;


public class XUtils3Activity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xutils);

        imageView = (ImageView) findViewById(R.id.iv_volley);
    }
}
