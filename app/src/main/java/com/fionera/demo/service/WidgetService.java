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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author fionera
 * @date 15-11-5
 */
public class WidgetService extends Service{

    private ScheduledThreadPoolExecutor timer;
    private SimpleDateFormat simpleDateFormat;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new ScheduledThreadPoolExecutor(1, r -> new Thread(r,"widget-update-%d"));
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss", Locale.CHINA);
        timer.scheduleAtFixedRate(() -> {
            String time = simpleDateFormat.format(new Date());

            RemoteViews remoteViews = new RemoteViews(getPackageName(),
                    R.layout.layout_timer_widget);
            remoteViews.setTextViewText(R.id.tv_widget_clock, time);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(
                    getApplicationContext());
            ComponentName componentName = new ComponentName(getApplicationContext(),
                    WidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        }, 100, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer = null;
    }
}
