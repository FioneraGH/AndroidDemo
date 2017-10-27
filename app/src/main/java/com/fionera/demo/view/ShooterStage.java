package com.fionera.demo.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.fionera.demo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author fionera
 * @date 15-11-24
 */
public class ShooterStage {
    /**
     * 假定一个字占用的像素
     */
    private static final int CHAR_WIDTH = 100;
    private Context context;

    private Set<Object> existVerticalMargins;
    /**
     * 弹幕显示数量，弹幕总数量
     */
    private int count = 0;
    private int danmuCount = 0;

    /**
     * Stage坐标宽高
     */
    private float stageX = 0;
    private float stageY = 0;
    private int stageWidth = 0;
    private int stageHeight = 0;

    private ScheduledThreadPoolExecutor timer;
    private ArrayList<Animator> objectAnimators;
    private ArrayList<String> danmuList;

    public ShooterStage(Context context, ArrayList<String> danmuList) {
        this.context = context;
        this.danmuList = danmuList;
        this.danmuCount = danmuList.size();
        objectAnimators = new ArrayList<>();
        existVerticalMargins = new HashSet<>();
    }

    /**
     * 取消弹幕的发送
     */
    public void destroyStage() {
        timer.shutdown();
    }

    /**
     * 弹幕的发送调用，周期性
     */
    private void shootItem(final RelativeLayout rlStage) {
        /*
          计算stage坐标尺寸
         */
        rlStage.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        stageX = rlStage.getX();
                        stageY = rlStage.getY();
                        stageWidth = rlStage.getWidth() - rlStage.getPaddingLeft() - rlStage
                                .getPaddingRight();
                        stageHeight = rlStage.getHeight() - rlStage.getPaddingTop() - rlStage
                                .getPaddingBottom();
                        rlStage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        /*
          构造一个用于在主线程上运行的Runnable
         */
        final Runnable run = () -> addDanmu(rlStage);
        timer = new ScheduledThreadPoolExecutor(1, r -> new Thread(r, "stage-shoot-%d"));
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(run);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 500, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 实际的弹幕发送调用
     */
    private void addDanmu(final RelativeLayout rlStage) {

        String currentString = danmuList.get(count % danmuCount);
        Random random = new Random(System.currentTimeMillis());
        int seedHeight = random.nextInt(stageHeight / CHAR_WIDTH - 1);
        int seedSize = random.nextInt(5);

        /*
          弹幕TextView
         */
        final TextView textView = new TextView(context);
        textView.setText(currentString);
        textView.setTextColor(0xffffffff);
        textView.setSingleLine();
        textView.setTextSize(22 - seedSize * 2);
        textView.setBackground(new ColorDrawable(0x66ff0000));

        /*
          设置弹幕距离顶部高度，可以使用setTransactionY替代
         */
        while (true) {
            int verticalMargin = seedHeight * CHAR_WIDTH + 10;
            if (!existVerticalMargins.contains(verticalMargin)) {
                existVerticalMargins.add(verticalMargin);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.topMargin = verticalMargin;
                textView.setLayoutParams(params);
                textView.setTag(R.id.danmu_vertical_margin, verticalMargin);
                break;
            } else {
                /*
                  当前同屏显示的弹幕数量已经到达最大行数，则放弃此次弹幕发送
                 */
                if (count == stageHeight / CHAR_WIDTH) {
                    return;
                }
                seedHeight = random.nextInt(stageHeight / CHAR_WIDTH);
            }
        }
        LayoutParams p = (LayoutParams) textView.getLayoutParams();
        Log.d("-------",seedHeight + " " + p.topMargin);

        textView.setOnClickListener(v -> {

            ((TextView) v).setTextColor(0xff0000ff);
            pauseStage();

            final TextView nowClick = (TextView) v;
            new AlertDialog.Builder(context).setCancelable(false).setTitle(
                    "Test").setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        nowClick.setTextColor(0xffffffff);
                        resumeStage(rlStage);
                    }).show();
        });

        /*
          TextView移动动画
         */
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textView, "translationX",
                stageX + stageWidth, stageX - (currentString.length() * CHAR_WIDTH)).setDuration(
                10000 - seedSize * 500);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /*
                  移除被移除的弹幕的动画和被占用的margin
                 */
                objectAnimators.remove(animation);
                existVerticalMargins.remove(textView.getTag(R.id.danmu_vertical_margin));
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
        objectAnimators.add(objectAnimator);
        objectAnimator.start();
        rlStage.addView(textView);
        count++;
    }

    /**
     * 暂停所有的
     */
    private void pauseStage() {
        timer.shutdown();
        for (Animator objectAnimator : objectAnimators) {
            objectAnimator.pause();
        }
    }

    /**
     * 恢复所有的
     */
    private void resumeStage(RelativeLayout rlStage) {
        shootItem(rlStage);
        for (Animator objectAnimator : objectAnimators) {
            objectAnimator.resume();
        }
    }
}
