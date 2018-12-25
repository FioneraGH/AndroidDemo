package com.fionera.demo.util.pageindicator.indicator.base;

import androidx.viewpager.widget.ViewPager;

/**
 * @author fionera
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {
    /**
     * bind ViewPager
     */
    void setViewPager(ViewPager vp);

    /**
     * for special viewpager,such as LooperViewPager
     */
    void setViewPager(ViewPager vp, int realCount);

    void setCurrentItem(int item);
}
