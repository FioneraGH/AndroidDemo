package com.fionera.demo.util.pageindicator.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fionera.demo.DemoApplication;
import com.fionera.demo.R;
import com.fionera.demo.util.pageindicator.indicator.RoundCornerIndicator;

/**
 * @author fionera
 */
public class SimpleImageBanner
        extends BaseBanner<Integer, SimpleImageBanner> {

    public SimpleImageBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onTitleSelect(TextView tv, int position) {
        super.onTitleSelect(tv, position);
        tv.setText("当前" + position);
    }

    @Override
    public View onCreateItemView(int position) {
        ImageView iv = new ImageView(getContext());
        int itemWidth = DemoApplication.screenWidth;
        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        iv.setImageResource(R.mipmap.ic_launcher);
        return iv;
    }

    private RoundCornerIndicator indicator;

    @Override
    public View onCreateIndicator() {
        indicator = new RoundCornerIndicator(getContext());
        indicator.setViewPager(getViewPager());
        return indicator;
    }

    @Override
    public void setCurrentIndicator(int i) {
        indicator.setCurrentItem(i);
    }
}