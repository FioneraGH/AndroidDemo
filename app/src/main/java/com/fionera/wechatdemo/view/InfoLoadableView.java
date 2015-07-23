package com.fionera.wechatdemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.fionera.wechatdemo.R;

/**
 * Created by fionera on 15-7-22.
 */
public class InfoLoadableView extends LinearLayout implements View.OnTouchListener {

    private View footer;
    private Button btnLoad;
    private ProgressBar progressBar;

    public InfoLoadableView(Context context) {
        super(context);

        footer = LayoutInflater.from(context).inflate(R.layout.more_info_foot, null, true);
        btnLoad = (Button) footer.findViewById(R.id.bt_load);
        progressBar = (ProgressBar) footer.findViewById(R.id.pg);
        setOrientation(VERTICAL);
        addView(footer, 1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
