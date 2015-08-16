package com.fionera.wechatdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.wechatdemo.R;

/**
 * Created by fionera on 15-8-16.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {


    private enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    ;

    private int pos;
    private Position position = Position.RIGHT_BOTTOM;
    private int radius;
    private boolean menuIsOpen;
    private View mainButton;
    private OnMenuItemClickListener onMenuItemClickListener;

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu,
                defStyle, 0);

        pos = a.getInt(R.styleable.ArcMenu_position, 3);
        switch (pos) {
            case 0:
                position = Position.LEFT_TOP;
                break;
            case 1:
                position = Position.LEFT_BOTTOM;
                break;
            case 2:
                position = Position.RIGHT_TOP;
                break;
            case 3:
                position = Position.RIGHT_BOTTOM;
                break;
        }
        radius = (int) a.getDimension(R.styleable.ArcMenu_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                        context.getResources().getDisplayMetrics()));

        a.recycle();

        Log.d("ArcView", pos + " " + radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            //测量childs
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed) {
            LayoutMainButton();

            for (int i = 1; i < getChildCount(); i++) {
                View child = getChildAt(i);

                int cl = (int) (radius * Math.sin(Math.PI / 2 / (getChildCount() - 2) * (i - 1)));
                int ct = (int) (radius * Math.cos(Math.PI / 2 / (getChildCount() - 2) * (i - 1)));
                int cw = child.getMeasuredWidth();
                int ch = child.getMeasuredHeight();

                switch (position) {
                    case LEFT_TOP:
                        cl = (int) (radius * Math.sin(
                                Math.PI / 2 / (getChildCount() - 2) * (i - 1)));
                        ct = (int) (radius * Math.cos(
                                Math.PI / 2 / (getChildCount() - 2) * (i - 1)));
                        break;
                    case LEFT_BOTTOM:
                        ct = getMeasuredHeight() - (ct + ch);
                        break;
                    case RIGHT_TOP:
                        cl = getMeasuredWidth() - (cl + cw);
                        break;
                    case RIGHT_BOTTOM:
                        cl = getMeasuredWidth() - (cl + cw);
                        ct = getMeasuredHeight() - (ct + ch);
                        break;
                }

                child.layout(cl, ct, cl + cw, ct + ch);
                child.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 定位主菜单按钮
     */
    public void LayoutMainButton() {
        mainButton = getChildAt(0);
        mainButton.setOnClickListener(this);

        int l = 0;
        int t = 0;
        int height = mainButton.getMeasuredHeight();
        int width = mainButton.getMeasuredWidth();
        switch (position) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        int r = l + height;
        int b = t + width;

        mainButton.layout(l, t, r, b);
    }

    @Override
    public void onClick(View v) {

    }


    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    /**
     * 点击菜单回调接口
     */
    public interface OnMenuItemClickListener {
        void onClick(View view, int pos);
    }
}
