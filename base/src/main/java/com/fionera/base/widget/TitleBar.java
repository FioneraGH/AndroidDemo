package com.fionera.base.widget;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fionera.base.R;

@SuppressWarnings("unused")
public class TitleBar
        extends LinearLayout {

    private TextView mTvTitle;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        Toolbar titleBar = (Toolbar) View.inflate(context, R.layout.layout_title_bar, null);
        addView(titleBar, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        mTvTitle = (TextView) titleBar.findViewById(R.id.title_bar_title);
    }

    public void setTitleBarText(String title) {
        mTvTitle.setText(title);
    }

    public void setTitleBarTextColor(int id) {
        mTvTitle.setTextColor(id);
    }

    public void setTitleBarTitleClick(OnClickListener onTitleClickListener) {
        mTvTitle.setOnClickListener(onTitleClickListener);
    }
}
