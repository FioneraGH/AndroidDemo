package com.fionera.wechatdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

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
        radius = (int) a.getDimension(R.styleable.ArcMenu_radiu,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                        context.getResources().getDisplayMetrics()));

        a.recycle();

        Log.d("ArcView", pos + " " + radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            //测量childs!!!!!
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed) {
            layoutMainButton();

            for (int i = 1; i < getChildCount(); i++) {
                View child = getChildAt(i);

                int cl = (int) (radius * Math.sin(Math.PI / 2 / (getChildCount() - 2) * (i - 1)));
                int ct = (int) (radius * Math.cos(Math.PI / 2 / (getChildCount() - 2) * (i - 1)));
                int cw = child.getMeasuredWidth();
                int ch = child.getMeasuredHeight();

                switch (position) {
                    case LEFT_TOP:
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
    public void layoutMainButton() {
        mainButton = getChildAt(0);
        mainButton.setOnClickListener(this);

        int l = 0;
        int t = 0;
        int height = mainButton.getMeasuredHeight();
        int width = mainButton.getMeasuredWidth();
        switch (position) {
            case LEFT_TOP:
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
        mainButton = findViewById(R.id.iv_arc_menu);

        rotateButton(v, 0f, 360f, 300);
        toggleMenu(300);
    }

    private void rotateButton(View v, float start, float end, int time) {

        RotateAnimation rotateAnimation = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(time);
        rotateAnimation.setFillAfter(true);
        v.startAnimation(rotateAnimation);
    }

    /**
     * 主按钮开关子按钮
     * @param time
     */
    public void toggleMenu(int time) {
        /**
         * 平移动画和旋转动画
         */

        for (int i = 1; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            //点击即显示子按钮
            child.setVisibility(View.VISIBLE);

            int cl = (int) (radius * Math.sin(Math.PI / 2 / (getChildCount() - 2) * (i - 1)));
            int ct = (int) (radius * Math.cos(Math.PI / 2 / (getChildCount() - 2) * (i - 1)));
            int xflag = 1;
            int yflag = 1;

            switch (position) {
                case LEFT_TOP:
                    xflag = -1;
                    yflag = -1;
                    break;
                case LEFT_BOTTOM:
                    xflag = -1;
                    break;
                case RIGHT_TOP:
                    yflag = -1;
                    break;
                case RIGHT_BOTTOM:
                    break;
            }

            AnimationSet animationSet = new AnimationSet(true);
            TranslateAnimation translateAnimation;
            RotateAnimation rotateAnimation = new RotateAnimation(0f, 720f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(time);
            rotateAnimation.setFillAfter(true);

            if (!menuIsOpen) {
                translateAnimation = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                child.setClickable(true);
                child.setFocusable(true);
            } else {
                translateAnimation = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
                child.setClickable(false);
                child.setFocusable(false);
            }

            translateAnimation.setDuration(time);
            translateAnimation.setStartOffset(i * 100 / getChildCount());
            translateAnimation.setFillAfter(true);

            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    /**
                     * 动画结束若是关闭状态隐藏子按钮
                     */
                    if (!menuIsOpen) {
                        child.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(translateAnimation);

            child.startAnimation(animationSet);
            final int no = i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null) {
                        onMenuItemClickListener.onClick(child, no);
                    } else {
                        Log.d("ArcMenu", "listener is null");
                    }
                    menuIsOpen = menuIsOpen ? false : true;
                    foldItem(no);
                }
            });
        }
        menuIsOpen = menuIsOpen ? false : true;
    }

    private void foldItem(int pos) {
        for (int i = 1; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (i == pos) {
                child.startAnimation(scaleBig(300));
            } else {
                child.startAnimation(scaleSmall(300));
            }
            child.setClickable(false);
            child.setFocusable(false);
            child.setVisibility(View.GONE);
        }
    }

    private Animation scaleBig(int time) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(time);
        scaleAnimation.setFillAfter(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(time);
        alphaAnimation.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        return animationSet;
    }

    private Animation scaleSmall(int time) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(time);
        scaleAnimation.setFillAfter(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(time);
        alphaAnimation.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        return animationSet;
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
