package com.fionera.wechatdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by fionera on 15-8-14.
 */
public class SlidingMenu extends HorizontalScrollView {


    private LinearLayout mWrapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mWidthScreen;

    private int mMenuRightPadding;
    private boolean once = false;

    private int mMenuWidth;

    /**
     * 未使用自定义属性时，调用
     *
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidthScreen = outMetrics.widthPixels;

        //把50dp转换为像素值
        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 设置子View的宽和高以及自己的宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!once) {
            mWrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mContent = (ViewGroup) mWrapper.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mWidthScreen - mMenuRightPadding;
            mContent.getLayoutParams().width = mWidthScreen;
//            mWrapper.getLayoutParams().width = mWidthScreen+ mMenuRightPadding;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 布局参数，通过设置偏移量将Menu隐藏
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        if(changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:

                //隐藏在左边的宽度
                int x = getScrollX();
                if(x >mMenuWidth/2){
                    this.smoothScrollTo(mMenuWidth,0);
                }else{
                    this.smoothScrollTo(0,0);
                }

                return true;
        }

        //不能覆盖，会造成其他Action失效
        return super.onTouchEvent(ev);
    }
}
