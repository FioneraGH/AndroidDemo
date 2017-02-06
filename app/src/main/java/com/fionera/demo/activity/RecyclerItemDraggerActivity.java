package com.fionera.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.fionera.demo.DemoApplication;
import com.fionera.demo.R;
import com.fionera.demo.bean.ChatMsgBean;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.view.PullToRefreshLayout;
import com.fionera.demo.view.PullableRecyclerView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerItemDraggerActivity
        extends AppCompatActivity {

    @ViewInject(R.id.ptrl_root)
    private PullToRefreshLayout pullToRefreshLayout;
    @ViewInject(R.id.recycler_view)
    private PullableRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        x.view().inject(this);

        final List<ChatMsgBean> data = new ArrayList<>();

        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout ptrl) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        data.clear();
                        for (int i = 0; i < 10; i++) {
                            ChatMsgBean entry = new ChatMsgBean();
                            entry.setDate("0000-00-00");
                            entry.setName("hello");
                            entry.setMsgType(false);
                            entry.setText("world " + i);
                            data.add(entry);
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        recyclerView.setTotalCount(data.size());
                    }
                }.sendEmptyMessageDelayed(0, 1500);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout ptrl) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        for (int i = 0; i < 10; i++) {
                            ChatMsgBean entry = new ChatMsgBean();
                            entry.setDate("0000-00-00");
                            entry.setName("hello");
                            entry.setMsgType(false);
                            entry.setText("world " + i);
                            data.add(entry);
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        recyclerView.setTotalCount(data.size());
                    }
                }.sendEmptyMessageDelayed(0, 1500);
            }
        });
        pullToRefreshLayout.autoRefresh();

        recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerItemDraggerActivity.this,
                                                              LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MyAdapter(RecyclerItemDraggerActivity.this, data));

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerView.setTotalCount(data.size());

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                                                   ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        data.remove(position);
                        recyclerView.getAdapter().notifyItemRemoved(position);
                    }

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        Collections.swap(data, viewHolder.getAdapterPosition(),
                                         target.getAdapterPosition());
                        recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(),
                                                                  target.getAdapterPosition());
                        return false;
                    }

                }).attachToRecyclerView(recyclerView);
    }

    private class MyAdapter
            extends RecyclerView.Adapter<MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<ChatMsgBean> data;
        private int lastPosition = -1;

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
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tvUserName.setText(data.get(position).getName());
            holder.tvSendTime.setText(data.get(position).getDate());
            holder.tvContent.setText(data.get(position).getText());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowToast.show(position + " " + holder.getAdapterPosition() + " " + holder
                            .getLayoutPosition());
                }
            });

            runItemAnim(holder.itemView, position);
        }

        private void runItemAnim(View itemView, int position) {
            if (position > lastPosition) {
                lastPosition = position;
                itemView.setTranslationY(DemoApplication.screenHeight);
                if (position < 6) {
                    itemView.animate().withLayer().translationY(0f)
                            .setInterpolator(new DecelerateInterpolator(1.0f))
                            .setStartDelay(100 * position).start();
                } else {
                    itemView.animate().withLayer().translationY(0f)
                            .setInterpolator(new DecelerateInterpolator(1.0f)).setStartDelay(100)
                            .start();
                }
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class MyViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvSendTime;
        TextView tvUserName;
        TextView tvContent;
        boolean isComMsg;

        MyViewHolder(View itemView) {
            super(itemView);
            tvSendTime = (TextView) itemView.findViewById(R.id.tv_sendtime);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_username);
            tvContent = (TextView) itemView.findViewById(R.id.tv_chatcontent);
            isComMsg = false;
        }
    }
}
