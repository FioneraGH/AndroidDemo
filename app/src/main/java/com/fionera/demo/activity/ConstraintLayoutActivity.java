package com.fionera.demo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.util.ShowToast;

public class ConstraintLayoutActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);

        TextView textView = (TextView) findViewById(R.id.tv_constraint_tips);
        textView.setText(stringFromJNI() + " " + addNumberUsingJNI(1, 10));
    }

    public native String stringFromJNI();

    public native int addNumberUsingJNI(int a, int b);

    static {
        System.loadLibrary("native-lib");
    }

    public void turnOnNight(View view) {
        Intent localIntent = new Intent();
        localIntent.setClassName("com.android.systemui",
                "com.android.systemui.tuner.TunerActivity");
        localIntent.putExtra("show_night_mode", true);
        startActivity(localIntent);
        ShowToast.show("open");
    }
}
