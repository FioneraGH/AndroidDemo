package com.fionera.wechatdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.util.LogUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by fionera on 15-10-3.
 */
@ContentView(R.layout.fragment_bitmap_util)
public class BitmapUtilFragment extends Fragment {

    @ViewInject(R.id.iv_bitmap_util_preview)
    private ImageView ivBitmapUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = x.view().inject(this,inflater,container);

        x.image().bind(ivBitmapUtil, "https://ss0.bdstatic" + "" +
                        ".com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png",
                ImageOptions.DEFAULT);

        return view;
    }
}
