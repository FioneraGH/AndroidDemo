package com.fionera.demo.util.pageindicator.anim.select;

import android.animation.ObjectAnimator;
import android.support.annotation.Keep;
import android.view.View;

import com.fionera.demo.util.pageindicator.anim.base.IndicatorBaseAnimator;

public class ZoomInEnter
        extends IndicatorBaseAnimator {
    public ZoomInEnter() {
        this.duration = 200;
    }

    public void setAnimation(View view) {
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", 1.0F, 1.5F),
                ObjectAnimator.ofFloat(
                view, "scaleY", 1.0F, 1.5F));
    }
}
