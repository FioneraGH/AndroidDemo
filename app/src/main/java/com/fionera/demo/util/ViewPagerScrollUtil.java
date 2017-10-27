package com.fionera.demo.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * @author fionera
 * @date 15-12-19
 */
public class ViewPagerScrollUtil {

    public void setSliderTransformDuration(ViewPager pager, int duration) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(), null,
                    duration);
            mScroller.set(pager, scroller);
        } catch (Exception ignored) {

        }
    }

    private class FixedSpeedScroller extends Scroller {
        private int mDuration = 1000;

        FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            this(context, interpolator);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
