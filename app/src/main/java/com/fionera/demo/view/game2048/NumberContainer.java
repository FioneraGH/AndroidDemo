package com.fionera.demo.view.game2048;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.fionera.demo.util.LogCat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fionera on 16-2-10.
 * Modified by fionera on 17-1-9.
 */
public class NumberContainer
        extends RelativeLayout {

    /**
     * 设置Item的数量n*n；默认为4
     */
    private int mColumn = 4;
    /**
     * 存放所有的Item
     */
    private NumberItem[] numberItems;

    /**
     * Item横向与纵向的边距
     */
    private int mMargin = 10;
    /**
     * 面板的padding
     */
    private int mPadding;
    /**
     * 检测用户滑动的手势
     */
    private GestureDetector mGestureDetector;

    private boolean isMergeHappen = true;
    private boolean isMoveHappen = true;

    /**
     * 记录分数
     */
    private int mScore;

    public interface OnScoreChangeListener {
        void onChange(int score);
    }

    private OnScoreChangeListener onScoreChangeListener;

    public void setOnScoreChangeListener(OnScoreChangeListener onScoreChangeListener) {
        this.onScoreChangeListener = onScoreChangeListener;
    }

    /**
     * 方向
     */
    private enum ACTION {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    public NumberContainer(Context context) {
        this(context, null);
    }

    public NumberContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMargin,
                                                  getResources().getDisplayMetrics());
        /*
          设置Layout的内边距，四边一致，设置为四内边距中的最小值
         */
        mPadding = getPaddingLeft();
        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {

            final int FLING_MIN_DISTANCE = 50;

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                float x = e2.getX() - e1.getX();
                float y = e2.getY() - e1.getY();

                if (x > FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
                    action(ACTION.RIGHT);

                } else if (x < -FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
                    action(ACTION.LEFT);

                } else if (y > FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
                    action(ACTION.DOWN);

                } else if (y < -FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
                    action(ACTION.UP);
                }
                return true;

            }
        });
    }

    private void action(ACTION action) {

        for (int i = 0; i < mColumn; i++) {
            List<NumberItem> row = new ArrayList<>();
            for (int j = 0; j < mColumn; j++) {
                int index = getIndexByAction(action, i, j);
                NumberItem item = numberItems[index];
                if (item.getNumber() != 0) {
                    row.add(item);
                }
            }

            for (int j = 0; j < mColumn && j < row.size(); j++) {
                int index = getIndexByAction(action, i, j);
                NumberItem item = numberItems[index];

                if (item.getNumber() != row.get(j).getNumber()) {
                    isMoveHappen = true;
                }
            }

            mergeItem(row);

            for (int j = 0; j < mColumn; j++) {
                int index = getIndexByAction(action, i, j);
                if (row.size() > j) {
                    numberItems[index].setNumber(row.get(j).getNumber());
                } else {
                    numberItems[index].setNumber(0);
                }
            }

        }
        generateNum();

    }

    private int getIndexByAction(ACTION action, int i, int j) {
        int index = -1;
        switch (action) {
            case LEFT:
                index = i * mColumn + j;
                break;
            case RIGHT:
                index = i * mColumn + mColumn - j - 1;
                break;
            case UP:
                index = i + j * mColumn;
                break;
            case DOWN:
                index = i + (mColumn - 1 - j) * mColumn;
                break;
        }
        return index;
    }

    private void mergeItem(List<NumberItem> row) {
        if (row.size() < 2) {
            return;
        }

        for (int j = 0; j < row.size() - 1; j++) {
            NumberItem item1 = row.get(j);
            NumberItem item2 = row.get(j + 1);

            if (item1.getNumber() == item2.getNumber()) {
                isMergeHappen = true;

                int val = item1.getNumber() + item2.getNumber();
                item1.setNumber(val);

                /*
                  合并总分
                 */
                mScore += val;
                if (onScoreChangeListener != null) {
                    onScoreChangeListener.onChange(mScore);
                }

                for (int k = j + 1; k < row.size() - 1; k++) {
                    row.get(k).setNumber(row.get(k + 1).getNumber());
                }

                row.get(row.size() - 1).setNumber(0);
                return;
            }

        }

    }

    public void generateNum() {

        if (checkOver()) {
            LogCat.e("GAME OVER");
            return;
        }

        if (!isFull()) {
            if (isMoveHappen || isMergeHappen) {
                Random random = new Random();
                int next = random.nextInt(16);
                NumberItem item = numberItems[next];

                while (item.getNumber() != 0) {
                    next = random.nextInt(16);
                    item = numberItems[next];
                }

                item.setNumber(Math.random() > 0.75 ? 4 : 2);

                isMergeHappen = isMoveHappen = false;
            }

        }
    }

    private boolean checkOver() {
        if (!isFull()) {
            return false;
        }

        for (int i = 0; i < mColumn; i++) {
            for (int j = 0; j < mColumn; j++) {

                int index = i * mColumn + j;

                NumberItem item = numberItems[index];

                if ((index + 1) % mColumn != 0) {
                    LogCat.d("RIGHT");
                    NumberItem itemRight = numberItems[index + 1];
                    if (item.getNumber() == itemRight.getNumber()) {
                        return false;
                    }
                }
                if ((index + mColumn) < mColumn * mColumn) {
                    LogCat.d("DOWN");
                    NumberItem itemBottom = numberItems[index + mColumn];
                    if (item.getNumber() == itemBottom.getNumber()) {
                        return false;
                    }
                }
                if (index % mColumn != 0) {
                    LogCat.d("LEFT");
                    NumberItem itemLeft = numberItems[index - 1];
                    if (itemLeft.getNumber() == item.getNumber()) {
                        return false;
                    }
                }
                if (index + 1 > mColumn) {
                    LogCat.d("UP");
                    NumberItem itemTop = numberItems[index - mColumn];
                    if (item.getNumber() == itemTop.getNumber()) {
                        return false;
                    }
                }
            }

        }
        onScoreChangeListener.onChange(-1);
        return true;
    }

    private boolean isFull() {
        for (NumberItem numberItem : numberItems) {
            if (numberItem.getNumber() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测量Layout的宽和高，以及设置Item的宽和高，这里忽略wrap_content 以宽、高之中的最小值绘制正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
          获得正方形的边长
         */
        int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
        /*
          获得Item的宽度
         */
        int childWidth = (length - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;

        if (numberItems == null) {

            numberItems = new NumberItem[mColumn * mColumn];
            for (int i = 0; i < numberItems.length; i++) {

                NumberItem item = new NumberItem(getContext());
                RelativeLayout.LayoutParams lp = new LayoutParams(childWidth, childWidth);

                numberItems[i] = item;
                item.setId(i + 1);

                /*
                  添加子项，使用右下位置flag
                 */
                if ((i + 1) % mColumn != 0) {
                    lp.rightMargin = mMargin;
                }
                if (i % mColumn != 0) {
                    lp.addRule(RelativeLayout.RIGHT_OF, numberItems[i - 1].getId());
                }
                if ((i + 1) > mColumn) {
                    lp.topMargin = mMargin;
                    lp.addRule(RelativeLayout.BELOW, numberItems[i - mColumn].getId());
                }
                addView(item, lp);
            }
            generateNum();
        }

        setMeasuredDimension(length, length);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }
}
