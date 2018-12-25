package com.fionera.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.bean.StickyHeaderBean;
import com.fionera.multipic.common.ImageUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * StickyHeaderAdapter
 *
 * @author fionera
 * @date 17-1-9 in AndroidDemo
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

    @NonNull
    @Override
    public StickyHeaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickyHeaderHolder(LayoutInflater.from(context)
                .inflate(R.layout.rv_sticky_header_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StickyHeaderHolder holder, int position) {
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
            ivStickyHeader = itemView.findViewById(R.id.iv_sticky_header);
            tvStickyHeaderName = itemView.findViewById(R.id.tv_sticky_header_name);
            tvStickyHeaderAudience = itemView.findViewById(
                    R.id.tv_sticky_header_audience);
            tvStickyHeaderProfessional = itemView.findViewById(
                    R.id.tv_sticky_header_professional);
            tvStickyHeaderDesc = itemView.findViewById(R.id.tv_sticky_header_desc);
            tvStickyHeaderDate = itemView.findViewById(R.id.tv_sticky_header_date);
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
