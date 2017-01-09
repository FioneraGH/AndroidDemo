package com.fionera.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.bean.StickyHeaderBean;
import com.fionera.demo.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by fionera on 17-1-9 in AndroidDemo.
 */

public class SectionDecoration
        extends RecyclerView.ItemDecoration {

    private Context context;
    private List<StickyHeaderBean.DataEntity.ComingEntity> comingEntityList = new ArrayList<>();

    private DecorationCallback callback;

    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private int alignBottom;
    private Paint.FontMetrics fontMetrics;

    public SectionDecoration(Context context, List<StickyHeaderBean.DataEntity.ComingEntity> comingEntityList,
                             DecorationCallback callback) {
        this.context = context;
        this.comingEntityList = comingEntityList;
        this.callback = callback;

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.grey2));

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DisplayUtils.sp2px(14f));
        textPaint.setColor(ContextCompat.getColor(context,R.color.black1));
        textPaint.setTextAlign(Paint.Align.LEFT);

        fontMetrics = new Paint.FontMetrics();
        topGap = DisplayUtils.dp2px(24);
        alignBottom = DisplayUtils.dp2px(8);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        String groupId = callback.getGroupId(position);
        if (TextUtils.equals(groupId, "-1")) {
            return;
        }
        if (0 == position || isFirstInGroup(position)) {
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }
    }

    private boolean isFirstInGroup(int position) {
        if (0 == position) {
            return true;
        } else {
            String preGroupId = callback.getGroupId(position - 1);
            String groupId = callback.getGroupId(position);
            return !TextUtils.equals(preGroupId, groupId);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            String groupId = callback.getGroupId(position);
            if (TextUtils.equals(groupId, "-1")) {
                return;
            }
            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) {
                c.drawRect(left, view.getTop(), right, view.getTop(), paint);
            } else {
                if (0 == position || isFirstInGroup(position)) {
                    c.drawRect(left, view.getTop() - topGap, right, view.getTop(), paint);
                    c.drawText(textLine, left, view.getTop() - DisplayUtils.dp2px(4), textPaint);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        float lineHeight = textPaint.getTextSize() + fontMetrics.descent;

        String preGroupId = "";
        String groupId = "-1";
        for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (TextUtils.equals(groupId, "-1") || TextUtils.equals(preGroupId, groupId)) {
                continue;
            }

            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) {
                continue;
            }

            int viewBottom = view.getBottom();
            float textY = Math.max(topGap, view.getTop());

            if (position + 1 < itemCount) {
                String nextGroupId = callback.getGroupId(position + 1);
                if (!TextUtils.equals(nextGroupId, groupId) && view.getBottom() < textY) {
                    textY = viewBottom;
                }
            }

            c.drawRect(left, textY - topGap, right, textY, paint);
            c.drawText(textLine, left + 2 * alignBottom, textY - alignBottom, textPaint);
        }
    }

    public interface DecorationCallback {
        String getGroupId(int position);
        String getGroupFirstLine(int position);
    }
}
