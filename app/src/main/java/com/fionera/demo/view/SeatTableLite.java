package com.fionera.demo.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.fionera.base.util.DisplayUtils;
import com.fionera.demo.R;
import com.fionera.base.util.LogCat;

import java.util.ArrayList;
import java.util.Collections;

/**
 * SeatTableLite
 * Created by fionera on 17-2-13 in AndroidDemo.
 */
@SuppressWarnings("unused")
public class SeatTableLite
        extends View {
    private final static float DEFAULT_BOX_WIDTH = 60;
    private final static float DEFAULT_BOX_HEIGHT = 59;
    public static final int DURATION = 250;

    Paint paint = new Paint();

    Matrix matrix = new Matrix();

    /**
     * 方格水平间距
     */
    int spacing;

    /**
     * 方格垂直间距
     */
    int verSpacing;

    int halfSpacing;

    /**
     * 行数
     */
    int row;

    /**
     * 列数
     */
    int column;

    /**
     * 可选时方格的图片
     */
    Bitmap seatBitmap;

    /**
     * 选中时方格的图片
     */
    Bitmap checkedSeatBitmap;

    /**
     * 方格已经不可用时的图片
     */
    Bitmap boxInvalidBitmap;

    /**
     * 触摸位置
     */
    int lastX;
    int lastY;

    /**
     * 整个方格图的宽度
     */
    int seatBitmapWidth;

    /**
     * 整个方格图的高度
     */
    int seatBitmapHeight;

    /**
     * 方格图片的宽度
     */
    private int seatWidth;

    /**
     * 方格图片的高度
     */
    private int seatHeight;

    /**
     * 最多可以选择的方格数量
     */
    int maxSelected = Integer.MAX_VALUE;

    /**
     * 待绘制方格资源
     */
    int boxCheckedResID;
    int boxInvalidResID;
    int boxAvailableResID;

    boolean isOnClick;

    /**
     * 方格已选
     */
    private static final int SEAT_TYPE_INVALID = 1;

    /**
     * 方格已经选中
     */
    private static final int SEAT_TYPE_SELECTED = 2;

    /**
     * 方格可选
     */
    private static final int SEAT_TYPE_AVAILABLE = 3;

    /**
     * 方格不可用
     */
    private static final int SEAT_TYPE_NOT_AVAILABLE = 4;

    private int downX, downY;
    private boolean pointer;

    public SeatTableLite(Context context) {
        this(context, null);
    }

    public SeatTableLite(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeatTableLite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeatTableView);
        boxCheckedResID = typedArray.getResourceId(R.styleable.SeatTableView_seat_checked,
                R.drawable.ic_seat_green);
        boxInvalidResID = typedArray.getResourceId(R.styleable.SeatTableView_overview_sold,
                R.drawable.ic_seat_sold);
        boxAvailableResID = typedArray.getResourceId(R.styleable.SeatTableView_seat_available,
                R.drawable.ic_seat_gray);
        typedArray.recycle();

        mFlingDetector = new GestureDetector(context, mSimpleOnGestureListener);
    }

    float xScale1 = 1;
    float yScale1 = 1;

    private void init() {
        spacing = DisplayUtils.dp2px(10);
        verSpacing = DisplayUtils.dp2px(10);
        halfSpacing = spacing / 2;

        seatBitmap = BitmapFactory.decodeResource(getResources(), boxAvailableResID);
        checkedSeatBitmap = BitmapFactory.decodeResource(getResources(), boxCheckedResID);
        boxInvalidBitmap = BitmapFactory.decodeResource(getResources(), boxInvalidResID);

        float scaleX = DEFAULT_BOX_WIDTH / seatBitmap.getWidth();
        float scaleY = DEFAULT_BOX_HEIGHT / seatBitmap.getHeight();
        xScale1 = scaleX;
        yScale1 = scaleY;

        seatWidth = (int) (seatBitmap.getWidth() * xScale1);
        seatHeight = (int) (seatBitmap.getHeight() * yScale1);

        seatBitmapWidth = column * seatWidth + (column - 1) * spacing;
        seatBitmapHeight = row * seatHeight + (row - 1) * verSpacing;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        matrix.postTranslate(spacing, verSpacing);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (row <= 0 || column <= 0) {
            return;
        }

        drawSeat(canvas);
    }

    private Matrix tempMatrix = new Matrix();

    private void drawSeat(Canvas canvas) {
        float translateX = getTranslateX();
        float translateY = getTranslateY();
        float scaleX = getMatrixScaleX();
        float scaleY = getMatrixScaleX();

        for (int i = 0; i < row; i++) {
            float top = i * seatBitmap.getHeight() * yScale1 * scaleY + i * verSpacing * scaleY + translateY;

            float bottom = top + seatBitmap.getHeight() * yScale1 * scaleY;
            if (bottom < 0 || top > getHeight()) {
                continue;
            }

            for (int j = 0; j < column; j++) {
                float left = j * seatBitmap.getWidth() * xScale1 * scaleX + j * spacing * scaleX + translateX;

                float right = (left + seatBitmap.getWidth() * xScale1 * scaleY);
                if (right < 0 || left > getWidth()) {
                    continue;
                }

                tempMatrix.setTranslate(left, top);
                tempMatrix.postScale(xScale1, yScale1, left, top);
                tempMatrix.postScale(scaleX, scaleY, left, top);

                canvas.drawLine(left - halfSpacing, top - halfSpacing, right + halfSpacing,
                        top - halfSpacing, paint);
                canvas.drawLine(left - halfSpacing, top - halfSpacing, left - halfSpacing,
                        bottom + halfSpacing, paint);
                canvas.drawLine(right + halfSpacing, top - halfSpacing, right + halfSpacing,
                        bottom + halfSpacing, paint);
                if(i == row - 1){
                    canvas.drawLine(left - halfSpacing, bottom + halfSpacing, right + halfSpacing,
                            bottom + halfSpacing, paint);
                }

                switch (getSeatType(i, j)) {
                    case SEAT_TYPE_AVAILABLE:
                        canvas.drawBitmap(seatBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_NOT_AVAILABLE:
                        canvas.drawBitmap(boxInvalidBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_SELECTED:
                        canvas.drawBitmap(checkedSeatBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_INVALID:
                        canvas.drawBitmap(boxInvalidBitmap, tempMatrix, paint);
                        break;
                }
            }
        }
    }

    private int getSeatType(int row, int column) {
        if (isHave(getID(row, column)) >= 0) {
            return SEAT_TYPE_SELECTED;
        }

        if (seatChecker != null) {
            if (!seatChecker.isValidSeat(row, column)) {
                return SEAT_TYPE_NOT_AVAILABLE;
            } else if (seatChecker.isSold(row, column)) {
                return SEAT_TYPE_INVALID;
            }
        }

        return SEAT_TYPE_AVAILABLE;
    }

    private int getID(int row, int column) {
        return row * this.column + (column + 1);
    }

    private float velocityX;
    private float velocityY;
    private GestureDetector mFlingDetector;
    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            isOnClick = true;
            int x = (int) e.getX();
            int y = (int) e.getY();

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    int tempX = (int) ((j * seatWidth + j * spacing) * getMatrixScaleX()
                            + getTranslateX());
                    int maxTemX = (int) (tempX + seatWidth * getMatrixScaleX());

                    int tempY = (int) ((i * seatHeight + i * verSpacing) *
                            getMatrixScaleY() + getTranslateY());
                    int maxTempY = (int) (tempY + seatHeight * getMatrixScaleY());

                    if (seatChecker != null && seatChecker.isValidSeat(i, j) && !seatChecker
                            .isSold(i, j)) {
                        if (x >= tempX && x <= maxTemX && y >= tempY && y <= maxTempY) {
                            int id = getID(i, j);
                            int index = isHave(id);
                            if (index >= 0) {
                                remove(index);
                                if (seatChecker != null) {
                                    seatChecker.unCheck(i, j);
                                }
                            } else {
                                if (selects.size() >= maxSelected) {
                                    Toast.makeText(getContext(),
                                            "最多只能选择" + maxSelected + "个",
                                            Toast.LENGTH_SHORT).show();
                                    return super.onSingleTapConfirmed(e);
                                } else {
                                    addChooseSeat(i, j);
                                    if (seatChecker != null) {
                                        seatChecker.checked(i, j);
                                    }
                                }
                            }

                            invalidate();
                            break;
                        }
                    }
                }
            }

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            SeatTableLite.this.velocityX = velocityX;
            SeatTableLite.this.velocityY = velocityY;
            return true;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        super.onTouchEvent(event);

        mFlingDetector.onTouchEvent(event);

        int pointerCount = event.getPointerCount();
        if (pointerCount > 1) {
            pointer = true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                velocityX = 0;
                velocityY = 0;

                pointer = false;
                downX = x;
                downY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                int downDX = Math.abs(x - downX);
                int downDY = Math.abs(y - downY);
                if ((downDX > 10 || downDY > 10) && !pointer) {
                    int dx = x - lastX;
                    int dy = y - lastY;
                    matrix.postTranslate(dx, dy);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                autoScroll(velocityX / 10, velocityY / 10);
                break;
        }
        lastX = x;
        lastY = y;

        return true;
    }

    private void autoScroll(float vX, float vY) {
        float currentSeatBitmapWidth = seatBitmapWidth * getMatrixScaleX();
        float currentSeatBitmapHeight = seatBitmapHeight * getMatrixScaleY();
        float moveXLength = 0;
        float moveYLength;

        //处理左右滑动的情况
        if (currentSeatBitmapWidth < getWidth()) {
            if (getTranslateX() < 0 || getMatrixScaleX() < spacing) {
                //计算要移动的距离
                if (getTranslateX() < 0) {
                    moveXLength = (-getTranslateX()) + spacing;
                } else {
                    moveXLength = spacing - getTranslateX();
                }
                LogCat.d("deal with h1:" + getTranslateX() + " " + currentSeatBitmapWidth +
                        " " + getWidth());
            }
        } else {
            if (getTranslateX() < 0 && getTranslateX() + currentSeatBitmapWidth > getWidth()) {
                LogCat.d("no need to deal with h:" + getTranslateX() + " " + currentSeatBitmapWidth +
                        " " + getWidth() + " " + getScrollX());
                /*
                adjust outline to avoid recheck over scroll
                 */
                if (vX + getTranslateX() + currentSeatBitmapWidth < getWidth()) {
                    vX = getWidth() - currentSeatBitmapWidth - getTranslateX();
                } else if (vX + getTranslateX() > 0) {
                    vX = -getTranslateX();
                }
                moveXLength = vX;
            } else {
                //往左侧滑动
                if (getTranslateX() + currentSeatBitmapWidth < getWidth()) {
                    moveXLength = getWidth() - (getTranslateX() + currentSeatBitmapWidth);
                } else {
                    //右侧滑动
                    moveXLength = -getTranslateX() + spacing;
                }
                LogCat.d("deal with h2:" + getTranslateX() + " " + currentSeatBitmapWidth +
                        " " + getWidth());
            }
        }

        float startYPosition = verSpacing * getMatrixScaleY();

        //处理上下滑动
        if (currentSeatBitmapHeight < getHeight()) {
            if (getTranslateY() < startYPosition) {
                moveYLength = startYPosition - getTranslateY();
            } else {
                moveYLength = -(getTranslateY() - (startYPosition));
            }
            LogCat.d("deal with v1:" + getTranslateY() + " " + currentSeatBitmapHeight +
                    " " + getHeight());
        } else {
            if (getTranslateY() < 0 && getTranslateY() + currentSeatBitmapHeight > getHeight()) {
                LogCat.d("no need to deal with v:" + getTranslateY() + " " + currentSeatBitmapHeight +
                        " " + getHeight());
                /*
                adjust outline to avoid recheck over scroll
                 */
                if (vY + getTranslateY() + currentSeatBitmapHeight < getWidth()) {
                    vY = getHeight() - currentSeatBitmapHeight - getTranslateY();
                } else if (vY + getTranslateY() > 0) {
                    vY = -getTranslateY();
                }
                moveYLength = vY;
            } else {
                //往上滑动
                if (getTranslateY() + currentSeatBitmapHeight < getHeight()) {
                    moveYLength = getHeight() - (getTranslateY() + currentSeatBitmapHeight);
                } else {
                    moveYLength = -(getTranslateY() - (startYPosition));
                }
                LogCat.d("deal with v2:" + getTranslateY() + " " + currentSeatBitmapHeight +
                        " " + getHeight());
            }
        }

        Point start = new Point();
        start.x = (int) getTranslateX();
        start.y = (int) getTranslateY();

        Point end = new Point();
        end.x = (int) (start.x + moveXLength);
        end.y = (int) (start.y + moveYLength);

        moveAnimate(start, end);
    }

    private ArrayList<Integer> selects = new ArrayList<>();

    public ArrayList<String> getSelectedSeat() {
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                if (isHave(getID(i, j)) >= 0) {
                    results.add(i + "," + j);
                }
            }
        }
        return results;
    }

    private int isHave(Integer seat) {
        return Collections.binarySearch(selects, seat);
    }

    private void remove(int index) {
        selects.remove(index);
    }

    float[] m = new float[9];

    private float getTranslateX() {
        matrix.getValues(m);
        return m[Matrix.MTRANS_X];
    }

    private float getTranslateY() {
        matrix.getValues(m);
        return m[Matrix.MTRANS_Y];
    }

    private float getMatrixScaleX() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_X];
    }

    private float getMatrixScaleY() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_Y];
    }

    private void moveAnimate(Point start, Point end) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(mMoveEvaluator, start, end);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new MoveAnimation());
        valueAnimator.setDuration(DURATION);
        valueAnimator.start();
    }

    private MoveEvaluator mMoveEvaluator = new MoveEvaluator();

    private class MoveEvaluator
            implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
            int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
            return new Point(x, y);
        }
    }

    private class MoveAnimation
            implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Point p = (Point) animation.getAnimatedValue();
            move(p);
        }
    }

    private void move(Point p) {
        float x = p.x - getTranslateX();
        float y = p.y - getTranslateY();
        matrix.postTranslate(x, y);
        invalidate();
    }

    private void addChooseSeat(int row, int column) {
        int id = getID(row, column);
        for (int i = 0; i < selects.size(); i++) {
            int item = selects.get(i);
            if (id < item) {
                selects.add(i, id);
                return;
            }
        }

        selects.add(id);
    }

    public interface SeatChecker {
        /**
         * 是否可用方格
         *
         * @param row    row
         * @param column column
         * @return boolean
         */
        boolean isValidSeat(int row, int column);

        /**
         * 是否已选
         *
         * @param row    row
         * @param column column
         * @return return
         */
        boolean isSold(int row, int column);

        void checked(int row, int column);

        void unCheck(int row, int column);

        /**
         * 获取选中后方格上显示的文字
         *
         * @param row    row
         * @param column column
         * @return 返回2个元素的数组, 第一个元素是第一行的文字, 第二个元素是第二行文字, 如果只返回一个元素则会绘制到方格图的中间位置
         */
        String[] checkedSeatTxt(int row, int column);

    }

    private SeatChecker seatChecker;

    public void setMaxSelected(int maxSelected) {
        this.maxSelected = maxSelected;
    }

    public void setSeatChecker(SeatChecker seatChecker) {
        this.seatChecker = seatChecker;
        invalidate();
    }

    public void setData(int row, int column) {
        this.row = row;
        this.column = column;
        init();
        invalidate();
    }

    private int getRowNumber(int row) {
        int result = row;
        if (seatChecker == null) {
            return -1;
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (seatChecker.isValidSeat(i, j)) {
                    break;
                }

                if (j == column - 1) {
                    result--;
                }
            }
        }
        return result;
    }

    private int getColumnNumber(int row, int column) {
        int result = column;
        if (seatChecker == null) {
            return -1;
        }

        for (int i = row; i <= row; i++) {
            for (int j = 0; j < column; j++) {
                if (!seatChecker.isValidSeat(i, j)) {
                    result--;
                }
            }
        }
        return result;
    }
}

