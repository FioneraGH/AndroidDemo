package com.fionera.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.util.stickyheaders.SectioningAdapter;
import com.fionera.demo.util.stickyheaders.StickyHeaderLayoutManager;

public class StickyHeaderActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_header);

        String[] demos = {"111", "222", "333"};
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_sticky_header);
        recyclerView.setLayoutManager(new StickyHeaderLayoutManager());
        recyclerView.setAdapter(new DemoAdapter(demos));
    }

    private class DemoAdapter
            extends SectioningAdapter {

        class HeaderViewHolder
                extends SectioningAdapter.HeaderViewHolder {
            TextView titleTextView;

            HeaderViewHolder(View itemView) {
                super(itemView);
                titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            }
        }

        class ItemViewHolder
                extends SectioningAdapter.ItemViewHolder {
            TextView titleTextView;
            TextView descriptionTextView;

            ItemViewHolder(View itemView) {
                super(itemView);
                titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
                descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            }
        }

        class FooterViewHolder
                extends SectioningAdapter.FooterViewHolder {
            TextView titleTextView;

            FooterViewHolder(View itemView) {
                super(itemView);
                titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            }
        }

        String[] demos;

        DemoAdapter(String[] demos) {
            this.demos = demos;
        }

        @Override
        public int getNumberOfSections() {
            return 5;
        }

        @Override
        public int getNumberOfItemsInSection(int sectionIndex) {
            return demos.length;
        }

        @Override
        public boolean doesSectionHaveHeader(int sectionIndex) {
            return true;
        }

        @Override
        public boolean doesSectionHaveFooter(int sectionIndex) {
            return true;
        }

        @Override
        public SectioningAdapter.HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return new HeaderViewHolder(LayoutInflater.from(StickyHeaderActivity.this)
                                                .inflate(R.layout.rv_sticky_header_header, parent,
                                                         false));
        }

        @Override
        public SectioningAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent) {
            return new ItemViewHolder(LayoutInflater.from(StickyHeaderActivity.this)
                                              .inflate(R.layout.rv_sticky_header_item, parent,
                                                       false));
        }

        @Override
        public SectioningAdapter.FooterViewHolder onCreateFooterViewHolder(ViewGroup parent) {
            return new FooterViewHolder(LayoutInflater.from(StickyHeaderActivity.this)
                                                .inflate(R.layout.rv_sticky_header_header, parent,
                                                         false));
        }

        @Override
        public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder,
                                           int sectionIndex) {
            HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
            hvh.titleTextView.setText("title");
        }

        @Override
        public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder,
                                         int sectionIndex, int itemIndex) {
            ItemViewHolder ivh = (ItemViewHolder) viewHolder;
            ivh.titleTextView.setText(demos[itemIndex]);
            ivh.descriptionTextView.setText(demos[itemIndex]);
        }

        @Override
        public void onBindFooterViewHolder(SectioningAdapter.FooterViewHolder viewHolder,
                                           int sectionIndex) {
            FooterViewHolder fvh = (FooterViewHolder) viewHolder;
            fvh.titleTextView.setText("footer");
        }
    }

}
