package com.fionera.demo.view.game2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fionera on 16-2-10.
 */
public class NumberItem
        extends View {
    /**
     * 该View上的数字
     */
    private int number;
    private String numberDraw;
    private Paint paint;
    /**
     * 绘制文字的区域
     */
    private Rect rect;

    public NumberItem(Context context) {
        this(context, null);
    }

    public NumberItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();

    }

    public void setNumber(int number) {
        this.number = number;
        numberDraw = String.valueOf(number);
        paint.setTextSize(30.0f);
        rect = new Rect();
        paint.getTextBounds(numberDraw, 0, numberDraw.length(), rect);
        invalidate();
    }


    public int getNumber() {
        return number;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        String mBgColor;
        switch (number) {
            case 0:
                mBgColor = "#CCC0B3";
                break;
            case 2:
                mBgColor = "#EEE4DA";
                break;
            case 4:
                mBgColor = "#EDE0C8";
                break;
            case 8:
                mBgColor = "#F2B179";
                break;
            case 16:
                mBgColor = "#F49563";
                break;
            case 32:
                mBgColor = "#F5794D";
                break;
            case 64:
                mBgColor = "#F55D37";
                break;
            case 128:
                mBgColor = "#EEE863";
                break;
            case 256:
                mBgColor = "#EDB04D";
                break;
            case 512:
                mBgColor = "#ECB04D";
                break;
            case 1024:
                mBgColor = "#EB9437";
                break;
            case 2048:
                mBgColor = "#EA7821";
                break;
            default:
                mBgColor = "#EA7821";
                break;
        }

        paint.setColor(Color.parseColor(mBgColor));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        if (number != 0) {
            drawText(canvas);
        }

    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        paint.setColor(Color.BLACK);
        float x = (getWidth() - rect.width()) / 2;
        float y = getHeight() / 2 + rect.height() / 2;
        canvas.drawText(numberDraw, x, y, paint);
    }

}