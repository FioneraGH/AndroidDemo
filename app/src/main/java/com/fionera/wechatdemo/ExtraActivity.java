package com.fionera.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fionera.wechatdemo.extra.MatrixActivity;
import com.fionera.wechatdemo.extra.PullToRefreshActivity;
import com.fionera.wechatdemo.extra.SplitPageActivity;

public class ExtraActivity extends Activity {

    private Button matrix;
    private Button fresh;
    private Button split;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        // 设定Matrix跳转
        matrix = (Button) findViewById(R.id.button1);
        matrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, MatrixActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定下拉刷新跳转
        fresh = (Button) findViewById(R.id.button2);
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, PullToRefreshActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定分页加载跳转
        split = (Button) findViewById(R.id.button3);
        split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, SplitPageActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
    }
}
