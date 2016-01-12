package com.fionera.wechatdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.util.RvItemTouchListener;

import java.util.zip.Inflater;

/**
 * Created by fionera on 16-1-12.
 */
public class RecentSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private RvItemTouchListener rvItemTouchListener;

    public void setRvItemTouchListener(RvItemTouchListener rvItemTouchListener) {
        this.rvItemTouchListener = rvItemTouchListener;
    }

    public RecentSessionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_recent_session_item, parent,
                false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(
                v -> rvItemTouchListener.onItemClick(holder.itemView, position));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private class RecentViewHolder extends RecyclerView.ViewHolder {


        public RecentViewHolder(View itemView) {
            super(itemView);

        }
    }
}
