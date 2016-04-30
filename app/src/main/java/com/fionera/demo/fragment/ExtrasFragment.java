package com.fionera.demo.fragment;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;

import com.fionera.demo.R;
import com.fionera.demo.activity.BottomSheetActivity;
import com.fionera.demo.activity.DataBindingActivity;
import com.fionera.demo.activity.DialogActivity;
import com.fionera.demo.activity.DoubleHeadTableActivity;
import com.fionera.demo.activity.FlycoPageIndicaorActivity;
import com.fionera.demo.activity.G2048Activity;
import com.fionera.demo.activity.MatrixActivity;
import com.fionera.demo.activity.NotificationActivity;
import com.fionera.demo.activity.PullToLoadActivity;
import com.fionera.demo.activity.RVWithHeaderActivity;
import com.fionera.demo.activity.RecyclerItemDraggerActivity;
import com.fionera.demo.activity.RxAndroidActivity;
import com.fionera.demo.activity.SmartTabLayoutActivity;
import com.fionera.demo.activity.XUtils3Activity;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by fionera on 15-10-3.
 */
public class ExtrasFragment
        extends BaseFragment {

    @ViewInject(R.id.button1)
    private Button matrix;
    @ViewInject(R.id.button2)
    private Button smart;
    @ViewInject(R.id.button3)
    private Button dialog;
    @ViewInject(R.id.button4)
    private Button load;
    @ViewInject(R.id.button5)
    private Button notify;
    @ViewInject(R.id.button6)
    private Button recyclerview;
    @ViewInject(R.id.button7)
    private Button databind;
    @ViewInject(R.id.button8)
    private Button g2048;
    @ViewInject(R.id.button9)
    private Button xutils;
    @ViewInject(R.id.button10)
    private Button rx;
    @ViewInject(R.id.button11)
    private Button bottomsheet;
    @ViewInject(R.id.button12)
    private Button rvwithhead;
    @ViewInject(R.id.button13)
    private Button doublehead;
    @ViewInject(R.id.button14)
    private Button flycoindicator;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_extras;
    }

    @Override
    public void initViews(View rootView) {

        setTitleBarText("扩展");

        matrix.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, MatrixActivity.class)));
        smart.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, SmartTabLayoutActivity.class)));
        dialog.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, DialogActivity.class)));
        load.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, PullToLoadActivity.class)));
        notify.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, NotificationActivity.class)));
        recyclerview.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, RecyclerItemDraggerActivity.class)));
        databind.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, DataBindingActivity.class)));
        g2048.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), G2048Activity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(mActivity, g2048, "testTrans");
            ActivityCompat.startActivity(mActivity, intent, options.toBundle());
        });
        xutils.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, XUtils3Activity.class)));
        rx.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, RxAndroidActivity.class)));
        bottomsheet.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, BottomSheetActivity.class)));
        rvwithhead.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, RVWithHeaderActivity.class)));
        doublehead.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, DoubleHeadTableActivity.class)));
        flycoindicator.setOnClickListener(
                v -> mContext.startActivity(new Intent(mContext, FlycoPageIndicaorActivity.class)));
    }
}
