package com.fionera.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fionera.demo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SplitPageActivity extends Activity implements OnScrollListener {

    // ListView的Adapter
    private SimpleAdapter mSimpleAdapter;
    private ListView lv;
    private ArrayList<HashMap<String, String>> list;
    // ListView底部View
    private View footer;
    private Handler handler = new Handler();
    ;
    // 设置一个最大的数据条数，超过即不再加载
    private int MaxDateNum;
    // 最后可见条目的索引
    private int lastVisibleIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_page);


        MaxDateNum = 30; // 设置最大数据条数

        lv = (ListView) findViewById(R.id.list_view_split);

        // 实例化底部布局
        footer = getLayoutInflater().inflate(R.layout.layout_load_more, null);

        // 用map来装载数据，初始化10条数据
        list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("ItemTitle", "No." + i + " title");
            map.put("ItemText", "No." + i + " content");
            list.add(map);
        }
        // 实例化SimpleAdapter
        mSimpleAdapter = new SimpleAdapter(this, list, R.layout.lv_split_page_item,
                new String[]{"ItemTitle", "ItemText"},
                new int[]{R.id.tv_title_split, R.id.tv_content_split});

        // 加上底部View，注意要放在setAdapter方法前
        //lv.addHeaderView(new ProgressBar(this));
        lv.addFooterView(footer);
        lv.setAdapter(mSimpleAdapter);
        lv.setOnScrollListener(this);

    }

    /**
     * 加载额外的数据
     */
    private void loadMoreDate() {
        int count = mSimpleAdapter.getCount();
        if (count + 5 < MaxDateNum) {
            // 每次加载5条
            for (int i = count; i < count + 5; i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("ItemTitle", "new No." + i + " title");
                map.put("ItemText", "new No." + i + " content");
                list.add(map);
            }
        } else {
            // 数据已经不足5条
            for (int i = count; i < MaxDateNum; i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("ItemTitle", "new No." + i + " title");
                map.put("ItemText", "new No." + i + " content");
                list.add(map);
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 计算最后可见条目的索引
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

        // 所有的条目已经和最大条数相等，则移除底部的View
        if (totalItemCount == MaxDateNum + 1) {
            lv.removeFooterView(footer);
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == mSimpleAdapter.getCount()) {
            // 当滑到底部时自动加载
            handler.postDelayed(() -> {
                loadMoreDate();
                mSimpleAdapter.notifyDataSetChanged();
            }, 2000);
        }
    }
}