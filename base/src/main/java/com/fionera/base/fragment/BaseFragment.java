package com.fionera.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fionera.base.R;
import com.fionera.base.util.DisplayUtils;
import com.fionera.base.widget.TitleBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment
        extends Fragment {

    protected Context mContext;
    protected Activity mActivity;
    protected LinearLayout rootView;
    protected TitleBar titleBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
