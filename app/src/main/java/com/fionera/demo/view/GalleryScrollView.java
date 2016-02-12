package com.fionera.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by fionera on 16-2-11.
 */
public class GalleryScrollView
        extends HorizontalScrollView {
    private int subChildCount = 0;
    private int downX = 0;
    private int currentPage = 0;
    private ArrayList<Integer> pointList = new ArrayList<>();

    public GalleryScrollView(Context context) {
        this(context, null);
    }

    public GalleryScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        receiveChildInfo();
    }

    public void receiveChildInfo() {

        LinearLayout firstChild = (LinearLayout) getChildAt(0);
        if (firstChild != null) {
            subChildCount = firstChild.getChildCount();
            for (int i = 0; i < subChildCount; i++) {
                if (firstChild.getChildAt(i).getWidth() > 0) {
                    if (0 != i) {
                        pointList.add(firstChild.getChildAt(i).getLeft() - firstChild.getChildAt(i)
                                .getWidth() / 4);
                    } else {
                        pointList.add(firstChild.getChildAt(i).getLeft());
                    }
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (Math.abs((ev.getX() - downX)) > getWidth() / 4) {
                    if (ev.getX() - downX > 0) {
                        smoothScrollToPrePage();
                    } else {
                        smoothScrollToNextPage();
                    }
                } else {
                    smoothScrollToCurrent();
                }
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void smoothScrollToCurrent() {
        smoothScrollTo(pointList.get(currentPage), 0);
    }

    private void smoothScrollToNextPage() {
        if (currentPage < subChildCount - 1) {
            currentPage++;
            smoothScrollTo(pointList.get(currentPage), 0);
        }
    }

    private void smoothScrollToPrePage() {
        if (currentPage > 0) {
            currentPage--;
            smoothScrollTo(pointList.get(currentPage), 0);
        }
    }

    /**
     * 上一页
     */
    public void prePage() {
        smoothScrollToPrePage();
    }

    /**
     * 下一页
     */
    public void nextPage() {
        smoothScrollToNextPage();
    }

    /**
     * 跳转到指定的页面
     */
    public boolean gotoPage(int position) {
        if (position > 0 && position < subChildCount - 1) {
            smoothScrollTo(pointList.get(position), 0);
            currentPage = position;
            return true;
        }
        return false;
    }
}
