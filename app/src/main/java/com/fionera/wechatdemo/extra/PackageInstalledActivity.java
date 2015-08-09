package com.fionera.wechatdemo.extra;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fionera on 15-8-9.
 */

public class PackageInstalledActivity extends ListActivity implements AdapterView
        .OnItemClickListener {


    private List<String> packageInfo = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(
                PackageManager.GET_ACTIVITIES);
        for (PackageInfo packageInf : packageInfos) {
            packageInfo.add(packageInf.packageName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R
                .id.text1, packageInfo);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent();
        intent.putExtra("package_name", packageInfo.get(position));
        setResult(1, intent);
        finish();
    }
}
