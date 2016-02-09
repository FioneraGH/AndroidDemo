package com.fionera.demo.fragment;

import android.view.View;
import android.widget.ImageView;

import com.fionera.demo.R;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by fionera on 15-10-3.
 */
public class BitmapUtilFragment extends BaseFragment {


    @Override
    public int setLayoutResource() {
        return R.layout.fragment_bitmap_util;
    }

    @Override
    public void findViewInThisFunction(View rootView) {

        setTitleBarText("内容");
    }
}
