package com.fionera.demo.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.fionera.demo.R;
import com.fionera.demo.activity.MatrixActivity;
import com.fionera.demo.activity.PullToLoadActivity;
import com.fionera.demo.activity.PullToRefreshActivity;
import com.fionera.demo.activity.RecycleActivity;
import com.fionera.demo.activity.SplitPageActivity;
import com.fionera.demo.activity.XUtils3Activity;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by fionera on 15-10-3.
 */
public class ExtrasFragment extends BaseFragment {


    @ViewInject(R.id.button1)
    private Button matrix;
    @ViewInject(R.id.button2)
    private Button fresh;
    @ViewInject(R.id.button3)
    private Button split;
    @ViewInject(R.id.button4)
    private Button load;
    @ViewInject(R.id.button6)
    private Button recycle;
    @ViewInject(R.id.button9)
    private Button volley;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_extras;
    }

    @Override
    public void findViewInThisFunction(View rootView) {

        setTitleBarText("扩展");

        // 设定Matrix跳转
        matrix.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MatrixActivity.class);
            mContext.startActivity(intent);
        });
        // 设定下拉刷新跳转
        fresh.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PullToRefreshActivity.class);
            mContext.startActivity(intent);
        });
        // 设定分页加载跳转
        split.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SplitPageActivity.class);
            mContext.startActivity(intent);
        });
        // 设定下拉加载跳转
        load.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PullToLoadActivity.class);
            mContext.startActivity(intent);
        });
        // 设定RecyclerView测试跳转
        recycle.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, RecycleActivity.class);
            mContext.startActivity(intent);
        });
        // 设定Volley测试跳转
        volley.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, XUtils3Activity.class);
            mContext.startActivity(intent);
        });
    }
}
