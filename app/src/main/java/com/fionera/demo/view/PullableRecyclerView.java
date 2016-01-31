package com.fionera.demo.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.fionera.demo.util.Pullable;

public class PullableRecyclerView
        extends RecyclerView
        implements Pullable {

    private int totalCount = 0;

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getChildCount() == 0 || ((LinearLayoutManager) getLayoutManager())
                .findFirstCompletelyVisibleItemPosition() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (getChildCount() == 0 || ((LinearLayoutManager) getLayoutManager())
                .findLastCompletelyVisibleItemPosition() == totalCount ) {
            return true;
        } else {
            return false;
        }
    }

}
