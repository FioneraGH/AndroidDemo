package com.fionera.demo.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.fionera.demo.activity.DoubleHeadTableActivity;

public class ListNestedScrollView
		extends HorizontalScrollView {
	
	private DoubleHeadTableActivity activity;

	public ListNestedScrollView(Context context) {
		this(context,null);
	}

	public ListNestedScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
        activity = (DoubleHeadTableActivity) context;
        setOverScrollMode(OVER_SCROLL_NEVER);
	}

	public ListNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		activity = (DoubleHeadTableActivity) context;
        setOverScrollMode(OVER_SCROLL_NEVER);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		activity.setTouchView(this);
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(activity.getTouchView() == this) {
			activity.onScrollChanged(l, t);
		}else{
			super.onScrollChanged(l, t, oldl, oldt);
		}
	}
}
