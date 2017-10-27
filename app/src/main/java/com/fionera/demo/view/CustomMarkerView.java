package com.fionera.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.fionera.demo.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * CustomMarkerView
 *
 * @author fionera
 * @date 17-6-7 in AndroidDemo
 */

@SuppressLint("ViewConstructor")
public class CustomMarkerView
        extends MarkerView {
    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("销量" + e.getY());
        super.refreshContent(e, highlight);
    }
}
