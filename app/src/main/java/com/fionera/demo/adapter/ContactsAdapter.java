package com.fionera.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.demo.R;
import com.fionera.demo.bean.ContactBean;
import com.fionera.demo.util.RvItemTouchListener;

import java.util.List;

/**
 * Created by fionera on 16-1-12.
 */
public class ContactsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ContactBean> data;

    private RvItemTouchListener rvItemTouchListener;

    public void setRvItemTouchListener(RvItemTouchListener rvItemTouchListener) {
        this.rvItemTouchListener = rvItemTouchListener;
    }

    public ContactsAdapter(Context context, List<ContactBean> data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(context)
                                             .inflate(R.layout.rv_recent_session_item, parent,
                                                      false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(
                v -> rvItemTouchListener.onItemClick(holder.itemView, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class ContactViewHolder
            extends RecyclerView.ViewHolder {

        public ContactViewHolder(View itemView) {
            super(itemView);
        }
    }
}
