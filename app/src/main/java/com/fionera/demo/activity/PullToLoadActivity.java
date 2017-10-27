package com.fionera.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fionera.base.activity.BaseActivity;
import com.fionera.demo.R;
import com.fionera.demo.bean.ChatMsgBean;
import com.fionera.demo.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fionera
 */
public class PullToLoadActivity
        extends BaseActivity
        implements AbsListView.OnScrollListener {

    private ListView listView;
    private View header;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this, "ChatEntity");

    private static final int PAGE_SIZE = 20;
    private int allRecorders = 0;  // 全部记录数
    private int currentPage = 1; // 默认在第一页
    private int pageSize = 1;  // 默认共一页

    private List<ChatMsgBean> items = new ArrayList<>();

    private TestAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_load);

        listView = findViewById(R.id.lv_load_data);
        header = View.inflate(mContext, R.layout.layout_load_more, null);
        listView.setOnScrollListener(this);
        showAllData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    /**
     * 读取显示数据
     */
    public void showAllData() {
        allRecorders = databaseHelper.getCount();
        if (allRecorders > PAGE_SIZE) {
            listView.addHeaderView(header);
        }

        pageSize = (allRecorders + PAGE_SIZE - 1) / PAGE_SIZE;
        items.addAll(databaseHelper.getSomeItems(currentPage, PAGE_SIZE));

        Collections.reverse(items);

        baseAdapter = new TestAdapter();
        listView.setAdapter(baseAdapter);
        listView.setSelection(items.size() - 1);
        databaseHelper.closeDb();
    }

    int firstItem = -1;

    @Override
    public void onScroll(AbsListView absView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        firstItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scorllState) {
        if (firstItem == 0 && currentPage < pageSize && scorllState == AbsListView
                .OnScrollListener.SCROLL_STATE_IDLE) {
            currentPage++;
            appendDate();
        }
    }

    /**
     * 增加数据
     */
    private void appendDate() {
        List<ChatMsgBean> addItems = databaseHelper.getSomeItems(currentPage, PAGE_SIZE);
        items.addAll(0, addItems);
        if (allRecorders == items.size()) {
            listView.removeHeaderView(header);
        }

        baseAdapter.notifyDataSetChanged();
        listView.setSelection(addItems.size());
    }

    private class TestAdapter
            extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int pos) {
            return items.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View v, ViewGroup p) {

            TextView view = new TextView(PullToLoadActivity.this);
            view.setTextSize(25);

            if (items != null) {
                view.setText(items.get(pos).getText());
            } else {
                view.setText(pos);
            }
            return view;
        }
    }
}