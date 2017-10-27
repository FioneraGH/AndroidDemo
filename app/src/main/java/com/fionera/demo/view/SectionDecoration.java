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

import com.fionera.base.util.DisplayUtils;
import com.fionera.base.util.LogCat;
import com.fionera.demo.R;
import com.fionera.demo.bean.StickyHeaderBean;

import java.util.List;
import java.util.Locale;

/**
 * Sticker Header Decoration
 *
 * @author fionera
 * @date 17-1-9 in AndroidDemo
 */

public class SectionDecoration
        extends RecyclerView.ItemDecoration {

    private DecorationCallback callback;

    private TextPaint textPaint;
    private TextPaint textPaintOver;
    private Paint paint;
    private Paint paintOver;
    private int topGap;
    private int alignBottom;

    public SectionDecoration(Context context, List<StickyHeaderBean.DataEntity.ComingEntity> comingEntityList,
                             DecorationCallback callback) {
        this.callback = callback;

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.grey2));

        paintOver = new Paint();
        paintOver.setColor(ContextCompat.getColor(context, R.color.blue2));

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DisplayUtils.sp2px(14f));
        textPaint.setColor(ContextCompat.getColor(context,R.color.blue2));
        textPaint.setTextAlign(Paint.Align.LEFT);

        textPaintOver = new TextPaint();
        textPaintOver.setAntiAlias(true);
        textPaintOver.setTextSize(DisplayUtils.sp2px(14f));
        textPaintOver.setColor(ContextCompat.getColor(context,R.color.black1));
        textPaintOver.setTextAlign(Paint.Align.LEFT);

        topGap = DisplayUtils.dp2px(30);
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
                c.drawRect(left, view.getTop() - topGap / 2, right, view.getTop(), paint);
            } else {
                if (0 == position || isFirstInGroup(position)) {
                    c.drawRect(left, view.getTop() - topGap, right, view.getTop(), paint);
                    c.drawText(textLine, left, view.getTop() - DisplayUtils.dp2px(8), textPaint);
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

        String preGroupId;
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

            float textY = Math.max(topGap, view.getTop());

            if (position + 1 < itemCount) {
                String nextGroupId = callback.getGroupId(position + 1);
                LogCat.d(String.format(Locale.CHINA, "Draw Over Current Position:%d %s:%s:%s",
                        position, preGroupId, groupId, nextGroupId));
                if (!TextUtils.equals(nextGroupId, groupId) && view.getBottom() < textY) {
                    textY = view.getBottom();
                }
            }

            c.drawRect(left, textY - topGap, right, textY, paintOver);
            c.drawText(textLine, left + 2 * alignBottom, textY - alignBottom, textPaintOver);
        }
    }

    public interface DecorationCallback {
        String getGroupId(int position);
        String getGroupFirstLine(int position);
    }
}
