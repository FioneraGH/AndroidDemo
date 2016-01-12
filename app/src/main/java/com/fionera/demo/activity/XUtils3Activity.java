package com.fionera.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.fionera.demo.R;
import com.fionera.demo.bean.DemoDbBean;
import com.fionera.demo.util.HttpRequestCallBack;
import com.fionera.demo.util.HttpUtils;
import com.fionera.demo.util.LogUtils;
import com.fionera.demo.util.ShowToast;

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
public class XUtils3Activity extends Activity {

    private Context mContext = this;
    /**
     * View绑定控制反转
     */
    @ViewInject(R.id.iv_xutils)
    private ImageView ivXUtils;

    /**
     * 事件驱动
     *
     * @param v
     */
    @Event({R.id.btn_xutils, R.id.iv_xutils})
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
    private String temp = "";

    private void dbOp() throws DbException {

        demoDbBeanList.clear();
        temp = "";

        /**
         * 设定关系对象映射
         */
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbDir(
                new File(mContext.getFilesDir().getAbsolutePath() + "/")).setDbName(
                "demo.db").setDbVersion(1).setDbUpgradeListener(
                (db, oldVersion, newVersion) -> ShowToast.show(
                        String.format("upgrade from %d to %d", oldVersion,
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
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_ALARMS).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PODCASTS).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_RINGTONES).getAbsolutePath());

        LogUtils.d(Environment.getExternalStorageDirectory().getAbsolutePath());
        LogUtils.d(Environment.getDataDirectory().getAbsolutePath());
        LogUtils.d(Environment.getDownloadCacheDirectory().getAbsolutePath());
        LogUtils.d(Environment.getRootDirectory().getAbsolutePath());

        LogUtils.d(mContext.getExternalFilesDir(
                Environment.DIRECTORY_ALARMS).getAbsolutePath());
        LogUtils.d(mContext.getExternalCacheDir().getAbsolutePath());

        LogUtils.d(mContext.getFilesDir().getAbsolutePath());
        LogUtils.d(mContext.getCacheDir().getAbsolutePath());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            LogUtils.d(mContext.getNoBackupFilesDir().getAbsolutePath());
        }

        LogUtils.d(mContext.getObbDir().getAbsolutePath());

        btnClick(null);
        /**
         * 图片加载
         */
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(
                ImageView.ScaleType.CENTER_CROP).build();

        x.image().bind(ivXUtils, "https://ss0.bdstatic" + "" +
                        ".com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png",
                imageOptions);

        HttpUtils.request(HttpMethod.POST, "", null, new HttpRequestCallBack<String>() {
            @Override
            public void onSucceed(String result) {

                LogUtils.d(result);
            }

            @Override
            public void onFailed(String reason) {

            }

            @Override
            public void onNoNetwork() {

            }

        });
    }
}
