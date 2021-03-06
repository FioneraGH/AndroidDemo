package com.fionera.demo.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.LogCat;
import com.fionera.demo.MainActivity;
import com.fionera.demo.R;
import com.fionera.demo.service.GrabAccessibilityService;
import com.fionera.demo.service.ListenClipboardService;

import androidx.core.app.NotificationCompat;

/**
 * @author fionera
 */
public final class ClipBoardActivity
        extends BaseActivity {
    public static final String CLIPBOARD_CONTENT = "clipboard_content";

    private Button btnClipBoardTest;
    private Button btnGrabRedStart;

    public static void startForContent(Context context, String content) {
        Intent intent = new Intent(context, ClipBoardActivity.class);
        intent.putExtra(CLIPBOARD_CONTENT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_board);

        initView();

        btnClipBoardTest.setOnClickListener(v -> ListenClipboardService
                .startForTest(mContext, "test" + System.currentTimeMillis()));
        btnGrabRedStart.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new NotificationCompat.Builder(mContext, "DEFAULT_CHANNEL")
                    .setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(
                            "[微信红包]").setContentText("点击查看红包").setContentIntent(pendingIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(14726467, notification);
            }
        });

        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            btnClipBoardTest.setText(uri.getQueryParameter("uid"));
        }
        tryToShowContent(intent);
        ListenClipboardService.start(mContext);
        initView();

        if (!isAccessibilitySettingsOn(getApplicationContext())) {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + GrabAccessibilityService.class
                .getCanonicalName();
        LogCat.d("Check Accessibility Service Which Name:" + service);
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
                ':');

        if (1 == accessibilityEnabled) {
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            LogCat.d("Check Accessibility Service Running Name:" + settingValue);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tryToShowContent(intent);
    }

    private void tryToShowContent(Intent intent) {
        String content = intent.getStringExtra(CLIPBOARD_CONTENT);
        if (!TextUtils.isEmpty(content)) {
            btnClipBoardTest.setText(content);
        }
    }

    private void initView() {
        btnClipBoardTest = findViewById(R.id.btn_clip_board_test);
        btnGrabRedStart = findViewById(R.id.btn_grab_red_start);
    }
}