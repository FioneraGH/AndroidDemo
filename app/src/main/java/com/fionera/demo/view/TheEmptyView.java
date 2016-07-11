package com.fionera.demo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.fionera.demo.R;
import com.fionera.demo.util.EmptyViewUtil;


public class TheEmptyView
        extends FrameLayout {

    private static EmptyViewUtil.EmptyViewBuilder mConfig = null;

    private float mTextSize;
    private int mTextColor;
    private String mEmptyText;
    private int mIconSrc;
    private OnClickListener mOnClickListener;
    private String actionText;

    private boolean mShowIcon = true;
    private boolean mShowText = true;
    private boolean mShowButton = false;

    private ImageView mImageView;
    private TextView mTextView;
    private Button mButton;

    public static void init(EmptyViewUtil.EmptyViewBuilder defaultConfig) {
        TheEmptyView.mConfig = defaultConfig;
    }

    public static boolean hasDefaultConfig() {
        return TheEmptyView.mConfig != null;
    }

    public static EmptyViewUtil.EmptyViewBuilder getConfig() {
        return mConfig;
    }

    public TheEmptyView(Context context) {
        this(context, null);
    }

    public TheEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.simple_empty_view, this);

        mTextView = (TextView) findViewById(R.id.t_emptyTextView);
        mImageView = (ImageView) findViewById(R.id.t_emptyImageIcon);
        mButton = (Button) findViewById(R.id.t_emptyButton);

    }

    public void setShowIcon(boolean mShowIcon) {
        this.mShowIcon = mShowIcon;
        mImageView.setVisibility(mShowIcon ? VISIBLE : GONE);
    }

    public void setShowText(boolean showText) {
        this.mShowText = showText;
        mTextView.setVisibility(showText ? VISIBLE : GONE);
    }

    public void setShowButton(boolean showButton) {
        this.mShowButton = showButton;
        mButton.setVisibility(showButton ? VISIBLE : GONE);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        mTextView.setTextSize(mTextSize);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        mTextView.setTextColor(mTextColor);
    }

    public String getEmptyText() {
        return mEmptyText;
    }

    public void setEmptyText(String mEmptyText) {
        this.mEmptyText = mEmptyText;
        mTextView.setText(mEmptyText);
    }

    public void setIcon(int mIconSrc) {
        this.mIconSrc = mIconSrc;
        mImageView.setImageResource(mIconSrc);
    }

    public void setIcon(Drawable drawable) {
        this.mIconSrc = 0;
        mImageView.setImageDrawable(drawable);
    }

    public void setAction(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        mButton.setOnClickListener(onClickListener);
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
        mButton.setText(actionText);
    }
}
