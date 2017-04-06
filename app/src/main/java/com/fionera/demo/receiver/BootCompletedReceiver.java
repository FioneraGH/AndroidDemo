package com.fionera.demo.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;

import com.fionera.demo.service.ListenClipboardService;

public class BootCompletedReceiver
        extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            ListenClipboardService.startForWeakLock(context, intent);
        }
    }
}
