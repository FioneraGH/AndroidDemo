package com.fionera.wechatdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by fionera on 15-11-26.
 */
public class ScrollText extends SurfaceView {

    private static final String LOG_TAG = "ScrollTextView";
    private SurfaceHolder surfaceHolder;
    private DrawWorker drawWorker;

    private int viewWidth;
    private int viewHeight;

    private int backgroundColor = Color.argb(0xff, 0, 0, 0);
    private int fontColor = Color.WHITE;
    private String text = "";
    private float textWidth = 0;
    private float textHeight = 0;

    private Paint backgroundPaint;
    private TextPaint textPaint;

    public enum ScrollMode {LEFT_TO_RIGHT, RIGHT_TO_LEFT}

    private ScrollMode scrollMode = ScrollMode.LEFT_TO_RIGHT;
    private float scrollSpeed = 1;

    public ScrollText(Context context) {
        super(context);
        init();
    }

    public ScrollText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new MySurfaceCallback());

        backgroundPaint = new Paint();

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    // 设置背景色
    public void setBackgroundColor(int color) {
        backgroundColor = color;
    }

    // 设置文本
    public void setText(String text) {
        this.text = text;
        textWidth = measureTextWidth(text, textPaint);
        textHeight = measureTextHeight(textPaint);
    }

    // 获得文本
    public String getText() {
        return text;
    }

    // 设置字体颜色
    public void setFontColor(int color) {
        fontColor = color;
    }

    // 设置字体大小
    public void setFontSize(float size) {
        textPaint.setTextSize(size);
        textWidth = measureTextWidth(text, textPaint);
        textHeight = measureTextHeight(textPaint);
    }

    // 设置滚动模式
    public void setScrollMode(ScrollMode mode) {
        scrollMode = mode;
    }

    // 设置滚动速度,即绘制循环每一轮绘制文本的位移像素数.(每一轮大概要16ms)
    public void setScrollSpeed(float speed) {
        scrollSpeed = speed;
    }

    // 度量文本宽度
    private float measureTextWidth(String text, Paint paint) {
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        float temp = 0;
        for (float width : widths) {
            temp += width;
        }
        return temp;
    }

    // 度量文本高度
    private float measureTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    // 实现SurfaceHolder.Callback接口，用于监视surface状态
    private class MySurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawWorker = new DrawWorker();
            drawWorker.work();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            viewWidth = width;
            viewHeight = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            drawWorker.finish();
        }
    }

    // 绘制工人
    private class DrawWorker extends Thread {

        float x = 0;
        boolean canRun;

        public DrawWorker() {
            super();
            canRun = true;
        }

        @Override
        public synchronized void start() {
            work();
        }

        public void work() {
            super.start();
            canRun = true;
        }

        // 更新x坐标值
        private void updateX() {
            switch (scrollMode) {
                case LEFT_TO_RIGHT:
                    x += scrollSpeed;
                    if (x > viewWidth) {
                        x = -textWidth;
                    }
                    break;
                case RIGHT_TO_LEFT:
                    x -= scrollSpeed;
                    if (x < -textWidth) {
                        x = viewWidth;
                    }
                    break;
            }
        }

        // 计算基线（baseline）位置
        // 由于drawText方法第三个参数y指的是基线位置，所以需要使用这个方法将文本左上角坐标的Y值转换一下
        private float calculateBaseline(float y) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            return y + (-fontMetrics.ascent);
        }

        @Override
        public void run() {
            Canvas canvas;
            // 绘制循环,每一轮的执行时间大概是16ms
            for (; ; ) {
                // lockCanvas效率很低,执行它大概要用13ms
                //                long startTime = System.currentTimeMillis();
                canvas = surfaceHolder.lockCanvas();
                //                Log.e("lockCanvas Time", System.currentTimeMillis() - startTime
                // + "");
                if (canvas != null) {
                    canvas.drawColor(backgroundColor);

                    textPaint.setColor(fontColor);
                    float baseline = calculateBaseline((viewHeight - textHeight) / 2);
                    if (textWidth <= viewWidth) {
                        canvas.drawText(text, (viewWidth - textWidth) / 2, baseline, textPaint);
                    } else {
                        canvas.drawText(text, x, baseline, textPaint);
                        updateX();
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

                if (!canRun) break;
                try {
                    sleep(2);
                } catch (InterruptedException e) {
                    Log.e(LOG_TAG, Log.getStackTraceString(e));
                }
                //                Log.e("lockCanvas Time", System.currentTimeMillis() - startTime
                // + "");
            }
        }

        public void finish() {
            canRun = false;
        }
    }

}
