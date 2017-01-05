package com.fionera.demo.util.pageindicator.anim.select;

import android.animation.ObjectAnimator;
import android.view.View;

import com.fionera.demo.util.pageindicator.anim.base.IndicatorBaseAnimator;

public class RotateEnter
        extends IndicatorBaseAnimator {
    public RotateEnter() {
        this.duration = 250;
    }

    public void setAnimation(View view) {
        animatorSet
                .playTogether(ObjectAnimator.ofFloat(view, "rotation", 0, 180));
    }
}
