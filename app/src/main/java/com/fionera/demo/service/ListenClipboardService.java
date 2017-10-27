package com.fionera.demo.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;

import com.fionera.demo.activity.ClipBoardActivity;
import com.fionera.demo.receiver.BootCompletedReceiver;
import com.fionera.demo.view.floatview.TipViewController;

/**
 * @author fionera
 */
public final class ListenClipboardService
        extends Service
        implements TipViewController.ViewDismissHandler {

    public static final String CLIPBOARD_WAKELOCK = "clipboard_wakelock";

    private static final String KEY_FOR_CMD = "cmd";
    private static final String CMD_TEST = "test";

    private static CharSequence sLastContent = null;
    private ClipboardManager mClipboardWatcher;
    private TipViewController mTipViewController;
    private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener =
            this::performClipboardCheck;

    public static void start(Context context) {
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.startService(serviceIntent);
    }

    /**
     * for dev
     */
    public static void startForTest(Context context, String content) {
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        serviceIntent.putExtra(KEY_FOR_CMD, CMD_TEST);
        serviceIntent.putExtra(ClipBoardActivity.CLIPBOARD_CONTENT, content);
        context.startService(serviceIntent);
    }

    public static void startForWeakLock(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.startService(serviceIntent);

        intent.putExtra(CLIPBOARD_WAKELOCK, true);
        Intent wakeIntent = new Intent(context, ListenClipboardService.class);

        // using wake lock to start service
        WakefulBroadcastReceiver.startWakefulService(context, wakeIntent);
    }

    @Override
    public void onCreate() {
        mClipboardWatcher = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (mClipboardWatcher != null) {
            mClipboardWatcher.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClipboardWatcher.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener);

        sLastContent = null;
        if (mTipViewController != null) {
            mTipViewController.setViewDismissHandler(null);
            mTipViewController = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getBooleanExtra(CLIPBOARD_WAKELOCK, false)) {
                BootCompletedReceiver.completeWakefulIntent(intent);
            }
            String cmd = intent.getStringExtra(KEY_FOR_CMD);
            if (!TextUtils.isEmpty(cmd)) {
                if (cmd.equals(CMD_TEST)) {
                    String content = intent.getStringExtra(ClipBoardActivity.CLIPBOARD_CONTENT);
                    showContent(content);
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void performClipboardCheck() {
        CharSequence content = mClipboardWatcher.getPrimaryClip().getItemAt(0).coerceToText(
                ListenClipboardService.this);
        if (TextUtils.isEmpty(content)) {
            return;
        }
        showContent(content);
    }

    private void showContent(CharSequence content) {
        boolean noNeedShow = sLastContent != null && sLastContent.equals(
                content) || content == null;
        if (noNeedShow) {
            return;
        }
        sLastContent = content;

        if (mTipViewController != null) {
            mTipViewController.updateContent(content);
        } else {
            mTipViewController = new TipViewController(getApplication(), content);
            mTipViewController.setViewDismissHandler(this);
            mTipViewController.show();
        }
    }

    @Override
    public void onViewDismiss() {
        sLastContent = null;
        mTipViewController = null;
    }
}