package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.adapter.ExpandableAdapter;

public class ExpandableListViewActivity extends Activity {


    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);

        expandableListView = (ExpandableListView) findViewById(R.id.expand_list_view);

        String[] group = new String[]{"ABC", "DEF", "GHI"};
        String[][] child = new String[][]{
                {"111",},
                {"444",},
                {"777",},
        };

        expandableListAdapter = new ExpandableAdapter(this, group, child,expandableListView);
        expandableListView.setAdapter(expandableListAdapter);


        expandableListView.setOnGroupExpandListener(
                new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {

                    }
                }
        );
        expandableListView.setOnGroupCollapseListener(
                new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {

                    }
                }
        );

    }

}
