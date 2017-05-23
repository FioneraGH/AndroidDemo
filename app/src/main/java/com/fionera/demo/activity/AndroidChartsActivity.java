package com.fionera.demo.activity;

import android.os.Bundle;

import com.fionera.base.activity.BaseActivity;
import com.fionera.demo.R;
import com.fionera.demo.view.ViewDragLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class AndroidChartsActivity
        extends BaseActivity {

    private LineChart lcAndroidChart;
    private BarChart bcAndroidChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_charts);
        initView();

        List<Entry> entryList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entryList.add(new Entry(i, 10 - i));
        }
        LineDataSet lineDataSet = new LineDataSet(entryList,"TestLine");
        lineDataSet.setColor(R.color.blue2);
        lineDataSet.setValueTextColor(R.color.green2);
        LineData lineData = new LineData(lineDataSet);
        lcAndroidChart.setData(lineData);
        lcAndroidChart.invalidate();
    }

    private void initView() {
        lcAndroidChart = (LineChart) findViewById(R.id.lc_android_chart);
        bcAndroidChart = (BarChart) findViewById(R.id.bc_android_chart);
    }
}
