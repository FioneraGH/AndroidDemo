package com.fionera.demo.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by fionera on 15-12-25.
 */
public class TopCropImageView extends ImageView {

    public TopCropImageView(Context context) {
        this(context, null);
    }

    public TopCropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopCropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        if (getDrawable() == null) {
            return super.setFrame(l, t, r, b);
        }
        Matrix matrix = getImageMatrix();
        float scaleWidth = getWidth() / (float) getDrawable().getIntrinsicWidth();
        float scaleHeight = getHeight() / (float) getDrawable().getIntrinsicHeight();
        float scaleFactor = (scaleWidth > scaleHeight) ? scaleWidth : scaleHeight;
        matrix.setScale(scaleFactor, scaleFactor, 0, 0);
        if (scaleFactor == scaleHeight) {
            float translateX = ((getDrawable().getIntrinsicWidth() * scaleFactor) - getWidth()) / 2;
            matrix.postTranslate(-translateX, 0);
        }
        setImageMatrix(matrix);
        return super.setFrame(l, t, r, b);
    }
}