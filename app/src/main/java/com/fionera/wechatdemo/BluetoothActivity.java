package com.fionera.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.share.ShareActivity;


public class BluetoothActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);

        /**
         * 创建一个用于解析二维码的跳转按钮
         */
        Button decode = new Button(this);
        decode.setGravity(Gravity.CENTER);
        decode.setText("二维码扫描");
        decode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothActivity.this,
                        CaptureActivity.class);
                BluetoothActivity.this.startActivity(intent);
                BluetoothActivity.this.finish();

            }
        });
        ll.addView(decode);

        /**
         * 创建一个用于生成二维码的跳转按钮
         */
        Button encode = new Button(this);
        encode.setText("二维码生成");
        encode.setGravity(Gravity.CENTER);
        encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.setClassName(BluetoothActivity.this,
                        ShareActivity.class.getName());
                startActivity(intent);
            }
        });
        ll.addView(encode);

        setContentView(ll);

    }

}


