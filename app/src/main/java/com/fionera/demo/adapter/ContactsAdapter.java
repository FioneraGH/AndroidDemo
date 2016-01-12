package com.fionera.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.demo.R;
import com.fionera.demo.util.RvItemTouchListener;

/**
 * Created by fionera on 16-1-12.
 */
public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private RvItemTouchListener rvItemTouchListener;

    public void setRvItemTouchListener(RvItemTouchListener rvItemTouchListener) {
        this.rvItemTouchListener = rvItemTouchListener;
    }

    public ContactsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_contact_item, parent,
                false);
        return new ContactViewHolder(view);
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

    private class ContactViewHolder extends RecyclerView.ViewHolder {


        public ContactViewHolder(View itemView) {
            super(itemView);

        }
    }
}
