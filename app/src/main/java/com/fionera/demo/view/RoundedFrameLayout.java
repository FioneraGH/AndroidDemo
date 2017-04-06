package com.fionera.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.fionera.base.util.DisplayUtils;

/**
 * RoundedFrameLayout
 * Created by fionera on 16-6-26.
 */
public class RoundedFrameLayout
        extends FrameLayout {

    private Paint mShapePaint = null;

    public RoundedFrameLayout(Context context) {
        this(context, null);
    }

    public RoundedFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        initShapePaint();
    }

    private void initShapePaint() {
        mShapePaint = new Paint();
        mShapePaint.setAntiAlias(true);
        mShapePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        try {
            if (canvas != null) {
                drawShapePathCanvas(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawShapePathCanvas(@NonNull Canvas shapeCanvas) {
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) {
            return;
        }
        int count = shapeCanvas.save();
        int count2 = shapeCanvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        super.draw(shapeCanvas);
        shapeCanvas.drawPath(getPath(width, height, DisplayUtils.dp2px(2f)), mShapePaint);
        if (count2 > 0) {
            shapeCanvas.restoreToCount(count2);
        }
        shapeCanvas.restoreToCount(count);
    }

    private Path roundPath;

    private Path getPath(int width, int height, float radius) {
        if (roundPath == null) {
            roundPath = new Path();
            roundPath.addRoundRect(new RectF(0, 0, width, height), radius, radius,
                    Path.Direction.CW);
        }
        return roundPath;
    }
}
