package com.fionera.demo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.demo.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author fionera
 * @date 16-3-1
 */
public class BottomSheetDialogView {

    private BottomSheetDialogView(Context context) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        View view = View.inflate(context, R.layout.bottom_sheet_dialog_recycler_view, null);

        RecyclerView recyclerView = view
                .findViewById(R.id.bottom_sheet_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new SimpleAdapter());

        dialog.setContentView(view);
        dialog.show();
    }

    public static void show(Context context) {
        new BottomSheetDialogView(context);
    }

    private static class SimpleAdapter
            extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rv_recent_session_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    private static class ViewHolder
            extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}