package com.fionera.demo.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.base.activity.BaseActivity;
import com.fionera.demo.R;
import com.fionera.demo.view.BottomSheetDialogView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author fionera
 */
public class BottomSheetActivity
        extends BaseActivity {

    public BottomSheetBehavior behavior;
    public RecyclerView recyclerView;
    public View nsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        nsView = findViewById(R.id.ns_bottom_sheet);
        recyclerView = findViewById(R.id.rv_bottom_sheet);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter().setItemClickListener(pos -> {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            BottomSheetDialogView.show(mContext);
        }));

        behavior = BottomSheetBehavior.from(nsView);
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

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_recent_session_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            holder.itemView.setOnClickListener(
                    view -> mItemClickListener.onItemClick(holder.getAdapterPosition()));
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public static class ViewHolder
                extends RecyclerView.ViewHolder {

            ViewHolder(View view) {
                super(view);
            }
        }
    }
}
