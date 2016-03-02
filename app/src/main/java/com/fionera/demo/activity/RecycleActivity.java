package com.fionera.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.bean.ChatMsgBean;
import com.fionera.demo.util.DividerItemDecoration;
import com.fionera.demo.view.PullToRefreshLayout;
import com.fionera.demo.view.PullableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity
        extends Activity {

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableRecyclerView recyclerView;
    private List<ChatMsgBean> data;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptrl_root);
        recyclerView = (PullableRecyclerView) findViewById(R.id.recycler_view);
        data = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ChatMsgBean entry = new ChatMsgBean();
            entry.setDate("0000-00-00");
            entry.setName("hello");
            entry.setMsgType(false);
            entry.setText("world " + i);
            data.add(entry);
        }

        recyclerView.setLayoutManager(
                new LinearLayoutManager(RecycleActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MyAdapter(RecycleActivity.this, data));

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        recyclerView.setTotalCount(data.size());
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout ptrl) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1500);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout ptrl) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1500);
            }
        });
    }

    class MyAdapter
            extends RecyclerView.Adapter<MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<ChatMsgBean> data;

        /**
         * Adapter 适配器构造方法
         *
         * @param context 上下文
         * @param data    数据载体
         */
        MyAdapter(Context context, List<ChatMsgBean> data) {

            this.data = data;
            layoutInflater = LayoutInflater.from(context);

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(
                    layoutInflater.inflate(R.layout.lv_chat_msg_left_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.tvUserName.setText(data.get(position).getName());
            holder.tvSendTime.setText(data.get(position).getDate());
            holder.tvContent.setText(data.get(position).getText());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class MyViewHolder
            extends RecyclerView.ViewHolder {

        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public boolean isComMsg;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSendTime = (TextView) itemView.findViewById(R.id.tv_sendtime);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_username);
            tvContent = (TextView) itemView.findViewById(R.id.tv_chatcontent);
            isComMsg = false;
        }
    }
}
