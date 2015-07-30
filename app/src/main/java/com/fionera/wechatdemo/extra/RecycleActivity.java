package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fionera.wechatdemo.MainActivity;
import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.ChatMsgEntry;
import com.fionera.wechatdemo.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity extends Activity {

    private RecyclerView recyclerView;
    private List<ChatMsgEntry> data;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data = new ArrayList<ChatMsgEntry>();

        for (int i = 0; i < 10; i++) {
            ChatMsgEntry entry = new ChatMsgEntry();
            entry.setDate("0000-00-00");
            entry.setName("hello");
            entry.setMsgType(false);
            entry.setText("world " + i);
            data.add(entry);
        }

        myAdapter = new MyAdapter(RecycleActivity.this, data);
        recyclerView.setAdapter(myAdapter);

        // 设定RecyclerView的布局方式
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecycleActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // 设定分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<ChatMsgEntry> data;

        /**
         * Adapter 适配器构造方法
         * @param context 上下文
         * @param data 数据载体
         */
        MyAdapter(Context context, List<ChatMsgEntry> data) {

            this.data = data;
            layoutInflater = LayoutInflater.from(context);

        }

        /**
         * 绑定适配器时创建ViewHolder
         * @param parent 父级View
         * @param viewType View类型
         * @return 返回ViewHolder
         */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.chat_msg_right_item, parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        /**
         * 绑定ViewHolder显示数据
         * @param holder ViewHolder
         * @param position Item位置
         */
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

    /**
     * ViewHolder 类，用于创建ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

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
