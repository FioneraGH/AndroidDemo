package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.base.activity.BaseActivity;
import com.fionera.demo.R;
import com.fionera.demo.view.BottomSheetDialogView;

public class BottomSheetActivity
        extends BaseActivity {

    public BottomSheetBehavior behavior;
    public RecyclerView recyclerView;
    public View ns_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        ns_view = findViewById(R.id.ns_bottom_sheet);
        recyclerView = (RecyclerView) findViewById(R.id.rv_bottom_sheet);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter().setItemClickListener(pos -> {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            BottomSheetDialogView.show(mContext);
        }));

        behavior = BottomSheetBehavior.from(ns_view);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        findViewById(R.id.tv_bottom_sheet).setOnClickListener(view -> {
            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        ItemClickListener mItemClickListener;

        SimpleStringRecyclerViewAdapter setItemClickListener(ItemClickListener listener) {
            mItemClickListener = listener;
            return this;
        }

        interface ItemClickListener {
            void onItemClick(int pos);
        }

        SimpleStringRecyclerViewAdapter() {
            super();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_recent_session_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.itemView.setOnClickListener(
                    view -> mItemClickListener.onItemClick(holder.getAdapterPosition()));
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public static class ViewHolder
                extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}
