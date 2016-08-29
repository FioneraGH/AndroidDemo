package com.fionera.demo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fionera.demo.R;
import com.fionera.demo.util.ShowToast;

public class ConstraintLayoutActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);
    }

    public void turnOnNight(View view) {
        Intent localIntent = new Intent();
        localIntent.setClassName("com.android.systemui", "com.android.systemui.tuner.TunerActivity");
        localIntent.putExtra("show_night_mode", true);
        startActivity(localIntent);
        ShowToast.show("open");
    }
}
