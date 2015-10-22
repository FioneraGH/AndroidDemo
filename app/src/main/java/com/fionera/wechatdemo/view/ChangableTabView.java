package com.fionera.wechatdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.fionera.wechatdemo.R;

/**
 * Created by fionera on 15-10-22.
 */
public class ChangableTabView extends View {

    private Bitmap bitmap;
    private int color = 0x0066ff;
    private String text = "";
    private int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
            getResources().getDisplayMetrics());

    public ChangableTabView(Context context) {
        this(context, null);
    }

    public ChangableTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangableTabView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /**
         * 获取自定义属性
         */
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ChangableTabView);
        int n = attr.getIndexCount();
        for (int i = 0; i < n; i++) {
            int a = attr.getIndex(i);
            switch (a) {
                case R.styleable.ChangableTabView_ctab_icon:
                    bitmap = ((BitmapDrawable) attr.getDrawable(a)).getBitmap();
                    break;
                case R.styleable.ChangableTabView_ctab_color:
                    color = attr.getColor(a, 0x0066ff);
                    break;
                case R.styleable.ChangableTabView_ctab_text:
                    text = attr.getString(a);
                    break;
                case R.styleable.ChangableTabView_ctab_size:
                    size = (int) attr.getDimension(a,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                                    getResources().getDisplayMetrics()));
                    break;
            }
        }
        attr.recycle();

        /**
         * 初始化文字获取文字相应Rect
         */
        textRect = new Rect();
        textPaint = new Paint();
        textPaint.setTextSize(size);
        textPaint.setColor(0x66666);
        textPaint.getTextBounds(text, 0, text.length(), textRect);

        alpha = 1.0f;
    }

    private Bitmap bg;
    private Paint bgPaint;
    private Canvas bgCanvas;
    private Paint textPaint;
    private Rect iconRect;
    private Rect textRect;
    private float alpha;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 根据文字Rect计算图标Rect
         */
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - textRect.height());
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = getMeasuredHeight() / 2 - iconWidth / 2 - textRect.height() / 2;

        iconRect = new Rect(left, top, iconWidth + left, iconWidth + top);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /**
         * 绘制图标
         */
        canvas.drawBitmap(bitmap, null, iconRect, null);
        int tempAlpha = (int) Math.ceil(255 * alpha);
        setupTargetBitmap(tempAlpha);

        drawSourceText(canvas);
        drawTargetText(canvas, tempAlpha);

        /**
         * 绘制处理完后的背景
         */
        canvas.drawBitmap(bg, 0, 0, null);
    }

    private void drawSourceText(Canvas canvas) {

        textPaint.setColor(0xeeeeee);
        canvas.drawText(text, getMeasuredWidth() / 2 - textRect.width() / 2,
                iconRect.bottom + textRect.height(), textPaint);
    }

    private void drawTargetText(Canvas canvas, int alpha) {

        textPaint.setColor(0xeeeeee);
        textPaint.setAlpha(alpha);
        canvas.drawText(text, getMeasuredWidth() / 2 - textRect.width() / 2,
                iconRect.bottom + textRect.height(), textPaint);
    }

    /**
     * ！！！！！！！！！！！！！！！！！
     * 在内存中准备纯色背景图并采用xfmode处理
     *
     * @param alpha
     */
    private void setupTargetBitmap(int alpha) {

        /**
         * 先创建一个等大小的空bitmap
         */
        bg = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        bgCanvas = new Canvas(bg);
        bgPaint = new Paint();

        /**
         * 设置背景画笔的属性
         */
        bgPaint.setColor(color);
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);
        bgPaint.setAlpha(alpha);

        /**
         * 使用画笔并根据xfmode绘制bitmap
         */
        bgCanvas.drawRect(iconRect, bgPaint);
        bgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        bgPaint.setAlpha(255);
        bgCanvas.drawBitmap(bitmap, null, iconRect, bgPaint);
    }
}
