package com.fionera.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.fionera.demo.view.CustomMarkerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author fionera
 */
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
        for (int i = 1; i <= 8; i++) {
            entryList.add(new Entry(i, (10 - i) % 5,"title"));
        }
        LineDataSet lineDataSet = new LineDataSet(entryList, "TestLine");
        lineDataSet.setColor(ContextCompat.getColor(mContext, R.color.black));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(4);
        lineDataSet.setCircleColors(Color.RED, Color.GRAY);
        lineDataSet.setCircleRadius(8);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.BLACK);
        lineDataSet.setHighlightLineWidth(4);
        lineDataSet.setCircleHoleRadius(4);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(16);
        lineDataSet.setValueTextColor(ContextCompat.getColor(mContext, R.color.green2));
        lineDataSet.setDrawValues(false);
        LineData lineData = new LineData(lineDataSet);
        lcAndroidChart.setData(lineData);
        Description description = new Description();
        description.setText("描述");
        description.setTextSize(16);
        description.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        lcAndroidChart.setDescription(description);
        lcAndroidChart.setNoDataText("无数据");
        lcAndroidChart.setDrawBorders(false);
        lcAndroidChart.setBorderColor(ContextCompat.getColor(mContext, R.color.yellow1));
        lcAndroidChart.setBorderWidth(2);
        lcAndroidChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                ShowToast.show(e.getX() + "");
            }

            @Override
            public void onNothingSelected() {
                ShowToast.show("-1");
            }
        });
        Legend legend = lcAndroidChart.getLegend();
        legend.setEnabled(false);
        lcAndroidChart.setScaleEnabled(false);
        lcAndroidChart.setDrawMarkers(true);
        lcAndroidChart.setMarker(new CustomMarkerView(mContext, R.layout.layout_custom_marker));
        XAxis xAxis = lcAndroidChart.getXAxis();
        xAxis.setValueFormatter((value, axis) -> String.format(Locale.CHINA, "%2.0f月", value));
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.blue1));
        xAxis.setGridLineWidth(4);
        xAxis.setAxisLineColor(ContextCompat.getColor(mContext, R.color.blue));
        xAxis.setAxisLineWidth(4);
        xAxis.enableGridDashedLine(20, 4, 0);
        xAxis.addLimitLine(new LimitLine(3, "T"));
        xAxis.setTextSize(14);
        xAxis.setLabelCount(11);
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(12);

        YAxis yAxis = lcAndroidChart.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawAxisLine(true);
        yAxis.setGridColor(ContextCompat.getColor(mContext, R.color.blue1));
        yAxis.setGridLineWidth(4);
        yAxis.setAxisLineColor(Color.RED);
        yAxis.setAxisLineWidth(8);
        yAxis.addLimitLine(new LimitLine(3, "T"));
        yAxis.setDrawLimitLinesBehindData(true);
        yAxis.setAxisMinimum(0);
        yAxis.setSpaceTop(25);
        yAxis.setTextSize(16);

        lcAndroidChart.getAxisRight().setEnabled(false);

        lcAndroidChart.animateXY(700, 1000, Easing.EasingOption.EaseInCubic,
                Easing.EasingOption.EaseInCubic);
        lcAndroidChart.setHardwareAccelerationEnabled(true);

        lcAndroidChart.invalidate();

        List<BarEntry> barEntryList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            barEntryList.add(new BarEntry(i, (10 - i) % 5,"title"));
        }
        BarDataSet barDataSet = new BarDataSet(barEntryList, "TestBar");
        BarData barData = new BarData(barDataSet);
        bcAndroidChart.setData(barData);
        bcAndroidChart.invalidate();
    }

    private void initView() {
        lcAndroidChart = (LineChart) findViewById(R.id.lc_android_chart);
        bcAndroidChart = (BarChart) findViewById(R.id.bc_android_chart);
    }
}
