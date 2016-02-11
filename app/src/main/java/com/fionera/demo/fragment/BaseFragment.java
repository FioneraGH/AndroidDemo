package com.fionera.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fionera.demo.R;
import com.fionera.demo.view.TitleBar;

import org.xutils.x;

public abstract class BaseFragment
        extends Fragment {

    protected Context mContext;
    protected Activity mActivity;

    protected final int TITLE_LEFT_ID = TitleBar.LEFT_ID;
    protected final int TITLE_RIGHT_ID = TitleBar.RIGHT_ID;
    protected final int TITLE_TEXT_ID = TitleBar.TITLE_ID;

    private TitleBar titleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout rootView = (LinearLayout) inflater
                .inflate(R.layout.layout_title_bar_content, container, false);
        View contentView = inflater.inflate(setLayoutResource(), rootView, false);
        rootView.addView(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                         LinearLayout.LayoutParams.MATCH_PARENT);
        titleBar = (TitleBar) rootView.getChildAt(0);
        ViewCompat.setElevation(titleBar, 5);
        x.view().inject(this, rootView);
        findViewInThisFunction(rootView);
        return rootView;
    }

    public abstract int setLayoutResource();

    public abstract void findViewInThisFunction(View rootView);

    public void setTitleBarVisibility(int visibility) {
        titleBar.setVisibility(visibility);
    }

    public void setTitleBarText(String title) {
        titleBar.setTitleBarText(title);
    }

    public void setTitleBarTextColor(int id) {
        titleBar.setTitleBarTextColor(ContextCompat.getColor(mContext, id));
    }

    public void setTitleBarLeft(int drawable) {
        titleBar.setLeftDrawable(drawable);
    }

    public void setTitleBarRight(int drawable) {
        titleBar.setRightDrawable(drawable);
    }

    public void setTitleBarLeft(String leftText) {
        titleBar.setLeftText(leftText);
    }

    public void setTitleBarRight(String rightText) {
        titleBar.setRightText(rightText);
    }

    public void setTitleBarLeftVisibility(int visibility) {
        titleBar.setLeftVisibility(visibility);
    }

    public void setTitleBarRight(String rightText, int iconId, boolean iconAtLeft) {
        titleBar.setRightTextAndIcon(rightText, iconId, iconAtLeft);
    }

    public void setTitleBarLeft(String leftText, int iconId, boolean iconAtLeft) {
        titleBar.setLeftTextAndIcon(leftText, iconId, iconAtLeft);
    }

    public void setTitleBarRightVisibility(int visibility) {
        titleBar.setRightVisibility(visibility);
    }

    public void setTitleBarLeftClick(View.OnClickListener onLeftClickListener) {
        titleBar.setTitleBarLeftClick(onLeftClickListener);
    }

    public void setTitleBarRightClick(View.OnClickListener onRightClickListener) {
        titleBar.setTitleBarRightClick(onRightClickListener);
    }

    public void setTitleBarTitleClick(View.OnClickListener onTitleClickListener) {
        titleBar.setTitleBarTitleClick(onTitleClickListener);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        setHasOptionsMenu(false);
        mContext = context;
        mActivity = (Activity) context;
    }
}
