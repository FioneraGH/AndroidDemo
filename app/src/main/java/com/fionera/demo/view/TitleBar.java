package com.fionera.demo.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.util.DisplayUtils;


/**
 * Created by victor on 15-9-22.
 */
public class TitleBar
        extends LinearLayout {

    public LinearLayout mLayoutLeft, mLayoutRight;
    private ImageView mIvLeft, mIvRight;
    private TextView mTvLeft, mTvRight, mTvTitle;

    public static final int LEFT_ID = R.id.title_bar_left;
    public static final int RIGHT_ID = R.id.title_bar_right;
    public static final int TITLE_ID = R.id.title_bar_title;

    public TitleBar(Context context) {
        this(context,null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        View titleBar = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, null);
        addView(titleBar, LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(50));

        mLayoutLeft = (LinearLayout) titleBar.findViewById(R.id.title_bar_left);
        mLayoutRight = (LinearLayout) titleBar.findViewById(R.id.title_bar_right);
        mIvLeft = (ImageView) titleBar.findViewById(R.id.title_bar_left_icon);
        mIvRight = (ImageView) titleBar.findViewById(R.id.title_bar_right_icon);
        mTvLeft = (TextView) titleBar.findViewById(R.id.title_bar_left_text);
        mTvRight = (TextView) titleBar.findViewById(R.id.title_bar_right_text);
        mTvTitle = (TextView) titleBar.findViewById(R.id.title_bar_title);
    }

    public void setTitleBarText(String title) {
        mTvTitle.setText(title);
    }

    public void setTitleBarTextColor(int id) {
        mTvTitle.setTextColor(id);
    }

    public void setLeftDrawable(int id) {
        mIvLeft.setVisibility(VISIBLE);
        mTvLeft.setVisibility(GONE);
        mIvLeft.setImageDrawable(ContextCompat.getDrawable(getContext(), id));
    }

    public void setLeftText(String leftText) {
        mTvLeft.setVisibility(VISIBLE);
        mIvLeft.setVisibility(GONE);
        mTvLeft.setText(leftText);
    }

    public void setLeftTextColor(int colorId) {
        mTvLeft.setTextColor(ContextCompat.getColor(getContext(), colorId));
    }

    public void setRightTextColor(int colorId) {
        mTvRight.setTextColor(ContextCompat.getColor(getContext(), colorId));
    }

    public void setLeftTextSize(float size) {
        mTvLeft.setTextScaleX(size);
    }

    public void setRightTextSize(float size) {
        mTvRight.setTextSize(size);
    }


    public void setRightDrawable(int id) {
        mIvRight.setVisibility(VISIBLE);
        mTvRight.setVisibility(GONE);
        mIvRight.setImageDrawable(ContextCompat.getDrawable(getContext(), id));
    }

    public void setRightText(String leftText) {
        mTvRight.setVisibility(VISIBLE);
        mIvRight.setVisibility(GONE);
        mTvRight.setText(leftText);
    }

    public void setLeftVisibility(int visibility) {
        mLayoutLeft.setVisibility(visibility);
    }

    public void setRightVisibility(int visibility) {
        toggleRight(visibility == View.VISIBLE);
    }

    private void toggleRight(boolean state) {
        if (state) {
            mLayoutRight.setVisibility(View.VISIBLE);
            mLayoutRight.setAnimation(
                    AnimationUtils.loadAnimation(getContext(), R.anim.anim_iv_top_visible));
        } else {
            mLayoutRight.setVisibility(View.INVISIBLE);
            mLayoutRight.setAnimation(
                    AnimationUtils.loadAnimation(getContext(), R.anim.anim_iv_top_invisible));
        }
    }


    public void setTitleBarLeftClick(OnClickListener onLeftClickListener) {
        mLayoutLeft.setOnClickListener(onLeftClickListener);
    }

    public void setTitleBarTitleClick(OnClickListener onTitleClickListener) {
        mTvTitle.setOnClickListener(onTitleClickListener);
    }

    public void setTitleBarRightClick(OnClickListener onRightClickListener) {
        mLayoutRight.setOnClickListener(onRightClickListener);
    }

    public void setLeftTextAndIcon(String leftText, int iconId, boolean iconAtLeft) {
        mTvLeft.setText(leftText);
        mTvLeft.setVisibility(VISIBLE);
        mIvLeft.setVisibility(GONE);
        if (iconAtLeft) {
            mTvLeft.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(getContext(), iconId), null, null, null);
        } else {
            mTvLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat
                    .getDrawable(getContext(), iconId), null);
        }
        mTvRight.setCompoundDrawablePadding(10);
    }

    public void setRightTextAndIcon(String rightText, int iconId, boolean iconAtLeft) {
        mTvRight.setText(rightText);
        mTvRight.setVisibility(VISIBLE);
        mIvRight.setVisibility(GONE);
        if (iconAtLeft) {
            mTvRight.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(getContext(), iconId), null, null, null);
        } else {
            mTvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat
                    .getDrawable(getContext(), iconId), null);
        }
        mTvRight.setCompoundDrawablePadding(10);
    }

}
