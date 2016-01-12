package com.fionera.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.fionera.demo.R;
import com.fionera.demo.util.ShooterStage;

import java.util.ArrayList;

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
        danmuList.add("你好-----------------------------------------");
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
