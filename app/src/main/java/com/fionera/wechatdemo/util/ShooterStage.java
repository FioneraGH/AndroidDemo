package com.fionera.wechatdemo.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fionera on 15-11-24.
 */
public class ShooterStage {

    private Context context;
    private int count = 0;
    private int width = 100;
    private Timer timer;
    private boolean isPause;
    private ArrayList<ObjectAnimator> objectAnimators;
    private ArrayList<String> danmuList;
    private int danmuCount;

    public ShooterStage(Context context, ArrayList<String> danmuList) {

        this.context = context;
        this.danmuList = danmuList;
        this.danmuCount = danmuList.size();
        objectAnimators = new ArrayList<>();
    }

    /**
     * 取消弹幕的发送
     */
    public void destroyStage() {
        timer.cancel();
    }

    /**
     * 弹幕的发送调用，周期性
     *
     * @param rlStage
     */
    public void shootItem(final RelativeLayout rlStage) {

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addDanmu(rlStage);
                    }
                });
            }
        };
        timer.schedule(timerTask, 300, 1000);
    }

    /**
     * 实际的弹幕发送调用
     *
     * @param rlStage
     */
    private void addDanmu(final RelativeLayout rlStage) {

        Random random = new Random(System.currentTimeMillis());
        int seedHeight = random.nextInt(15);
        int seedSize = random.nextInt(5);
        final TextView textView = new TextView(context);
        textView.setText(danmuList.get(count % danmuCount));
        textView.setTextColor(0xffffffff);
        textView.setTextSize(22 - seedSize * 2);
        textView.setTranslationY(seedHeight * 100);
        textView.setBackground(new ColorDrawable(0x66ff0000));
        textView.setTag(false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((TextView) v).setTextColor(0xff0000ff);
                pauseStage();

                final TextView nowClick = (TextView) v;
                new AlertDialog.Builder(context).setTitle("Test").setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                nowClick.setTextColor(0xffffffff);
                                resumeStage(rlStage);
                            }
                        }).show();
                isPause = !isPause;
            }
        });
        textView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        width = textView.getWidth();
                        textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textView, "translationX", 1080.0f,
                -(width + 100)).setDuration(10000 - seedSize * 700);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                objectAnimators.remove(animation);
                rlStage.removeView(textView);
                count--;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
        objectAnimators.add(objectAnimator);
        rlStage.addView(textView);
        count++;
    }


    /**
     * 暂停所有的
     */
    private void pauseStage() {

        timer.cancel();
        for (ObjectAnimator objectAnimator : objectAnimators) {
            objectAnimator.pause();
        }
    }

    /**
     * 恢复所有的
     * @param rlstage
     */
    private void resumeStage(RelativeLayout rlstage) {

        shootItem(rlstage);
        for (ObjectAnimator objectAnimator : objectAnimators) {
            objectAnimator.resume();
        }
    }
}
