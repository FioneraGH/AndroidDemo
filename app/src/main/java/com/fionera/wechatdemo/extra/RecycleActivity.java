package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.ChatMsgBean;
import com.fionera.wechatdemo.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity extends Activity {

    private RecyclerView recyclerView;
    private List<ChatMsgBean> data;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data = new ArrayList<ChatMsgBean>();

        for (int i = 0; i < 10; i++) {
            ChatMsgBean entry = new ChatMsgBean();
            entry.setDate("0000-00-00");
            entry.setName("hello");
            entry.setMsgType(false);
            entry.setText("world " + i);
            data.add(entry);
        }

        myAdapter = new MyAdapter(RecycleActivity.this, data);
        recyclerView.setAdapter(myAdapter);

        // 设定RecyclerView的布局方式
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecycleActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 设定分割线
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<ChatMsgBean> data;

        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;


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

        private boolean isHeader(int postion) {
            return postion == 0;
        }

        @Override
        public int getItemViewType(int position) {
            return isHeader(position) ? TYPE_HEADER : TYPE_ITEM;
        }

        /**
         * 绑定适配器时创建ViewHolder
         *
         * @param parent   父级View
         * @param viewType View类型
         * @return 返回ViewHolder
         */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View view = layoutInflater.inflate(R.layout.lv_chat_msg_right_item, parent, false);
                return new MyViewHolder(view);
            } else {
                View view = layoutInflater.inflate(R.layout.lv_chat_msg_left_item, parent, false);
                return new MyViewHolder(view);
            }
        }

        /**
         * 绑定ViewHolder显示数据
         *
         * @param holder   ViewHolder
         * @param position Item位置
         */
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            if (isHeader(position)) {
                return;
            }
            holder.tvUserName.setText(data.get(position - 1).getName());
            holder.tvSendTime.setText(data.get(position - 1).getDate());
            holder.tvContent.setText(data.get(position - 1).getText());
        }

        @Override
        public int getItemCount() {
            return data.size() + 1;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recycle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_add:

                data.add(new ChatMsgBean("hello", "1111-11-11", "hello !", false));
                //myAdapter.notifyItemChanged(data.size());
                myAdapter.notifyItemInserted(1);

                break;
            case R.id.action_delete:
                data.remove(0);
                myAdapter.notifyItemRemoved(1);
                break;
            case R.id.action_listview:
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(RecycleActivity.this, LinearLayoutManager.VERTICAL,
                                false));
                break;
            case R.id.action_gridview:
                recyclerView.setLayoutManager(new GridLayoutManager(RecycleActivity.this, 2));
                break;
            case R.id.action_hori_listview:
                recyclerView.setLayoutManager(new LinearLayoutManager(RecycleActivity.this,
                        LinearLayoutManager.HORIZONTAL, false));
                break;
            case R.id.action_hori_gridview:
                recyclerView.setLayoutManager(
                        new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
                break;
            case R.id.action_staggerd_gridview:
                ChatMsgBean entryTemp = data.get(1);
                data.get(2).setName(data.get(1).getName());
                data.get(2).setText(data.get(1).getText());
                data.get(2).setDate(data.get(1).getDate());
                data.get(2).setMsgType(data.get(1).getMsgType());

                data.get(1).setName(entryTemp.getName());
                data.get(1).setText(entryTemp.getText());
                data.get(1).setDate(entryTemp.getDate());
                data.get(1).setMsgType(entryTemp.getMsgType());

                myAdapter.notifyItemMoved(2, 3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
