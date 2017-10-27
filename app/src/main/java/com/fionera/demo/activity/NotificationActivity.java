package com.fionera.demo.activity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.LogCat;
import com.fionera.demo.R;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author fionera
 */
public class NotificationActivity
        extends BaseActivity {
    public static final int NOTIFICATION_ID = 142035738;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("@(\\w+?)(?=\\W|$)(.)");

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            r -> new Thread(r, "update-notify-%d"));

    private NotificationManager notificationManager;

    private void sendNotification(final NotificationManager notificationManager) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "DEFAULT_CHANNEL");
        builder.setContentTitle("Picture Download").setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher).setChannelId("testChannel");
        threadPoolExecutor.execute(() -> {
            int maxValue = 100;
            int step = 20;
            for (int i = 0; i < maxValue; i += step) {
                builder.setProgress(100, i, false);
                notificationManager.notify(0x123456, builder.build());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
            builder.setContentText("Download complete").setProgress(0, 0, false);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        });
    }

    private void sendOpenOneNotification(final NotificationManager notificationManager) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "DEFAULT_CHANNEL");
        builder.setAutoCancel(true).setContentTitle("Open One").setContentText(
                "Click Open One Activity").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(
                PendingIntent.getActivity(mContext, 0,
                        new Intent(mContext, ConstraintLayoutActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        PendingIntent.FLAG_UPDATE_CURRENT)).setChannelId("testChannel");
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void sendOpenMulNotification(final NotificationManager notificationManager) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "DEFAULT_CHANNEL");
        builder.setAutoCancel(true).setContentTitle("Open One").setContentText(
                "Click Open Mul Activity").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(
                PendingIntent.getActivities(mContext, 0, new Intent[]{new Intent(mContext,
                                ConstraintLayoutActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK), new Intent(mContext,
                                GameActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), new
                                Intent(
                                mContext, OpenGlActivity.class).addFlags(Intent
                                .FLAG_ACTIVITY_NEW_TASK)},
                        PendingIntent.FLAG_UPDATE_CURRENT)).setChannelId("testChannel");
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void sendTaskBuilderNotification(final NotificationManager notificationManager) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "DEFAULT_CHANNEL");
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(mContext);
        taskStackBuilder.addNextIntentWithParentStack(new Intent(mContext, OpenGlActivity.class));
        builder.setAutoCancel(true).setContentTitle("Open One").setContentText(
                "Click Open Task Activity").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(
                taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT))
                .setChannelId("testChannel");
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        findViewById(R.id.btn_notify_send).setOnClickListener(
                v -> sendNotification(notificationManager));
        findViewById(R.id.btn_notify_open_one).setOnClickListener(
                v -> sendOpenOneNotification(notificationManager));
        findViewById(R.id.btn_notify_open_mul).setOnClickListener(
                v -> sendOpenMulNotification(notificationManager));
        findViewById(R.id.btn_notify_task_builder).setOnClickListener(
                v -> sendTaskBuilderNotification(notificationManager));

        TextView tvNotifyHtml = findViewById(R.id.tv_notify_html);
        TextView tvNotifySpannable = findViewById(R.id.tv_notify_spannable);
        TextView tvNotifyLinkify = findViewById(R.id.tv_notify_linkify);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("testChannel", "TestChannel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Description");
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        notificationManager.createNotificationChannel(channel);

        tvNotifyHtml.setText((Html.fromHtml("<a href='https://www.badiu.com'>百度</a>",
                Html.FROM_HTML_MODE_COMPACT)));
        tvNotifyHtml.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString text = new SpannableString("百度");
        text.setSpan(new NoUnderlineSpan("https://www.baidu.com"), 0, text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNotifySpannable.setText(text);
        tvNotifySpannable.setMovementMethod(LinkMovementMethod.getInstance());

        Linkify.addLinks(tvNotifyLinkify, Linkify.WEB_URLS);
        Linkify.addLinks(tvNotifyLinkify, NUMBER_PATTERN, "mxn://profile:8856?uid=user",
                (charSequence, start, end) -> charSequence.charAt(end - 1) != '.',
                (match, url) -> url.toLowerCase());
        stripUnderlines(tvNotifyLinkify);
    }

    private void stripUnderlines(TextView textView) {
        Spannable s = (Spannable) textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new NoUnderlineSpan(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    @SuppressLint("ParcelCreator")
    private class NoUnderlineSpan
            extends URLSpan {

        NoUnderlineSpan(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            LogCat.d(((TextView) widget).getText().toString());
            super.onClick(widget);
        }
    }
}
