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

public class ExtraActivity extends Activity {

    private Button matrix;
    private Button fresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        matrix = (Button) findViewById(R.id.button);
        matrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, MatrixActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        fresh = (Button) findViewById(R.id.button2);
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, PullToRefreshActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
    }
}
