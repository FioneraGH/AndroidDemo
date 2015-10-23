package com.fionera.wechatdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class HandlerActivity extends Activity {

    private TextView tvShow;
    private Button btnStart1;
    private DatePicker datePicker;

    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        tvShow = (TextView) findViewById(R.id.tvShow);
        btnStart1 = (Button) findViewById(R.id.btnStart);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        ViewGroup group = (ViewGroup) datePicker.getChildAt(0);
        group = (ViewGroup) group.getChildAt(1);
        group.setVisibility(View.GONE);


        btnStart1.setOnClickListener(new View.OnClickListener() {
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
    }
}
