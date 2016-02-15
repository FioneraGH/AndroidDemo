package com.fionera.demo.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fionera.demo.R;
import com.fionera.demo.bean.DemoDbBean;
import com.fionera.demo.util.HttpRequestCallBack;
import com.fionera.demo.util.HttpUtils;
import com.fionera.demo.util.LogCat;
import com.fionera.demo.util.ShowToast;
import com.fionera.demo.view.ViewDragLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 内容视图加载
 */
@ContentView(R.layout.activity_xutils)
public class XUtils3Activity
        extends AppCompatActivity {

    private Context mContext = this;

    @ViewInject(R.id.vdl_toggle)
    private ViewDragLayout viewDragLayout;

    /**
     * 事件驱动
     *
     * @param v
     */
    @Event({R.id.btn_xutils})
    private void btnClick(View v) {

        if (v == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_xutils:
                try {
                    dbOp();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private List<DemoDbBean> demoDbBeanList;

    private void dbOp() throws DbException {

        demoDbBeanList.clear();
        String temp = "";

        /**
         * 设定关系对象映射
         */
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbDir(new File(mContext.getFilesDir().getAbsolutePath() + "/"))
                .setDbName("demo.db").setDbVersion(1).setDbUpgradeListener(
                        (db, oldVersion, newVersion) -> ShowToast
                                .show(String.format("upgrade from %d to %d", oldVersion,
                                                    newVersion))).setAllowTransaction(true);

        /**
         * 获取数据库管理器
         */
        DbManager db = x.getDb(daoConfig);
        DemoDbBean demoDbBean = new DemoDbBean();
        demoDbBean.setId(1);
        demoDbBean.setName("hah");
        demoDbBean.setAge(10);
        db.saveOrUpdate(demoDbBean);

        demoDbBean = db.selector(DemoDbBean.class).findFirst();
        demoDbBeanList.add(demoDbBean);

        demoDbBean.setName("world");
        demoDbBeanList.add(demoDbBean);

        db.saveOrUpdate(demoDbBean);
        demoDbBean = db.selector(DemoDbBean.class).findFirst();
        demoDbBeanList.add(demoDbBean);

        for (DemoDbBean dbBean : demoDbBeanList) {
            temp += dbBean.toString() + "\n";
        }
        ShowToast.show(temp);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        demoDbBeanList = new ArrayList<>();

        /**
         * 获取用户绝对目录
         */
        LogCat.d(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                         .getAbsolutePath());

        LogCat.d(Environment.getExternalStorageDirectory().getAbsolutePath());
        LogCat.d(Environment.getDownloadCacheDirectory().getAbsolutePath());

        LogCat.d(mContext.getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath());
        LogCat.d(mContext.getExternalCacheDir().getAbsolutePath());

        LogCat.d(mContext.getFilesDir().getAbsolutePath());
        LogCat.d(mContext.getCacheDir().getAbsolutePath());

        LogCat.d(mContext.getObbDir().getAbsolutePath());

        btnClick(null);
    }

    public void fuck(View v){
        if("open".equals(v.getTag())) {
            viewDragLayout.openDrawer();
        }else{
            viewDragLayout.closeDrawer();
        }
    }
}
