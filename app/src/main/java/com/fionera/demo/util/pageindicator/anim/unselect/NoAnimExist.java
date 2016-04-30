package com.fionera.demo.util.pageindicator.anim.unselect;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.fionera.demo.util.pageindicator.anim.base.IndicatorBaseAnimator;


public class NoAnimExist
        extends IndicatorBaseAnimator {
    public NoAnimExist() {
        this.duration = 200;
    }

    public void setAnimation(View view) {
        animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(view, "alpha", 1, 1)});
    }
}
