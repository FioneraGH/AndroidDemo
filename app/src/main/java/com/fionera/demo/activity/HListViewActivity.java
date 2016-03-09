package com.fionera.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fionera.demo.R;
import com.fionera.demo.util.ShowToast;
import com.fionera.demo.view.CHScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 带滑动表头与固定列的ListView
 */
public class HListViewActivity
        extends Activity {
    private ListView mListView;
    //方便测试，直接写的public
    public HorizontalScrollView mTouchView;
    //装入所有的HScrollView
    protected List<CHScrollView> mHScrollViews = new ArrayList<>();
    private String[] cols = new String[]{"title", "data_1", "data_2", "data_3", "data_4",
            "data_5", "data_6", "data_7", "data_8", "data_9",};

    private ScrollAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hlistview);
        initViews();
    }

    private void initViews() {
        List<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> data;
        CHScrollView headerScroll = (CHScrollView) findViewById(R.id.item_scroll_title);
        //添加头滑动事件
        mHScrollViews.add(headerScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        for (int i = 0; i < 20; i++) {
            data = new HashMap<>();
            data.put("title", "区_" + i);
            for (int j = 1; j < cols.length; j++) {
                data.put("data_" + j, "Date_" + j + "_" + i);
            }

            datas.add(data);
        }
        mAdapter = new ScrollAdapter(this, datas, R.layout.lv_hlistview_item, cols,
                                     new int[]{R.id.item_titlev, R.id.item_datav1, R.id
                                             .item_datav2, R.id.item_datav3, R.id.item_datav4, R
                                             .id.item_datav5, R.id.item_datav6});
        mListView.setAdapter(mAdapter);
    }

    public void addHViews(final CHScrollView hScrollView) {
        if (!mHScrollViews.isEmpty()) {
            int size = mHScrollViews.size();
            CHScrollView scrollView = mHScrollViews.get(size - 1);
            final int scrollX = scrollView.getScrollX();
            //第一次满屏后，向下滑动，有一条数据在开始时未加入
            if (scrollX != 0) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        //当listView刷新完成之后，把该条移动到最终位置
                        hScrollView.scrollTo(scrollX, 0);
                    }
                });
            }
        }
        mHScrollViews.add(hScrollView);
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (CHScrollView scrollView : mHScrollViews) {
            //防止重复滑动
            if (mTouchView != scrollView) {
                scrollView.smoothScrollTo(l, t);
            }
        }
    }

    class ScrollAdapter
            extends SimpleAdapter {

        private List<? extends Map<String, ?>> datas;
        private int res;
        private String[] from;
        private int[] to;
        private Context context;

        public ScrollAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                             String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
            this.datas = data;
            this.res = resource;
            this.from = from;
            this.to = to;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(context).inflate(res, null);
                //第一次初始化的时候装进来
                addHViews((CHScrollView) v.findViewById(R.id.item_chscroll_scroll));
                View[] views = new View[to.length];
                //单元格点击事件
                for (int i = 0; i < to.length; i++) {
                    View tv = v.findViewById(to[i]);
                    tv.setOnClickListener(clickListener);
                    views[i] = tv;
                }
                //每行点击事件
                /*for(int i = 0 ; i < from.length; i++) {
					View tv = v.findViewById(row_hlistview[i]);
				}*/
                //
                v.setTag(views);
            }
            View[] holders = (View[]) v.getTag();
            int len = holders.length;
            for (int i = 0; i < len; i++) {
                ((TextView) holders[i]).setText(this.datas.get(position).get(from[i]).toString());
            }
            return v;
        }
    }

    protected View.OnClickListener clickListener = v -> ShowToast
            .show("点击了:" + ((TextView) v).getText());
}
