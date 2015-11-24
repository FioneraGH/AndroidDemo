package com.fionera.wechatdemo.extra;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.util.ShooterStage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DanmuActivity extends Activity {

    private RelativeLayout rlShootStage;
    private ShooterStage shooterStage;
    private ArrayList<String> danmuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmu);

        rlShootStage = (RelativeLayout) findViewById(R.id.rl_shoot_stage);

        danmuList = new ArrayList<>();
        danmuList.add("你好");
        danmuList.add("哈喽");
        danmuList.add("nihao");
        danmuList.add("hello");
        danmuList.add("你好你好");
        danmuList.add("你World好");
        danmuList.add("哈World喽");
        danmuList.add("nWorldihao");
        danmuList.add("hWorldello");
        danmuList.add("你World好你好");

        shooterStage = new ShooterStage(DanmuActivity.this,danmuList);
        shooterStage.shootItem(rlShootStage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shooterStage.destroyStage();
    }
}
