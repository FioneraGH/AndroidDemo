package com.fionera.demo.view.floatview;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.fionera.demo.MainActivity;
import com.fionera.demo.R;
import com.fionera.demo.activity.ClipBoardActivity;

public final class TipViewController
        implements View.OnClickListener, View.OnTouchListener, ViewContainer.KeyEventHandler {

    private WindowManager mWindowManager;
    private Context mContext;
    private ViewContainer mWholeView;
    private View mContentView;
    private ViewDismissHandler mViewDismissHandler;
    private CharSequence mContent;
    private TextView mTextView;

    public TipViewController(Context application, CharSequence content) {
        mContext = application;
        mContent = content;
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setViewDismissHandler(ViewDismissHandler viewDismissHandler) {
        mViewDismissHandler = viewDismissHandler;
    }

    public void updateContent(CharSequence content) {
        mContent = content;
        mTextView.setText(mContent);
    }

    public void show() {

        ViewContainer view = (ViewContainer) View.inflate(mContext, R.layout.layout_pop_view, null);

        mTextView = (TextView) view.findViewById(R.id.tv_pop_view_text);
        mTextView.setText(mContent);

        mWholeView = view;
        mContentView = view.findViewById(R.id.ll_pop_view_content_view);

        mContentView.setOnClickListener(this);
        mWholeView.setOnTouchListener(this);
        mWholeView.setKeyEventHandler(this);

        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.MATCH_PARENT;

        int flags = 0;
        int type;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;

        mWindowManager.addView(mWholeView, layoutParams);
    }

    @Override
    public void onClick(View v) {
        removePoppedViewAndClear();
        if (TextUtils.equals(mContent, "Don't forget the marshmallows!")) {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        } else {
            ClipBoardActivity.startForContent(mContext, mContent.toString());
        }
    }

    /**
     * touch the outside of the content view, remove the popped view
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Rect rect = new Rect();
        mContentView.getGlobalVisibleRect(rect);
        if (!rect.contains(x, y)) {
            removePoppedViewAndClear();
        }
        return false;
    }

    @Override
    public void onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            removePoppedViewAndClear();
        }
    }

    private void removePoppedViewAndClear() {

        if (mWindowManager != null && mWholeView != null) {
            mWindowManager.removeView(mWholeView);
        }

        if (mViewDismissHandler != null) {
            mViewDismissHandler.onViewDismiss();
        }

        mContentView.setOnClickListener(null);
        mWholeView.setOnTouchListener(null);
        mWholeView.setKeyEventHandler(null);
    }

    public interface ViewDismissHandler {
        void onViewDismiss();
    }
}
