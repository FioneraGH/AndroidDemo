package com.fionera.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.fionera.wechatdemo.extra.MatrixActivity;
import com.fionera.wechatdemo.extra.PullToLoadActivity;
import com.fionera.wechatdemo.extra.PullToRefreshActivity;
import com.fionera.wechatdemo.extra.RecycleActivity;
import com.fionera.wechatdemo.extra.SplitPageActivity;
import com.fionera.wechatdemo.extra.XUtils3Activity;

public class ExtraActivity extends Activity {

    private Button matrix;
    private Button fresh;
    private Button split;
    private Button load;
    private Button recycle;
    private Button volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        // 设定Matrix跳转
        matrix = (Button) findViewById(R.id.button1);
        matrix.setOnClickListener(v -> {
            Intent intent = new Intent(ExtraActivity.this, MatrixActivity.class);
            ExtraActivity.this.startActivity(intent);
            ExtraActivity.this.finish();
        });
        // 设定下拉刷新跳转
        fresh = (Button) findViewById(R.id.button2);
        fresh.setOnClickListener(v -> {
            Intent intent = new Intent(ExtraActivity.this, PullToRefreshActivity.class);
            ExtraActivity.this.startActivity(intent);
            ExtraActivity.this.finish();
        });
        // 设定分页加载跳转
        split = (Button) findViewById(R.id.button3);
        split.setOnClickListener(v -> {
            Intent intent = new Intent(ExtraActivity.this, SplitPageActivity.class);
            ExtraActivity.this.startActivity(intent);
            ExtraActivity.this.finish();
        });
        // 设定下拉加载跳转
        load = (Button) findViewById(R.id.button4);
        load.setOnClickListener(v -> {
            Intent intent = new Intent(ExtraActivity.this, PullToLoadActivity.class);
            ExtraActivity.this.startActivity(intent);
            ExtraActivity.this.finish();
        });
        // 设定RecyclerView测试跳转
        recycle = (Button) findViewById(R.id.button6);
        recycle.setOnClickListener(v -> {
            Intent intent = new Intent(ExtraActivity.this, RecycleActivity.class);
            ExtraActivity.this.startActivity(intent);
            ExtraActivity.this.finish();
        });
        // 设定Volley测试跳转
        volley = (Button) findViewById(R.id.button9);
        volley.setOnClickListener(v -> {
            Intent intent = new Intent(ExtraActivity.this, XUtils3Activity.class);
            ExtraActivity.this.startActivity(intent);
            ExtraActivity.this.finish();
        });

    }
}
