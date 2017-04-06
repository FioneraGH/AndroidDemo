package com.fionera.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.fionera.base.util.LogCat;
import com.fionera.demo.DemoApplication;
import com.fionera.demo.R;

/**
 * SlidingMenu
 * Created by fionera on 15-8-14.
 */

public class SlidingMenu
        extends HorizontalScrollView implements Runnable {

    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mMenuRightPadding;

    private int mWidthScreen = DemoApplication.screenWidth;
    private int mMenuWidth;

    private boolean firstMeasure;
    private boolean isOpen;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu,
                defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelSize(attr, (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                                    context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
    }

    /**
     * 设置子View的宽和高以及自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!firstMeasure) {
            LinearLayout mWrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mContent = (ViewGroup) mWrapper.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mWidthScreen - mMenuRightPadding;
            mContent.getLayoutParams().width = mWidthScreen;
            firstMeasure = true;
        }
    }

    /**
     * 布局参数，通过设置偏移量将Menu隐藏
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        LogCat.d(mMenuWidth + "");
        if (changed) {
            postDelayed(this, 1);
        }
    }

    @Override
    public void run() {
        scrollTo(mMenuWidth, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                // 隐藏在左边的宽度
                smoothScrollTo(getScrollX() > mMenuWidth / 2 ? mMenuWidth : 0, 0);
                isOpen = !isOpen;
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @SuppressWarnings("unused")
    public void toggleMenu() {
        smoothScrollTo(isOpen ? mMenuWidth : 0, 0);
        isOpen = !isOpen;
    }

    /**
     * 滚动时设置
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        /*
          l即是当前getScrollX的值，初值为mMenuWidth -> 0
         */
        float scale = l * 1.0f / mMenuWidth;

        mContent.setScaleX(0.9f + 0.1f * scale);
        mContent.setScaleY(0.9f + 0.1f * scale);
        mContent.setPivotX(0);
        mContent.setPivotY(mContent.getHeight() / 2);

        mMenu.setScaleX(1.0f - 0.3f * scale);
        mMenu.setScaleY(1.0f - 0.3f * scale);

        /*
          调用属性动画，设置TransactionX
         */
        mMenu.setTranslationX(mMenuWidth * scale * 0.7f);
        mMenu.setAlpha(1.0f - 1.0f * scale);
    }
}
