package com.fionera.demo.util.pageindicator.anim.select;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.fionera.demo.util.pageindicator.anim.base.IndicatorBaseAnimator;


public class ZoomInEnter
        extends IndicatorBaseAnimator {
    public ZoomInEnter() {
        this.duration = 200;
    }

    public void setAnimation(View view) {
        animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(view, "scaleX",
                                                                       new float[]{1.0F, 1.5F}),
                ObjectAnimator.ofFloat(
                view, "scaleY", new float[]{1.0F, 1.5F})});
    }
}
