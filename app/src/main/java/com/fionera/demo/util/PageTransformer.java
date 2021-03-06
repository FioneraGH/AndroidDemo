package com.fionera.demo.util;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * @author fionera
 */
public class PageTransformer
        implements ViewPager.PageTransformer {

    private static final float MAX_SIZE = 1.0f;
    private static final float MIN_SIZE = 0.8f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
        float offset;
        if (position < 0) {
            offset = 1 + position;
        } else {
            offset = 1 - position;
        }
        float scaleSize = MIN_SIZE + offset * (MAX_SIZE - MIN_SIZE);
        page.setScaleY(scaleSize);
        page.setScaleX(scaleSize);
    }
}
