package com.fionera.demo.util.pageindicator.anim.select;

import android.animation.ObjectAnimator;
import android.view.View;

import com.fionera.demo.util.pageindicator.anim.base.BaseIndicatorAnimator;

/**
 * @author fionera
 */
public class ZoomInEnter
        extends BaseIndicatorAnimator {
    public ZoomInEnter() {
        this.duration = 200;
    }

    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", 1.0F, 1.5F),
                ObjectAnimator.ofFloat(
                view, "scaleY", 1.0F, 1.5F));
    }
}
