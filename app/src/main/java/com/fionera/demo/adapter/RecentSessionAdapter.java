package com.fionera.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.demo.R;
import com.fionera.demo.util.RvItemTouchListener;

import java.util.List;

/**
 * Created by fionera on 16-1-12.
 */
public class RecentSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> data;

    private RvItemTouchListener rvItemTouchListener;

    public void setRvItemTouchListener(RvItemTouchListener rvItemTouchListener) {
        this.rvItemTouchListener = rvItemTouchListener;
    }

    public RecentSessionAdapter(Context context,List<String> data) {
        this.context = context;
        this.data = data;
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
        return data.size();
    }

    private class RecentViewHolder extends RecyclerView.ViewHolder {


        public RecentViewHolder(View itemView) {
            super(itemView);

        }
    }
}
