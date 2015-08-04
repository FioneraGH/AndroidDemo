package com.fionera.wechatdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HandlerActivity extends Activity {

    private TextView tvShow;
    private Button btnStart1;
    private Button btnStart2;
    private ImageView ivShow;
    private ProgressDialog dialog;
    private DatePicker datePicker;

    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        tvShow = (TextView) findViewById(R.id.tvShow);
        btnStart1 = (Button) findViewById(R.id.btnStart);
        btnStart2 = (Button) findViewById(R.id.btnStart2);
        ivShow = (ImageView) findViewById(R.id.ivShow);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        ViewGroup group = (ViewGroup) datePicker.getChildAt(0);
        group = (ViewGroup) group.getChildAt(1);
        group.setVisibility(View.GONE);

        dialog = new ProgressDialog(HandlerActivity.this);
        dialog.setTitle("Tips");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);

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
                        }, 3000);
                    }
                }).start();
            }
        });

        btnStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new LoadImageRunnable()).start();
                dialog.show();
            }
        });
    }

    public class LoadImageRunnable implements Runnable {

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://ww4.sinaimg.cn/bmiddle/786013a5jw1e7akotp4bcj20c80i3aao.jpg");
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpGet);
                if (200 == httpResponse.getStatusLine().getStatusCode()) {
                    byte[] data = EntityUtils.toByteArray(httpResponse.getEntity());

                    final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ivShow.setImageBitmap(bitmap);
                        }
                    });
                    dialog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
