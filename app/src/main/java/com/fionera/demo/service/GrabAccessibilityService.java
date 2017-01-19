package com.fionera.demo.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.fionera.demo.activity.ClipBoardActivity;
import com.fionera.demo.util.LogCat;
import com.fionera.demo.util.ShowToast;

import java.util.List;

/**
 * GrabAccessibilityService
 * Created by fionera on 17-1-19 in AndroidDemo.
 */

public class GrabAccessibilityService
        extends AccessibilityService {
    private static final String WE_CHAT_NOTIFICATION_TIP = "[微信红包]";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            List<CharSequence> texts = event.getText();
            if (texts != null) {
                for (CharSequence text : texts) {
                    if (text.toString().contains(WE_CHAT_NOTIFICATION_TIP)) {
                        openNotification(event);
                        break;
                    }
                }
            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            startGrabRedPack(event);
        }
    }

    private void openNotification(AccessibilityEvent event) {
        if (event.getParcelableData() == null || !(event
                .getParcelableData() instanceof Notification)) {
            return;
        }
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        LogCat.d("Accessibility Service Deal With Notification");
    }

    private void startGrabRedPack(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNodeInfo = event.getSource();
        if (rootNodeInfo == null) {
            return;
        }
        LogCat.d("Deal With Node Info");
    }

    @Override
    public void onInterrupt() {

    }
}
