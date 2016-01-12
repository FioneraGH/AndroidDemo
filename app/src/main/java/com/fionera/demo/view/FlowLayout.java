package com.fionera.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fionera on 15-8-18.
 */
public class FlowLayout extends ViewGroup {


    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //！！！！！！！！！！！！！！！！子View的测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content 需要重新计算
        int width = 0;
        int height = 0;
        int lineWidth = 0;//每一行宽度
        int lineHeight = 0;//每一行高度
        int count = getChildCount();//获取元素总数

        for (int i = 0; i < count; i++) {

            //得到子View
            View child = getChildAt(i);
            //测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到LayoutParams
            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
            //子View占据的宽度
            int childWidth = child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;
            int childHeight = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;

            //是否超长
            if (lineWidth + childWidth > widthSize) {
                //开新行取行宽
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                //开新行记行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                //叠加行宽
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
        }

        //最后一个子View,一定要处理，和下面一样
        width = Math.max(lineWidth, width);
        height += lineHeight;

        Log.d("FlowLayout", "width= " + width + " height= " + height);

        /**
         * 判定是否是wrapcontent
         */
        setMeasuredDimension(
                //
                widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);


    }

    /**
     * 存储所有的View 按行存储
     */
    private List<List<View>> allViews = new ArrayList<List<View>>();
    /**
     * 存储每一行的高度
     */
    private List<Integer> lineHeights = new ArrayList<Integer>();
    /**
     * 存储每一行的View
     */
    private List<View> lineViews;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        allViews.clear();
        lineHeights.clear();

        //上面计算的ViewGroup总宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        int count = getChildCount();

        //！！！！！
        lineViews = new ArrayList<View>();
        /**
         * 遍历保存每一个子View，以及用于布局的参数
         */
        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);

            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;
            int childHeight = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;

            if ((childWidth + lineWidth) > width) {
                lineHeights.add(lineHeight);
                allViews.add(lineViews);

                //重置行宽行高
                lineWidth = 0;
                lineHeight = childHeight;
                //重置存储View的行数组 ！！！
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(child);
        }//for end

        /**
         * 最后一行
         */
        lineHeights.add(lineHeight);
        allViews.add(lineViews);

        int left = 0;
        int top = 0;
        int lineNum = allViews.size();

        for (int i = 0; i < lineNum; i++) {
            //每一行的View和行高
            lineViews = allViews.get(i);
            lineHeight = lineHeights.get(i);

            for (int j = 0; j < lineViews.size(); j++) {

                View child = lineViews.get(j);
                //判断child状态
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + mlp.leftMargin;
                int tc = top + mlp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                Log.d("FlowLayout", lc + " " + tc + " " + rc + " " + bc);

                //布局子View
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
