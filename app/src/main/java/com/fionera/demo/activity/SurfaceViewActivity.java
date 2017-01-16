package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fionera.demo.R;
import com.fionera.demo.view.Thermometer;

public class SurfaceViewActivity
        extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);

        Thermometer thermometer = (Thermometer) findViewById(R.id.tm_temperature);
        thermometer.setTargetTemperature(38f);
    }
}
