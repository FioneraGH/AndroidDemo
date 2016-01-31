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


public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected Activity mActivity;

    protected final int TITLE_LEFT_ID = TitleBar.LEFT_ID;
    protected final int TITLE_RIGHT_ID = TitleBar.RIGHT_ID;
    private View mLayoutMain;
    private TitleBar mTitleBar;
    private LinearLayout mLayoutContent;
    private View mContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLayoutMain = inflater.inflate(R.layout.layout_title_bar_content, container, false);
        mLayoutContent = (LinearLayout) mLayoutMain.findViewById(R.id.title_bar_content);
        mTitleBar = (TitleBar) mLayoutMain.findViewById(R.id.title_bar);
        ViewCompat.setElevation(mTitleBar,5);
        mContent = inflater.inflate(setLayoutResource(), null);
        mLayoutContent.addView(mContent, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        x.view().inject(this, mLayoutMain);
        findViewInThisFunction(mLayoutMain);
        return mLayoutMain;
    }

    public abstract int setLayoutResource();

    public abstract void findViewInThisFunction(View rootView);

    public void setTitleBarVisibility(int visibility) {
        mTitleBar.setVisibility(visibility);
    }

    public void setTitleBarText(String title) {
        mTitleBar.setTitleBarText(title);
    }

    public void setTitleBarColor(int id) {
        mTitleBar.setTitleBarColor(ContextCompat.getColor(mContext, id));
    }

    public void setTitleBarLeft(int drawable) {
        mTitleBar.setLeftDrawable(drawable);
    }

    public void setTitleBarRight(int drawable) {
        mTitleBar.setRightDrawable(drawable);
    }

    public void setTitleBarLeft(String leftText) {
        mTitleBar.setLeftText(leftText);
    }

    public void setTitleBarRight(String rightText) {
        mTitleBar.setRightText(rightText);
    }

    public void setTitleBarLeftVisibility(int visibility) {
        mTitleBar.setLeftVisibility(visibility);
    }

    public void setTitleBarRight(String rightText, int iconId, boolean iconAtLeft) {
        mTitleBar.setRightTextAndIcon(rightText, iconId, iconAtLeft);
    }

    public void setTitleBarLeft(String leftText, int iconId, boolean iconAtLeft) {
        mTitleBar.setLeftTextAndIcon(leftText, iconId, iconAtLeft);
    }

    public void setTitleBarRightVisibility(int visibility) {
        mTitleBar.setRightVisibility(visibility);
    }

    public void setTitleBarLeftClick(View.OnClickListener onLeftClickListener) {
        mTitleBar.setTitleBarLeftClick(onLeftClickListener);
    }

    public void setTitleBarRightClick(View.OnClickListener onRightClickListener) {
        mTitleBar.setTitleBarRightClick(onRightClickListener);
    }

    public void setTitleBarTitleClick(View.OnClickListener onTitleClickListener) {
        mTitleBar.setTitleBarTitleClick(onTitleClickListener);
    }

    @Override
    public void onAttach(Context context) {

        setHasOptionsMenu(true);
        mContext = context;
        mActivity = (Activity) context;
        super.onAttach(context);
    }
}
