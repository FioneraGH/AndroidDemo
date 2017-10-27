package com.fionera.demo.util.pageindicator.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fionera.base.util.DisplayUtils;
import com.fionera.demo.DemoApplication;
import com.fionera.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * BaseBanner
 *
 * @author fionera
 * @date 16-5-4
 */

public abstract class BaseBanner<E, T extends BaseBanner<E, T>>
        extends RelativeLayout {
    /**
     * 日志
     */
    private static final String TAG = BaseBanner.class.getSimpleName();
    /**
     * 单线程池定时任务
     */
    private ScheduledExecutorService scheduledExecutorService;
    /**
     * ViewPager
     */
    private ViewPager mViewPager;
    /**
     * 数据源
     */
    List<E> mDatas = new ArrayList<>();
    /**
     * 当前position
     */
    private int mCurrentPosition;
    /**
     * 多久后开始滚动
     */
    private long mDelay;
    /**
     * 滚动间隔
     */
    private long mPeriod;
    /**
     * 是否自动滚动
     */
    private boolean mIsAutoScrollEnable;
    /**
     * 是否正在自动滚动中
     */
    private boolean mIsAutoScrolling;

    /**
     * 显示器(小点)的最顶层父容器
     */
    private RelativeLayout mRlBottomBarParent;
    private int mItemWidth;
    private int mItemHeight;

    /**
     * 显示器和标题的直接父容器
     */
    private LinearLayout mLlBottomBar;
    /**
     * 最后一条item是否显示背景条
     */
    private boolean mIsBarShowWhenLast;

    /**
     * 显示器的的直接容器
     */
    private LinearLayout mLlIndicatorContainer;

    /**
     * 标题
     */
    private TextView mTvTitle;

    public BaseBanner(Context context) {
        this(context, null, 0);
    }

    public BaseBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseBanner);
        float scale = ta.getFloat(R.styleable.BaseBanner_bb_scale, -1);

        boolean isLoopEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isLoopEnable, true);
        mDelay = ta.getInt(R.styleable.BaseBanner_bb_delay, 5);
        mPeriod = ta.getInt(R.styleable.BaseBanner_bb_period, 5);
        mIsAutoScrollEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isAutoScrollEnable, true);

        int barColor = ta.getColor(R.styleable.BaseBanner_bb_barColor, Color.TRANSPARENT);
        mIsBarShowWhenLast = ta.getBoolean(R.styleable.BaseBanner_bb_isBarShowWhenLast, true);
        int indicatorGravity = ta.getInt(R.styleable.BaseBanner_bb_indicatorGravity,
                Gravity.CENTER);
        float barPaddingLeft = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingLeft,
                DisplayUtils.dp2px(10));
        float barPaddingTop = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingTop,
                DisplayUtils.dp2px(indicatorGravity == Gravity.CENTER ? 6 : 2));
        float barPaddingRight = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingRight,
                DisplayUtils.dp2px(10));
        float barPaddingBottom = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingBottom,
                DisplayUtils.dp2px(indicatorGravity == Gravity.CENTER ? 6 : 2));
        int textColor = ta.getColor(R.styleable.BaseBanner_bb_textColor,
                Color.parseColor("#ffffff"));
        float textSize = ta.getDimension(R.styleable.BaseBanner_bb_textSize,
                DisplayUtils.sp2px(12.5f));
        boolean isTitleShow = ta.getBoolean(R.styleable.BaseBanner_bb_isTitleShow, true);
        boolean isIndicatorShow = ta.getBoolean(R.styleable.BaseBanner_bb_isIndicatorShow, true);
        ta.recycle();

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android",
                "layout_height");

        //create ViewPager
        mViewPager = isLoopEnable ? new LoopViewPager(context) : new ViewPager(context);
        mItemWidth = DemoApplication.screenWidth;
        if (scale < 0) {//scale not set in xml
            switch (height) {
                case ViewGroup.LayoutParams.MATCH_PARENT + "":
                    mItemHeight = LayoutParams.MATCH_PARENT;
                    break;
                case ViewGroup.LayoutParams.WRAP_CONTENT + "":
                    mItemHeight = LayoutParams.WRAP_CONTENT;
                    break;
                default:
                    int[] systemAttrs = {android.R.attr.layout_height};
                    TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
                    int h = a.getDimensionPixelSize(0, 0);
                    a.recycle();
                    mItemHeight = h;
                    break;
            }
        } else {
            if (scale > 1) {
                scale = 1;
            }
            mItemHeight = (int) (mItemWidth * scale);
        }

        LayoutParams lp = new LayoutParams(mItemWidth, mItemHeight);
        addView(mViewPager, lp);

        //top parent of indicators
        mRlBottomBarParent = new RelativeLayout(context);
        addView(mRlBottomBarParent, lp);

        //container of indicators and title
        mLlBottomBar = new LinearLayout(context);
        LayoutParams lp2 = new LayoutParams(mItemWidth, LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mRlBottomBarParent.addView(mLlBottomBar, lp2);

        mLlBottomBar.setBackgroundColor(barColor);
        mLlBottomBar.setPadding((int) barPaddingLeft, (int) barPaddingTop, (int) barPaddingRight,
                (int) barPaddingBottom);
        mLlBottomBar.setClipChildren(false);
        mLlBottomBar.setClipToPadding(false);

        //container of indicators
        mLlIndicatorContainer = new LinearLayout(context);
        mLlIndicatorContainer.setGravity(Gravity.CENTER);
        mLlIndicatorContainer.setVisibility(isIndicatorShow ? VISIBLE : INVISIBLE);
        mLlIndicatorContainer.setClipChildren(false);
        mLlIndicatorContainer.setClipToPadding(false);

        // title
        mTvTitle = new TextView(context);
        mTvTitle.setLayoutParams(
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0F));
        mTvTitle.setSingleLine(true);
        mTvTitle.setTextColor(textColor);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTvTitle.setVisibility(isTitleShow ? VISIBLE : INVISIBLE);

        if (indicatorGravity == Gravity.CENTER) {
            mLlBottomBar.setGravity(Gravity.CENTER);
            mLlBottomBar.addView(mLlIndicatorContainer);
        } else {
            if (indicatorGravity == Gravity.END) {
                mLlBottomBar.setGravity(Gravity.CENTER_VERTICAL);
                mLlBottomBar.addView(mTvTitle);
                mLlBottomBar.addView(mLlIndicatorContainer);

                mTvTitle.setPadding(0, 0, DisplayUtils.dp2px(7), 0);
                mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvTitle.setGravity(Gravity.START);
            } else if (indicatorGravity == Gravity.START) {
                mLlBottomBar.setGravity(Gravity.CENTER_VERTICAL);
                mLlBottomBar.addView(mLlIndicatorContainer);
                mLlBottomBar.addView(mTvTitle);

                mTvTitle.setPadding(DisplayUtils.dp2px(7), 0, 0, 0);
                mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvTitle.setGravity(Gravity.END);
            }
        }
    }

    /**
     * 创建ViewPager的Item布局
     */
    public abstract View onCreateItemView(int position);

    /**
     * 创建显示器
     */
    public abstract View onCreateIndicator();

    /**
     * 设置当前显示器的状态,选中或者未选中
     */
    public abstract void setCurrentIndicator(int position);

    /**
     * 覆写这个方法设置标题
     */
    public void onTitleSelect(TextView tv, int position) {
    }

    /**
     * 设置数据源
     */
    public BaseBanner<E, T> setSource(List<E> list) {
        this.mDatas = list;
        return this;
    }

    /**
     * 滚动延时,默认5秒
     */
    public BaseBanner<E, T> setDelay(long delay) {
        this.mDelay = delay;
        return this;
    }

    /**
     * 滚动间隔,默认5秒
     */
    public BaseBanner<E, T> setPeriod(long period) {
        this.mPeriod = period;
        return this;
    }

    /**
     * 设置是否支持自动滚动,默认true.仅对LoopViewPager有效
     */
    public BaseBanner<E, T> setAutoScrollEnable(boolean isAutoScrollEnable) {
        this.mIsAutoScrollEnable = isAutoScrollEnable;
        return this;
    }

    /**
     * 设置底部背景条颜色,默认透明
     */
    public BaseBanner<E, T> setBarColor(int barColor) {
        mLlBottomBar.setBackgroundColor(barColor);
        return this;
    }

    /**
     * 设置最后一条item是否显示背景条,默认true
     */
    public BaseBanner<E, T> setBarShowWhenLast(boolean isBarShowWhenLast) {
        this.mIsBarShowWhenLast = isBarShowWhenLast;
        return this;
    }

    /**
     * 设置底部背景条padding,单位dp
     */
    public BaseBanner<E, T> barPadding(float left, float top, float right, float bottom) {
        mLlBottomBar.setPadding(DisplayUtils.dp2px(left), DisplayUtils.dp2px(top),
                DisplayUtils.dp2px(right), DisplayUtils.dp2px(bottom));
        return this;
    }

    /**
     * 设置标题颜色,默认"#ffffff"
     */
    public BaseBanner<E, T> setTextColor(int textColor) {
        mTvTitle.setTextColor(textColor);
        return this;
    }

    /**
     * set title text size,unit sp,default 14sp
     */
    public BaseBanner<E, T> setTextSize(float textSize) {
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return this;
    }

    /**
     * 设置是否显示标题,默认true
     */
    public BaseBanner<E, T> setTitleShow(boolean isTitleShow) {
        mTvTitle.setVisibility(isTitleShow ? VISIBLE : INVISIBLE);
        return this;
    }

    /**
     * 设置是否显示显示器,默认true
     */
    public BaseBanner<E, T> setIndicatorShow(boolean isIndicatorShow) {
        mLlIndicatorContainer.setVisibility(isIndicatorShow ? VISIBLE : INVISIBLE);
        return this;
    }

    /**
     * 滚动到下一个item
     */
    private void scrollToNextItem(int position) {
        position++;
        mViewPager.setCurrentItem(position);
    }

    /**
     * 设置viewpager
     */
    private void setViewPager() {
        InnerBannerAdapter mInnerAdapter = new InnerBannerAdapter();
        mViewPager.setAdapter(mInnerAdapter);
        mViewPager.setOffscreenPageLimit(mDatas.size());

        if (mInternalPageListener != null) {
            mViewPager.removeOnPageChangeListener(mInternalPageListener);
        }
        mViewPager.addOnPageChangeListener(mInternalPageListener);
    }

    private ViewPager.OnPageChangeListener mInternalPageListener = new ViewPager
            .OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition = position % mDatas.size();

            setCurrentIndicator(mCurrentPosition);
            onTitleSelect(mTvTitle, mCurrentPosition);
            mLlBottomBar.setVisibility(
                    mCurrentPosition == mDatas.size() - 1 && !mIsBarShowWhenLast ? GONE : VISIBLE);

            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    /**
     * 开始滚动
     */
    public void startScroll() {
        if (mDatas == null) {
            throw new IllegalStateException(
                    "Data source is empty,you must setSource() before startScroll()");
        }

        if (mDatas.size() > 0 && mCurrentPosition > mDatas.size() - 1) {
            mCurrentPosition = 0;
        }

        onTitleSelect(mTvTitle, mCurrentPosition);
        setViewPager();
        //create indicator
        View indicatorViews = onCreateIndicator();
        if (indicatorViews != null) {
            mLlIndicatorContainer.removeAllViews();
            mLlIndicatorContainer.addView(indicatorViews);
        }

        goOnScroll();
    }

    /**
     * 继续滚动(for LoopViewPager)
     */
    public void goOnScroll() {
        if (!isValid()) {
            return;
        }

        if (mIsAutoScrolling) {
            return;
        }
        if (isLoopViewPager() && mIsAutoScrollEnable) {
            pauseScroll();
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                    r -> new Thread(r, "banner-slide-%d"));
            scheduledExecutorService.scheduleAtFixedRate(() -> scrollToNextItem(mCurrentPosition), mDelay, mPeriod, TimeUnit.SECONDS);
            mIsAutoScrolling = true;
        } else {
            mIsAutoScrolling = false;
        }
    }

    /**
     * 停止滚动(for LoopViewPager)
     */
    public void pauseScroll() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
        mIsAutoScrolling = false;
    }

    /**
     * 获取ViewPager对象
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseScroll();
                break;
            case MotionEvent.ACTION_UP:
                goOnScroll();
                break;
            case MotionEvent.ACTION_CANCEL:
                goOnScroll();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private class InnerBannerAdapter
            extends PagerAdapter {
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View inflate = onCreateItemView(position);
            inflate.setOnClickListener(view -> {
                if (mOnItemClickL != null) {
                    mOnItemClickL.onItemClick(position);
                }
            });
            container.addView(inflate);

            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private boolean isLoopViewPager() {
        return mViewPager instanceof LoopViewPager;
    }

    private boolean isValid() {
        if (mViewPager == null) {
            Log.e(TAG, "ViewPager is not exist!");
            return false;
        }

        if (mDatas == null || mDatas.size() == 0) {
            Log.e(TAG, "DataList must be not empty!");
            return false;
        }

        return true;
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    private OnItemClickL mOnItemClickL;

    public void setOnItemClickL(OnItemClickL onItemClickL) {
        this.mOnItemClickL = onItemClickL;
    }

    public interface OnItemClickL {
        void onItemClick(int position);
    }
}