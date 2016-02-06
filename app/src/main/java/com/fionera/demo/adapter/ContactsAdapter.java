package com.fionera.demo.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.demo.R;
import com.fionera.demo.bean.ContactBean;
import com.fionera.demo.databinding.RvContactItemBinding;
import com.fionera.demo.util.RvItemTouchListener;

import java.util.List;

/**
 * Created by fionera on 16-1-12.
 */
public class ContactsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ContactBean> data;
    private RecyclerView recyclerView;

    private RvItemTouchListener rvItemTouchListener;

    public void setRvItemTouchListener(RvItemTouchListener rvItemTouchListener) {
        this.rvItemTouchListener = rvItemTouchListener;
    }

    public ContactsAdapter(Context context, List<ContactBean> data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RvContactItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.rv_contact_item, recyclerView,
                         false);
        ContactViewHolder viewHolder = new ContactViewHolder(binding.getRoot());
        viewHolder.setBinding(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ContactViewHolder)holder).getBinding().setContact(data.get(position));
        ((ContactViewHolder)holder).getBinding().executePendingBindings();
        holder.itemView.setOnClickListener(
                v -> rvItemTouchListener.onItemClick(holder.itemView, position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class ContactViewHolder
            extends RecyclerView.ViewHolder {

        private RvContactItemBinding binding;

        public ContactViewHolder(View itemView) {
            super(itemView);
        }

        public void setBinding(RvContactItemBinding binding) {
            this.binding = binding;
        }

        public RvContactItemBinding getBinding() {
            return binding;
        }
    }
}
