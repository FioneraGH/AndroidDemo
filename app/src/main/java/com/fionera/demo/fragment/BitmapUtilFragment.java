package com.fionera.demo.fragment;

import android.view.View;

import com.fionera.demo.R;

/**
 * Created by fionera on 15-10-3.
 */
public class BitmapUtilFragment extends BaseFragment {


    @Override
    public int setLayoutResource() {
        return R.layout.fragment_bitmap_util;
    }

    @Override
    public void initViews(View rootView) {

        setTitleBarText("内容");
    }
}
