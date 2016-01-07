package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.fionera.wechatdemo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SplitPageActivity extends Activity implements OnScrollListener {

    // ListView的Adapter
    private SimpleAdapter mSimpleAdapter;
    private ListView lv;
    private Button btn;
    private ProgressBar pg;
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
        footer = getLayoutInflater().inflate(R.layout.lv_more_info_footer, null);

        btn = (Button) footer.findViewById(R.id.bt_load);
        pg = (ProgressBar) footer.findViewById(R.id.pg);

        // 用map来装载数据，初始化10条数据
        list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 15; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
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

        // 绑定监听器
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pg.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // 加载额外的5条数据
                        loadMoreDate();
                        btn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        mSimpleAdapter.notifyDataSetChanged();// 通知listView刷新数据
                    }

                }, 2000);
            }
        });
        //btn.setVisibility(View.GONE);
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
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", "new No." + i + " title");
                map.put("ItemText", "new No." + i + " content");
                list.add(map);
            }
        } else {
            // 数据已经不足5条
            for (int i = count; i < MaxDateNum; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
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
            pg.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    loadMoreDate();
                    btn.setVisibility(View.VISIBLE);
                    pg.setVisibility(View.GONE);
                    mSimpleAdapter.notifyDataSetChanged();
                }

            }, 2000);

        }

    }

}