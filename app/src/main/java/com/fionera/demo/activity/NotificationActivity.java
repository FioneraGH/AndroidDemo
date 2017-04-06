package com.fionera.demo.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.fionera.demo.R;

import java.util.regex.Pattern;

public class NotificationActivity
        extends AppCompatActivity {
    public static final int NOTIFICATION_ID = 142035738;

    private NotificationManager notificationManager;

    private void sendNotification(final NotificationManager notificationManager) {
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Picture Download").setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher);
        new Thread(() -> {
            for (int i = 0; i < 100; i += 20) {
                builder.setProgress(100, i, false);
                notificationManager.notify(0x123456, builder.build());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
            builder.setContentText("Download complete").setProgress(0, 0, false);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        findViewById(R.id.btn_notify_send).setOnClickListener(
                v -> sendNotification(notificationManager));

        TextView tvNotifyHtml = (TextView) findViewById(R.id.tv_notify_html);
        TextView tvNotifySpannable = (TextView) findViewById(R.id.tv_notify_spannable);
        TextView tvNotifyLinkify = (TextView) findViewById(R.id.tv_notify_linkify);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        tvNotifyHtml.setText((Html.fromHtml("<a href='https://www.badiu.com'>百度</a>")));
        tvNotifyHtml.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString text = new SpannableString("百度");
        text.setSpan(new NoUnderlineSpan("https://www.baidu.com"), 0, text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNotifySpannable.setText(text);
        tvNotifySpannable.setMovementMethod(LinkMovementMethod.getInstance());

        Pattern p = Pattern.compile("@(\\w+?)(?=\\W|$)(.)");
        Linkify.addLinks(tvNotifyLinkify, Linkify.WEB_URLS);
        Linkify.addLinks(tvNotifyLinkify, p, "mxn://profile:8856?uid=user",
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
//            ShowToast.show(((TextView) widget).getText().toString());
            super.onClick(widget);
        }
    }
}
