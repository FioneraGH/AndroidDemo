package com.fionera.wechatdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fionera.wechatdemo.DemoApplication;

/**
 * Created by fionera on 15-11-22.
 */
public class FloatView extends ImageView {

    private WindowManager wm = (WindowManager) getContext().getSystemService(
            Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams wmLayoutParams = DemoApplication.getWmLayoutParams();

    private float touchStartX;
    private float touchStartY;
    private float touchNewX;
    private float touchNewY;

    public FloatView(Context context) {
        super(context);
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("postion",touchStartX + "->" + touchNewX);

        touchNewX = event.getRawX();
        touchNewY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = event.getRawX();
                touchStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                updatePostion();
                break;
            case MotionEvent.ACTION_UP:
                updatePostion();
                touchStartX = touchStartY = 0;
                break;
        }
        return true;
    }

    private void updatePostion() {

        if(touchStartX == touchNewX){
            return;
        }
        wmLayoutParams.x = (int) (touchNewX - touchStartX);
        wmLayoutParams.y = (int) (touchNewY - touchStartY);
        wm.updateViewLayout(this, wmLayoutParams);
    }
}
