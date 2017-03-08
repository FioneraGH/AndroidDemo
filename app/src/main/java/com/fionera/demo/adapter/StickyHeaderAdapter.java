package com.fionera.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.bean.StickyHeaderBean;
import com.fionera.multipic.common.ImageUtil;

import java.util.List;

/**
 * StickyHeaderAdapter
 * Created by fionera on 17-1-9 in AndroidDemo.
 */

public class StickyHeaderAdapter
        extends RecyclerView.Adapter<StickyHeaderAdapter.StickyHeaderHolder> {

    private Context context;
    private List<StickyHeaderBean.DataEntity.ComingEntity> comingEntityList;

    public StickyHeaderAdapter(Context context,
                               List<StickyHeaderBean.DataEntity.ComingEntity> comingEntityList) {
        this.context = context;
        this.comingEntityList = comingEntityList;
    }

    @Override
    public StickyHeaderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StickyHeaderHolder(LayoutInflater.from(context)
                .inflate(R.layout.rv_sticky_header_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StickyHeaderHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return comingEntityList.size();
    }

    class StickyHeaderHolder
            extends RecyclerView.ViewHolder {

        private ImageView ivStickyHeader;
        private TextView tvStickyHeaderName;
        private TextView tvStickyHeaderAudience;
        private TextView tvStickyHeaderProfessional;
        private TextView tvStickyHeaderDesc;
        private TextView tvStickyHeaderDate;

        StickyHeaderHolder(View itemView) {
            super(itemView);
            ivStickyHeader = (ImageView) itemView.findViewById(R.id.iv_sticky_header);
            tvStickyHeaderName = (TextView) itemView.findViewById(R.id.tv_sticky_header_name);
            tvStickyHeaderAudience = (TextView) itemView.findViewById(
                    R.id.tv_sticky_header_audience);
            tvStickyHeaderProfessional = (TextView) itemView.findViewById(
                    R.id.tv_sticky_header_professional);
            tvStickyHeaderDesc = (TextView) itemView.findViewById(R.id.tv_sticky_header_desc);
            tvStickyHeaderDate = (TextView) itemView.findViewById(R.id.tv_sticky_header_date);
        }

        public void setData(int position) {
            StickyHeaderBean.DataEntity.ComingEntity comingEntity = comingEntityList.get(position);
            ImageUtil.loadImage(comingEntity.getImg().replaceAll("w.h", "50.80"), ivStickyHeader);
            tvStickyHeaderName.setText(comingEntity.getNm());
            tvStickyHeaderDesc.setText(comingEntity.getScm());
            tvStickyHeaderDate.setText(comingEntity.getShowInfo());
        }
    }
}
