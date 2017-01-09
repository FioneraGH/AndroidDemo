package com.fionera.demo.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.demo.DemoApplication;

/**
 * Created by fionera on 16-2-14.
 */
public class ViewDragLayout
        extends ViewGroup {

    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;

    private View mLeftMenuView;
    private View mContentView;

    private ViewDragHelper mHelper;

    public ViewDragLayout(Context context) {
        this(context, null);
    }

    public ViewDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        final float minVel = MIN_FLING_VELOCITY * DemoApplication.screenDensity;

        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public int getViewHorizontalDragRange(View child) {
                return child.equals(mLeftMenuView) ? child.getWidth() : 0;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return Math.max(-child.getWidth(), Math.min(left, 0));
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child.equals(mLeftMenuView);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mHelper.captureChildView(mLeftMenuView, pointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                final int childWidth = releasedChild.getWidth();
                float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
                mHelper.settleCapturedViewAt(
                        xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth,
                        releasedChild.getTop());
                invalidate();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            }

        });
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mHelper.setMinVelocity(minVel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        View leftMenuView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams) leftMenuView.getLayoutParams();

        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                                                        getPaddingLeft() + getPaddingRight(),
                                                        lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                                                         getPaddingTop() + getPaddingBottom() +
                                                                 lp.topMargin + lp.bottomMargin,
                                                         lp.height);
        leftMenuView.measure(drawerWidthSpec, drawerHeightSpec);


        View contentView = getChildAt(0);
        lp = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec
                .makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec
                .makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        mLeftMenuView = leftMenuView;
        mContentView = contentView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View menuView = mLeftMenuView;
        View contentView = mContentView;

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView
                .layout(lp.leftMargin, lp.topMargin, lp.leftMargin + contentView.getMeasuredWidth(),
                        lp.topMargin + contentView.getMeasuredHeight());

        lp = (MarginLayoutParams) menuView.getLayoutParams();
        menuView.layout(-menuView.getMeasuredWidth() + lp.leftMargin, lp.topMargin, -lp.rightMargin,
                        lp.topMargin + menuView.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * 更改布局参数为Margin
     */
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                      ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    public void openDrawer() {
        View menuView = mLeftMenuView;
        mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
        /*
          防止调用无效
         */
        postInvalidate();
    }

    public void closeDrawer() {
        View menuView = mLeftMenuView;
        mHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), menuView.getTop());
        postInvalidate();
    }
}
