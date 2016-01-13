package com.fionera.demo.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.fionera.demo.MainActivity;
import com.fionera.demo.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Picture Download").setContentText(
                "Download in progress").setSmallIcon(R.mipmap.ic_launcher);
        new Thread(() -> {
            for (int i = 0; i < 100; i += 20) {
                builder.setProgress(100, i, false);
                mNotificationManager.notify(0x123456, builder.build());
                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException ignored) {
                }
            }
            builder.setContentText("Download complete").setProgress(0, 0, false);
            mNotificationManager.notify(0x123456, builder.build());
        }).start();
    }
}
