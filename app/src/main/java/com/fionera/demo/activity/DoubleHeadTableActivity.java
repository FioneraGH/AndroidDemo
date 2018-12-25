package com.fionera.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.fionera.demo.view.ListNestedScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author fionera
 */
public class DoubleHeadTableActivity
        extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HorizontalScrollView touchView;
    private List<ListNestedScrollView> mHScrollItems = new ArrayList<>();
    private String[] columns = new String[]{"title", "data_1", "data_2", "data_3", "data_4",
            "data_5", "data_6"};

    public void setTouchView(HorizontalScrollView touchView) {
        this.touchView = touchView;
    }

    public HorizontalScrollView getTouchView() {
        return touchView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_head_table);
        new Handler().postDelayed(this::init, 3000);
    }

    private void init() {
        List<Map<String, String>> datas = new ArrayList<>();
        mHScrollItems.add(findViewById(R.id.item_scroll_title));
        recyclerView = findViewById(R.id.rv_scroll_list);
        for (int i = 0; i < 20; i++) {
            Map<String, String> data = new HashMap<>();
            data.put("title", "区_" + i);
            for (int j = 1; j < columns.length; j++) {
                data.put("data_" + j, "Date_" + j + "_" + i);
            }
            datas.add(data);
        }
        recyclerView.setAdapter(new ScrollAdapter(this, datas));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onScrollChanged(int l, int t) {
        for (ListNestedScrollView scrollView : mHScrollItems) {
            if (touchView != scrollView) {
                scrollView.smoothScrollTo(l, t);
            }
        }
    }

    private class ScrollAdapter
            extends RecyclerView.Adapter<ListHolder> {

        private Context context;
        private List<? extends Map<String, ?>> datas;

        ScrollAdapter(Context context, List<? extends Map<String, ?>> data) {
            this.context = context;
            this.datas = data;
        }


        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListHolder(LayoutInflater.from(context)
                                          .inflate(R.layout.rv_hscroll_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ListHolder holder, int position) {
            if (!mHScrollItems.isEmpty()) {
                final int scrollX = mHScrollItems.get(mHScrollItems.size() - 1).getScrollX();
                if (0 != scrollX) {
                    recyclerView.post(() -> holder.listNestedScrollView.scrollTo(scrollX, 0));
                }
            }
            mHScrollItems.add(holder.listNestedScrollView);
            int lineCount = ((LinearLayout) holder.listNestedScrollView.getChildAt(0))
                    .getChildCount();
            for (int i = 0; i < lineCount; i++) {
                TextView tv = (TextView) ((LinearLayout) holder.listNestedScrollView.getChildAt(0))
                        .getChildAt(i);
                tv.setOnClickListener(clickListener);
                tv.setText(datas.get(position).get(columns[i]).toString());
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    private View.OnClickListener clickListener = view -> ShowToast
            .show("点击了:" + ((TextView) view).getText());

    private class ListHolder
            extends RecyclerView.ViewHolder {

        ListNestedScrollView listNestedScrollView;

        ListHolder(View itemView) {
            super(itemView);
            listNestedScrollView = itemView
                    .findViewById(R.id.item_chscroll_scroll);
        }
    }
}
