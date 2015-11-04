package com.fionera.wechatdemo.extra;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fionera.wechatdemo.R;

public class PropertyAnimActivity extends Activity {

    private static int WIDTH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_anim);
    }

    public void scaleWidth(final View view){

        if(0 == WIDTH){
            WIDTH = view.getWidth();
        }
        ViewWrapper wrapper = new ViewWrapper(view, WIDTH);
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.an_scale_width);
        animatorSet.setTarget(wrapper);
        animatorSet.start();
    }

    private static class ViewWrapper {
        private View target;
        private int maxWidth;
        public ViewWrapper(View target, int maxWidth) {
            this.target = target;
            this.maxWidth = maxWidth;
        }
        public int getWidth() {
            return target.getLayoutParams().width;
        }
        public void setWidth(int widthValue) {
            target.getLayoutParams().width = maxWidth * widthValue / 100;
            target.requestLayout();
        }
        public void setMarginTop(int margin) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) target.getLayoutParams();
            layoutParams.setMargins(0, margin, 0, 0);
            target.setLayoutParams(layoutParams);
        }
    }

}
