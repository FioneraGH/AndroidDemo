package com.fionera.demo.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.fionera.demo.util.ShowToast;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationActivity
        extends AppCompatActivity {

    @ViewInject(R.id.tv_notify_html)
    private TextView tvNotifyHtml;
    @ViewInject(R.id.tv_notify_spannable)
    private TextView tvNotifySpannable;
    @ViewInject(R.id.tv_notify_linkify)
    private TextView tvNotifyLinkify;
    private NotificationManager notificationManager;

    @Event(R.id.btn_notify_send)
    private void onClick(View v) {
        sendNotification(notificationManager);
    }

    private void sendNotification(NotificationManager notificationManager) {
        Notification.Builder builder = new Notification.Builder(this);
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
            notificationManager.notify(0x123456, builder.build());
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        x.view().inject(this);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //        tvNotifyHtml.setText((Html.fromHtml("<a href='https://www.badiu.com'>百度</a>")));
        //        tvNotifyHtml.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString text = new SpannableString("百度");
        text.setSpan(new NoUnderlineSpan("https://www.baidu.com"), 0, text.length(),
                     Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNotifySpannable.setText(text);
        tvNotifySpannable.setMovementMethod(LinkMovementMethod.getInstance());

        Pattern p = Pattern.compile("@(\\w+?)(?=\\W|$)(.)");
        Linkify.addLinks(tvNotifyLinkify, Linkify.WEB_URLS);
        Linkify.addLinks(tvNotifyLinkify, p, "mxn://profile?uid=",
                         (s, start, end) -> s.charAt(end - 1) != '.',
                         new Linkify.TransformFilter() {
                             @Override
                             public String transformUrl(Matcher match, String url) {
                                 return url.toLowerCase();
                             }
                         });
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

    private class NoUnderlineSpan
            extends URLSpan {

        public NoUnderlineSpan(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            ShowToast.show(((TextView) widget).getText().toString());
            super.onClick(widget);
        }
    }
}
