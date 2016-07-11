package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.demo.R;
import com.fionera.demo.view.BottomSheetDialogView;

public class BottomSheetActivity
        extends AppCompatActivity {

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

        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter().setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {

            @Override
            public void onItemClick(int pos) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                BottomSheetDialogView.show(BottomSheetActivity.this);
            }
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

        findViewById(R.id.tv_bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        public ItemClickListener mItemClickListener;

        public SimpleStringRecyclerViewAdapter setItemClickListener(ItemClickListener listener) {
            mItemClickListener = listener;
            return this;
        }

        public interface ItemClickListener {
            void onItemClick(int pos);
        }

        public SimpleStringRecyclerViewAdapter() {
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(position);
                }
            });
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
