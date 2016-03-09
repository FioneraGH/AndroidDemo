package com.fionera.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.util.ShowToast;
import com.fionera.demo.view.ListNestedScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubleHeadTableActivity
        extends Activity {
    private ListView mListView;
    private HorizontalScrollView touchView;
    private List<ListNestedScrollView> mHScrollViews = new ArrayList<>();
    private String[] columns = new String[]{"title", "data_1", "data_2", "data_3", "data_4",
            "data_5", "data_6"};

    public void setTouchView(HorizontalScrollView touchView) {
        this.touchView = touchView;
    }

    public HorizontalScrollView getTouchView() {
        return touchView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_head_table);
        new Handler().postDelayed(this::init, 3000);
    }

    private void init() {
        List<Map<String, String>> datas = new ArrayList<>();
        mHScrollViews.add((ListNestedScrollView) findViewById(R.id.item_scroll_title));
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        for (int i = 0; i < 20; i++) {
            Map<String, String> data = new HashMap<>();
            data.put("title", "区_" + i);
            for (int j = 1; j < columns.length; j++) {
                data.put("data_" + j, "Date_" + j + "_" + i);
            }
            datas.add(data);
        }
        mListView.setAdapter(new ScrollAdapter(this, datas, R.layout.lv_hlistview_item, columns,
                                               new int[]{R.id.item_titlev, R.id.item_datav1, R.id
                                                       .item_datav2, R.id.item_datav3, R.id
                                                       .item_datav4, R.id.item_datav5, R.id
                                                       .item_datav6}));
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (ListNestedScrollView scrollView : mHScrollViews) {
            if (touchView != scrollView) {
                scrollView.smoothScrollTo(l, t);
            }
        }
    }

    private class ScrollAdapter
            extends SimpleAdapter {

        private Context context;
        private List<? extends Map<String, ?>> datas;
        private int resLayout;
        private String[] from;
        private int[] to;

        public ScrollAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                             String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
            this.datas = data;
            this.resLayout = resource;
            this.from = from;
            this.to = to;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(context).inflate(resLayout, parent, false);
                ListNestedScrollView listNestedScrollView = (ListNestedScrollView) v
                        .findViewById(R.id.item_chscroll_scroll);
                if (!mHScrollViews.isEmpty()) {
                    final int scrollX = mHScrollViews.get(mHScrollViews.size() - 1).getScrollX();
                    if (0 != scrollX) {
                        mListView.post(() -> listNestedScrollView.scrollTo(scrollX, 0));
                    }
                }
                mHScrollViews.add(listNestedScrollView);
                View[] views = new View[to.length];
                for (int i = 0; i < to.length; i++) {
                    View tv = v.findViewById(to[i]);
                    tv.setOnClickListener(clickListener);
                    views[i] = tv;
                }
                v.setTag(views);
            }
            View[] holders = (View[]) v.getTag();
            for (int i = 0; i < holders.length; i++) {
                ((TextView) holders[i]).setText(this.datas.get(position).get(from[i]).toString());
            }
            return v;
        }
    }


    private View.OnClickListener clickListener = v -> ShowToast
            .show("点击了:" + ((TextView) v).getText());
}
