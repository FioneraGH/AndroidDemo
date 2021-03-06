package com.fionera.demo.fragment;

import android.content.Intent;
import android.view.View;

import com.fionera.base.fragment.BaseFragment;
import com.fionera.demo.R;
import com.fionera.demo.activity.AndroidChartsActivity;
import com.fionera.demo.activity.BottomSheetActivity;
import com.fionera.demo.activity.ClipBoardActivity;
import com.fionera.demo.activity.ConstraintLayoutActivity;
import com.fionera.demo.activity.DoubleHeadTableActivity;
import com.fionera.demo.activity.FlycoPageIndicatorActivity;
import com.fionera.demo.activity.GameActivity;
import com.fionera.demo.activity.MatrixActivity;
import com.fionera.demo.activity.NotificationActivity;
import com.fionera.demo.activity.OpenGlActivity;
import com.fionera.demo.activity.PullToLoadActivity;
import com.fionera.demo.activity.RvWithHeaderActivity;
import com.fionera.demo.activity.RxAndroidActivity;
import com.fionera.demo.activity.SeatTableActivity;
import com.fionera.demo.activity.SmartTabLayoutActivity;
import com.fionera.demo.activity.StickyHeaderActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

/**
 * ExtrasFragment
 *
 * @author fionera
 * @date 15-10-3
 */
public class ExtrasFragment
        extends BaseFragment
        implements View.OnClickListener {

    private int[] ids = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R
            .id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11,
            R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.button16, R.id
            .button17, R.id.button18};

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_extras;
    }

    @Override
    public void initViews(View rootView) {

        setTitleBarText("扩展");

        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        for (int id : ids) {
            rootView.findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                mContext.startActivity(new Intent(mContext, MatrixActivity.class));
                break;
            case R.id.button2:
                mContext.startActivity(new Intent(mContext, ConstraintLayoutActivity.class));
                break;
            case R.id.button3:
                mContext.startActivity(new Intent(mContext, StickyHeaderActivity.class));
                break;
            case R.id.button4:
                mContext.startActivity(new Intent(mContext, PullToLoadActivity.class));
                break;
            case R.id.button5:
                mContext.startActivity(new Intent(mContext, NotificationActivity.class));
                break;
            case R.id.button6:
                mContext.startActivity(new Intent(mContext, OpenGlActivity.class));
                break;
            case R.id.button7:
                mContext.startActivity(new Intent(mContext, RvWithHeaderActivity.class));
                break;
            case R.id.button8:
                Intent intent = new Intent(getActivity(), GameActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        mActivity, view, "testTrans");
                ActivityCompat.startActivity(mActivity, intent, options.toBundle());
                break;
            case R.id.button9:
                mContext.startActivity(new Intent(mContext, AndroidChartsActivity.class));
                break;
            case R.id.button10:
                mContext.startActivity(new Intent(mContext, RxAndroidActivity.class));
                break;
            case R.id.button11:
                mContext.startActivity(new Intent(mContext, BottomSheetActivity.class));
                break;
            case R.id.button12:
                mContext.startActivity(new Intent(mContext, SmartTabLayoutActivity.class));
                break;
            case R.id.button13:
                mContext.startActivity(new Intent(mContext, DoubleHeadTableActivity.class));
                break;
            case R.id.button14:
                mContext.startActivity(new Intent(mContext, FlycoPageIndicatorActivity.class));
                break;
            case R.id.button15:
                break;
            case R.id.button17:
                mContext.startActivity(new Intent(mContext, ClipBoardActivity.class));
                break;
            case R.id.button18:
                mContext.startActivity(new Intent(mContext, SeatTableActivity.class));
                break;
            default:
                break;
        }
    }
}
