package com.fionera.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class HandlerActivity extends Activity {

    private TextView tvShow;
    private Button btnStart;
    private Button btnBroadCast;
    private DatePicker datePicker;

    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        tvShow = (TextView) findViewById(R.id.tvShow);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnBroadCast = (Button) findViewById(R.id.btnBroadCast);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup group = (ViewGroup) datePicker.getChildAt(0);
            group = (ViewGroup) group.getChildAt(1);
            group.setVisibility(View.GONE);
        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvShow.setText("From New Thread");
                            }
                        }, 2000);
                    }
                }).start();
            }
        });

        btnBroadCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.CLICK_TO_CHANGE_FROM_OTHER);
                intent.putExtra("which_click", 3);
                sendBroadcast(intent);
                Toast.makeText(HandlerActivity.this, "已发送", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
