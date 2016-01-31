package com.fionera.demo.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.fionera.demo.R;
import com.fionera.demo.provider.WidgetProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fionera on 15-11-5.
 */
public class WidgetService extends Service{

    private Timer timer;
    private SimpleDateFormat simpleDateFormat;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss", Locale.CHINA);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String time = simpleDateFormat.format(new Date());

                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_timer_widget);
                remoteViews.setTextViewText(R.id.tv_widget_clock, time);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                ComponentName componentName = new ComponentName(getApplicationContext(),
                        WidgetProvider.class);
                appWidgetManager.updateAppWidget(componentName,remoteViews);
            }
        }, 0, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer = null;
    }
}
