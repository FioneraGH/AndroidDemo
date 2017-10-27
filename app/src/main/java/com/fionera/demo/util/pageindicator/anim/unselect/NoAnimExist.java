package com.fionera.demo.util.pageindicator.anim.unselect;

import android.animation.ObjectAnimator;
import android.view.View;

import com.fionera.demo.util.pageindicator.anim.base.BaseIndicatorAnimator;

/**
 * @author fionera
 */
public class NoAnimExist
        extends BaseIndicatorAnimator {
    public NoAnimExist() {
        this.duration = 200;
    }

    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "alpha", 1, 1));
    }
}
