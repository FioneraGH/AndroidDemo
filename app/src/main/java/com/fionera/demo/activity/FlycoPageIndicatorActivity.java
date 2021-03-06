package com.fionera.demo.activity;

import android.os.Bundle;

import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.fionera.demo.util.pageindicator.anim.select.ZoomInEnter;
import com.fionera.demo.util.pageindicator.banner.BaseBanner;
import com.fionera.demo.util.pageindicator.banner.SimpleImageBanner;
import com.fionera.demo.util.pageindicator.indicator.FlycoPageIndicator;
import com.fionera.demo.util.pageindicator.indicator.base.PageIndicator;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author fionera
 */
public class FlycoPageIndicatorActivity
        extends AppCompatActivity {
    private int[] resIds = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R
            .mipmap.ic_launcher};
    private ArrayList<Integer> resList;
    private BaseBanner<Integer, SimpleImageBanner> banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyco_indicator);

        resList = new ArrayList<>();
        for (int resId : resIds) {
            resList.add(resId);
        }
        banner = (SimpleImageBanner) findViewById(R.id.vp_flyco_indicator);
        banner.setSource(resList).startScroll();
        banner.setOnItemClickL(position -> ShowToast.show("点击" + position));

        indicator(R.id.indicator_circle);
        indicator(R.id.indicator_square);
        indicator(R.id.indicator_round_rectangle);
        indicator(R.id.indicator_circle_stroke);
        indicator(R.id.indicator_square_stroke);
        indicator(R.id.indicator_round_rectangle_stroke);
        indicator(R.id.indicator_circle_snap);
        indicator(R.id.indicator_round_rectangle_anim);

        indicatorAnim();
        indicatorRes();

        indicator(R.id.indicator_circle_r);
        indicator(R.id.indicator_square_r);
        indicator(R.id.indicator_round_rectangle_r);
        indicator(R.id.indicator_circle_stroke_r);
        indicator(R.id.indicator_square_stroke_r);
        indicator(R.id.indicator_round_rectangle_stroke_r);
    }

    private void indicator(int indicatorId) {
        final PageIndicator indicator = findViewById(indicatorId);
        indicator.setViewPager(banner.getViewPager(), resList.size());
    }

    private void indicatorAnim() {
        final FlycoPageIndicator indicator = findViewById(
                R.id.indicator_circle_anim);
        indicator.setIsSnap(true).setSelectAnimClass(ZoomInEnter.class)
                .setViewPager(banner.getViewPager(), resList.size());
    }

    private void indicatorRes() {
        final FlycoPageIndicator indicatorRes = findViewById(
                R.id.indicator_res);
        indicatorRes.setViewPager(banner.getViewPager(), resList.size());
    }
}
