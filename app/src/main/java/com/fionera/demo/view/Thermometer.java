package com.fionera.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.DecimalFormat;

/**
 * Thermometer
 * Created by fionera on 16-6-9.
 */

public class Thermometer
        extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private int temperatureRange = 12;
    private RectF mRange;

    private int mWidth;
    private int mHeight;
    private int centerWidth;
    private int centerHeight;

    private int temperatureAllLong;
    private int mercuryWidth;
    private int maxLineLong;
    private int midLineLong;
    private int minLineLong;
    private int scaleLong;
    private int abHeight;

    private Paint linePaint;
    private Paint textPaint;

    private volatile float mSpeed = 0f;

    private Bitmap mBitmap;

    private float beginTemperature = 30f;
    private float endTemperature = 42f;
    private volatile float currentTemperature = 37f;
    private float targetTemperature = 39f;

    private int everySecondTime = 100;

    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SHIFT, 25,
            getResources().getDisplayMetrics());
    private float mSymbolSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SHIFT, 35,
            getResources().getDisplayMetrics());
    private float mShowSymbolSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SHIFT, 45,
            getResources().getDisplayMetrics());

    private Thread mThread;
    private Thread mChangeTemperatureThread;
    private boolean isRunning;
    private DecimalFormat format;

    public Thermometer(Context context) {
        this(context, null);
    }

    public Thermometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Thermometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() / 2;
        mHeight = getMeasuredHeight() / 2;
        centerWidth = mWidth / 2;
        centerHeight = mHeight / 2;

        mercuryWidth = mWidth / 15;
        minLineLong = mercuryWidth;
        midLineLong = minLineLong * 8 / 5;
        maxLineLong = midLineLong * 3 / 2;

        temperatureAllLong = mHeight * 7 / 10;
        scaleLong = temperatureAllLong / temperatureRange / 10;

        abHeight = mHeight / 12;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);

        textPaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setTextSize(mTextSize);
        linePaint.setShader(null);

        mRange = new RectF(0, 0, mWidth, mHeight);
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if (end - start < everySecondTime) {
                try {
                    Thread.sleep(everySecondTime - (end - start));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                drawBg();
                drawShowHeightAndShow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawBg() {
        mCanvas.drawColor(Color.WHITE);
        float everyTemperatureHeight = (scaleLong) * 10;
        for (int i = 0; i < temperatureRange; i++) {
            mCanvas.drawLine(centerWidth + mercuryWidth / 2,
                    everyTemperatureHeight * i + abHeight * 2,//这里加上两倍的上距离
                    centerWidth + mercuryWidth / 2 + maxLineLong,
                    everyTemperatureHeight * i + abHeight * 2, linePaint);
            mCanvas.drawText(endTemperature - i + "",
                    centerWidth + mercuryWidth / 2 + maxLineLong + minLineLong / 3,
                    everyTemperatureHeight * i + textPaint.getTextSize() / 2 + abHeight * 2,
                    textPaint);
            for (int j = 1; j <= 9; j++) {
                if (j == 5) {
                    mCanvas.drawLine(centerWidth + mercuryWidth / 2,
                            everyTemperatureHeight * i + j * (scaleLong) + abHeight * 2,
                            centerWidth + mercuryWidth / 2 + midLineLong,
                            everyTemperatureHeight * i + j * (scaleLong) + abHeight * 2, linePaint);
                } else {
                    mCanvas.drawLine(centerWidth + mercuryWidth / 2,
                            everyTemperatureHeight * i + j * (scaleLong) + abHeight * 2,
                            centerWidth + mercuryWidth / 2 + minLineLong,
                            everyTemperatureHeight * i + j * (scaleLong) + abHeight * 2, linePaint);
                }

            }
            //画最后一个刻度
            if (i == temperatureRange - 1) {

                mCanvas.drawLine(centerWidth + mercuryWidth / 2,
                        everyTemperatureHeight * (i + 1) + abHeight * 2,//这里加上两倍的上距离
                        centerWidth + mercuryWidth / 2 + maxLineLong,
                        everyTemperatureHeight * (i + 1) + abHeight * 2, linePaint);
                mCanvas.drawText(endTemperature - (i + 1) + "",
                        centerWidth + mercuryWidth / 2 + maxLineLong + minLineLong / 3,
                        everyTemperatureHeight * (i + 1) + textPaint
                                .getTextSize() / 2 + abHeight * 2, textPaint);
            }
        }
        //画左边的刻度
        for (int i = 0; i < temperatureRange; i++) {
            mCanvas.drawLine(centerWidth - mercuryWidth / 2,
                    everyTemperatureHeight * i + abHeight * 2,
                    centerWidth - mercuryWidth / 2 - maxLineLong,
                    everyTemperatureHeight * i + abHeight * 2, linePaint);
            mCanvas.drawText(endTemperature - i + "",
                    centerWidth - (mercuryWidth / 2 + maxLineLong + minLineLong / 3) - textPaint
                            .getTextSize(),
                    everyTemperatureHeight * i + textPaint.getTextSize() / 2 + abHeight * 2,
                    textPaint);
            for (int j = 1; j <= 9; j++) {
                if (j == 5) {
                    mCanvas.drawLine(centerWidth - mercuryWidth / 2,
                            everyTemperatureHeight * i + j * (scaleLong) + abHeight * 2,
                            centerWidth - mercuryWidth / 2 - midLineLong,
                            everyTemperatureHeight * i + j * (scaleLong) + abHeight * 2, linePaint);
                } else {
                    mCanvas.drawLine(centerWidth - mercuryWidth / 2,
                            everyTemperatureHeight * i + j * (scaleLong) + abHeight * 2,
                            centerWidth - mercuryWidth / 2 - minLineLong,
                            everyTemperatureHeight * i + j * (scaleLong) + abHeight * 2, linePaint);
                }

            }
            //画最后一个刻度
            if (i == temperatureRange - 1) {
                mCanvas.drawLine(centerWidth - mercuryWidth / 2,
                        everyTemperatureHeight * (i + 1) + abHeight * 2,
                        centerWidth - mercuryWidth / 2 - maxLineLong,
                        everyTemperatureHeight * (i + 1) + abHeight * 2, linePaint);
                mCanvas.drawText(endTemperature - (i + 1) + "",
                        centerWidth - (mercuryWidth / 2 + maxLineLong + minLineLong / 3) - textPaint
                                .getTextSize(), everyTemperatureHeight * (i + 1) + textPaint
                                .getTextSize() / 2 + abHeight * 2, textPaint);
            }
        }
        //画红色的园
        Paint CirclePaint = new Paint();
        CirclePaint.setStyle(Paint.Style.FILL);
        //        CirclePaint.setColor(getResources().getColor(R.color.theme_color));
        mCanvas.drawCircle(centerWidth,
                everyTemperatureHeight * (temperatureRange) + abHeight * 2 + mercuryWidth,
                mercuryWidth * 3 / 2, CirclePaint);
        //画摄氏度的符号
        Paint symbolTextPaint = new Paint();
        symbolTextPaint.setColor(Color.BLACK);
        symbolTextPaint.setTextSize(mSymbolSize);
        symbolTextPaint.setShader(null);
        mCanvas.drawText("℃", centerWidth - maxLineLong / 2 - mercuryWidth / 2 - symbolTextPaint
                .getTextSize() / 2, abHeight * 2 - symbolTextPaint.getTextSize(), symbolTextPaint);
        mCanvas.drawText("℃", centerWidth + maxLineLong / 2 + mercuryWidth / 2 - symbolTextPaint
                .getTextSize() / 2, abHeight * 2 - symbolTextPaint.getTextSize(), symbolTextPaint);

        //绘制显示数字的符号和虚线
        Paint ShowsymbolTextPaint = new Paint();
        ShowsymbolTextPaint.setColor(Color.BLACK);
        ShowsymbolTextPaint.setTextSize(mShowSymbolSize);
        ShowsymbolTextPaint.setShader(null);
        mCanvas.drawText("℃", mWidth * 3 / 2,
                temperatureAllLong / 2 - ShowsymbolTextPaint.getTextSize(), ShowsymbolTextPaint);
        mCanvas.drawText("- - - - - - - -", mWidth + ShowsymbolTextPaint.getTextSize() * 3,
                temperatureAllLong / 2, ShowsymbolTextPaint);
        mCanvas.drawText("℃", mWidth * 3 / 2,
                temperatureAllLong / 2 + ShowsymbolTextPaint.getTextSize(), ShowsymbolTextPaint);
    }

    private void drawShowHeightAndShow() {

        //这里控制水银的上升速度
        float difference = Math.abs(targetTemperature - currentTemperature);
        /*
          //这里定义一个boolean来控制是使用加法还是减法，其中true表示当前温度小于
          目标温度，要使用加法，false表示当前温度大于目标温度，要使用减法。
         */
        boolean addORsub = currentTemperature < targetTemperature;
        if (difference == 0 || difference <= 0.005) {
            mSpeed = 0;
            currentTemperature = targetTemperature;
        } else {
            if (difference > 2) {
                mSpeed = (float) 0.03;
            } else {
                if (difference > 1) {
                    mSpeed = (float) 0.025;
                } else {
                    if (difference > 0.5) {
                        mSpeed = (float) 0.015;
                    } else {
                        if (difference > 0.3) {
                            mSpeed = (float) 0.012;
                        } else {
                            if (difference > 0.2) {
                                mSpeed = (float) 0.009;
                            } else {
                                mSpeed = (float) 0.008;
                            }

                        }
                    }
                }
            }
        }
        if (addORsub) {
            currentTemperature += 20 * mSpeed;
        } else {
            currentTemperature -= 20 * mSpeed;
        }

        //

        Paint RectPaint = new Paint();
        RectPaint.setStyle(Paint.Style.FILL);
        //        RectPaint.setColor(getResources().getColor(R.color.theme_color));
        //        这里主要是对温度的显示，画矩形的过程中，唯一改变的就是Top这一个值了
        if (Math.abs(currentTemperature - targetTemperature) > 0.32) {
            mCanvas.drawRect(centerWidth - mercuryWidth / 2,
                    (scaleLong) * 10 * (temperatureRange) + abHeight * 2 - (currentTemperature -
                            beginTemperature) * 10 * scaleLong,
                    centerWidth + mercuryWidth / 2,
                    (scaleLong) * 10 * (temperatureRange) + abHeight * 2, RectPaint);
        } else {
            mCanvas.drawRect(centerWidth - mercuryWidth / 2,
                    (scaleLong) * 10 * (temperatureRange) + abHeight * 2 - (targetTemperature - beginTemperature) * 10 * scaleLong,
                    centerWidth + mercuryWidth / 2,
                    (scaleLong) * 10 * (temperatureRange) + abHeight * 2, RectPaint);
        }

        //这里开始画显示的数字
        Paint ShowNumberTextPaint = new Paint();
        ShowNumberTextPaint.setColor(Color.BLACK);
        ShowNumberTextPaint.setTextSize(mShowSymbolSize);
        ShowNumberTextPaint.setShader(null);
        format = new DecimalFormat("##0.0");
        float display = Float.parseFloat(format.format(trueTemperature));
        mCanvas.drawText(display + "", mWidth * 3 / 2 - ShowNumberTextPaint.getTextSize() * 2,
                temperatureAllLong / 2 - ShowNumberTextPaint.getTextSize(), ShowNumberTextPaint);
        mCanvas.drawText(display + "", mWidth * 3 / 2 - ShowNumberTextPaint.getTextSize() * 2,
                temperatureAllLong / 2 + ShowNumberTextPaint.getTextSize(), ShowNumberTextPaint);
    }

    private float trueTemperature = 35f;

    public void setTargetTemperature(float targetTemperature) {
        trueTemperature = targetTemperature;
        if (targetTemperature < 30) {
            this.targetTemperature = 30;
        }
        if (targetTemperature > endTemperature) {
            this.targetTemperature = endTemperature;
        }
        this.targetTemperature = targetTemperature;
    }
}
