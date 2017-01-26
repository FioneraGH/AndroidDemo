package com.fionera.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fionera.demo.R;
import com.fionera.demo.util.DisplayUtils;
import com.fionera.demo.view.TitleBar;

import org.xutils.x;

public abstract class BaseFragment
        extends Fragment {

    protected Context mContext;
    protected Activity mActivity;
    protected LinearLayout rootView;
    protected TitleBar titleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = (LinearLayout) inflater.inflate(R.layout.layout_title_bar_content, container,
                    false);
            View contentView = inflater.inflate(setLayoutResource(), rootView, false);
            rootView.addView(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            titleBar = (TitleBar) rootView.getChildAt(0);

            Toolbar toolbar = (Toolbar) titleBar.getChildAt(0);
            ((AppCompatActivity) mActivity).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }

            ViewCompat.setElevation(titleBar, DisplayUtils.dp2px(5));
            x.view().inject(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initViews(view);
    }

    public abstract int setLayoutResource();

    public abstract void initViews(View rootView);

    public void setTitleBarText(String title) {
        titleBar.setTitleBarText(title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity) context;
    }
}
