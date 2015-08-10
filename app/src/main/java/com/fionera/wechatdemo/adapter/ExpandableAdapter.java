package com.fionera.wechatdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fionera.wechatdemo.R;


/**
 * Created by fionera on 15-8-10.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String[] group;
    private String[][] child;
    private LayoutInflater layoutInflater;

    private ExpandableListView expandableListView;
    private int lastExpandedGroupPosition = 0;

    public ExpandableAdapter(Context context, String[] group, String[][] child,
            ExpandableListView expandableListView) {

        this.context = context;
        this.group = group;
        this.child = child;
        this.layoutInflater = LayoutInflater.from(context);

        this.expandableListView = expandableListView;
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.expandable_group_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_expand_group_name);
        textView.setText(getGroup(groupPosition).toString());

        if (isExpanded) {
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.expandable_child_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_expand_child_title);
        Button button = (Button) convertView.findViewById(R.id.btn_expand_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "This is group " + groupPosition + " child " + childPosition,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        if (groupPosition != lastExpandedGroupPosition) {
            expandableListView.collapseGroup(lastExpandedGroupPosition);
        }
        lastExpandedGroupPosition = groupPosition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
