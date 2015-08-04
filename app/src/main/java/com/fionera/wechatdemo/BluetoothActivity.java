package com.fionera.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.share.ShareActivity;


public class BluetoothActivity extends Activity {


    private ImageView iv_scanner;
    private ImageView iv_generator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        iv_scanner = (ImageView) findViewById(R.id.iv_scanner);
        iv_generator = (ImageView) findViewById(R.id.iv_generator);

        iv_scanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothActivity.this,
                        CaptureActivity.class);
                BluetoothActivity.this.startActivity(intent);
                BluetoothActivity.this.finish();

            }
        });
        iv_generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.setClassName(BluetoothActivity.this,
                        ShareActivity.class.getName());
                startActivity(intent);
            }
        });
    }
}

