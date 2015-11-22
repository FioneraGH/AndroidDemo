package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.ChatMsgBean;
import com.fionera.wechatdemo.util.DBHelper;

import java.util.ArrayList;
import java.util.Collections;

public class PullToLoadActivity extends Activity implements AbsListView.OnScrollListener {

    private static final int ITEM_SIZE = 20;

    private ListView listView;
    private Button btn;
    private ProgressBar pg;
    private View header;
    private ArrayList<ChatMsgBean> items;
    private DBHelper dbHelper = new DBHelper(this, "ChatEntity");
    private int allRecorders = 0;  //全部记录数
    private int currentPage = 1; //默认在第一页
    private int pageSize = 1;  //默认共一页

    private TestAdapter baseAdapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_load);
        listView = (ListView) findViewById(R.id.list_view_load);
        //获取listView头部，共享分页脚部
        header = getLayoutInflater().inflate(R.layout.more_info_foot, null);
        btn = (Button) header.findViewById(R.id.bt_load);
        btn.setVisibility(View.GONE);
        pg = (ProgressBar) header.findViewById(R.id.pg);
        pg.setVisibility(View.VISIBLE);

        listView.addHeaderView(header);
        listView.setOnScrollListener(this);

        showAllData();
    }

    /**
     * 读取显示数据
     */
    public void showAllData() {

        // 获取总记录数
        allRecorders = dbHelper.getCount();
        if (allRecorders < ITEM_SIZE) {
            listView.removeHeaderView(header);
        }
        // 计算总页数
        pageSize = (allRecorders + ITEM_SIZE - 1) / ITEM_SIZE;
        items = dbHelper.getSomeItems(currentPage, ITEM_SIZE);
        Collections.reverse(items);
        baseAdapter = new TestAdapter();
        listView.setAdapter(baseAdapter);
        listView.setSelection(items.size());//直接定位到最底部
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
                .OnScrollListener.SCROLL_STATE_IDLE) {// 不再滚动
            currentPage++;
            // 增加数据
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    appendDate();
                }

            }, 2000);

        }
    }

    /**
     * 增加数据
     */
    private void appendDate() {
        ArrayList addItems = dbHelper.getSomeItems(currentPage, ITEM_SIZE);
        baseAdapter.setCount(baseAdapter.getCount() + addItems.size());
        //判断，如果到了最末尾则去掉进度圈
        if (allRecorders == baseAdapter.getCount()) {
            listView.removeHeaderView(header);
        }
//        Collections.reverse(addItems);
        items.addAll(0, addItems);

        baseAdapter.notifyDataSetChanged();
        listView.setSelection(addItems.size());

    }

    class TestAdapter extends BaseAdapter {
        int count = ITEM_SIZE;

        public int getCount() {
            if (items.size() < ITEM_SIZE) {
                return items.size();
            } else {
                return count;
            }
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Object getItem(int pos) {
            return items.get(pos);
        }

        public long getItemId(int pos) {
            return pos;
        }

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