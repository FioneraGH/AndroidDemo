package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.DemoDbBean;
import com.fionera.wechatdemo.util.HttpRequestCallBack;
import com.fionera.wechatdemo.util.HttpUtil;
import com.fionera.wechatdemo.util.LogUtils;
import com.fionera.wechatdemo.util.ShowToast;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
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

    private void dbOp() throws DbException {

        /**
         * 设定关系对象映射
         */
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbDir(
                new File("")).setDbVersion(1).setDbUpgradeListener(
                new DbManager.DbUpgradeListener() {

                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        ShowToast.show(
                                String.format("upgrade from %d to %d", oldVersion, newVersion));
                    }
                }).setAllowTransaction(true);

        /**
         * 获取数据库管理器
         */
        DbManager db = x.getDb(daoConfig);
        DemoDbBean demoDbBean = new DemoDbBean();
        demoDbBean.setId(1);
        demoDbBean.setName("hello");
        demoDbBean.setAge(10);
        db.save(demoDbBean);

        demoDbBeanList = new ArrayList<>();
        demoDbBeanList = db.selector(DemoDbBean.class).findAll();
        demoDbBean.setName("world");
        demoDbBean = db.selector(DemoDbBean.class).findFirst();
        demoDbBeanList.add(demoDbBean);
        db.saveOrUpdate(demoDbBean);
        demoDbBean = db.selector(DemoDbBean.class).findFirst();
        demoDbBeanList.add(demoDbBean);

        String temp = "";
        for (DemoDbBean dbBean : demoDbBeanList) {
            temp += dbBean.toString() + "\n";
        }
        ShowToast.show(temp);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);

        /**
         * 获取用户绝对目录
         */
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_ALARMS).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath());
        LogUtils.d(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
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

        LogUtils.d(XUtils3Activity.this.getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath());
        LogUtils.d(XUtils3Activity.this.getExternalCacheDir().getAbsolutePath());

        LogUtils.d(XUtils3Activity.this.getFilesDir().getAbsolutePath());
        LogUtils.d(XUtils3Activity.this.getCacheDir().getAbsolutePath());
        LogUtils.d(XUtils3Activity.this.getNoBackupFilesDir().getAbsolutePath());

        LogUtils.d(XUtils3Activity.this.getObbDir().getAbsolutePath());

        btnClick(null);
        /**
         * 图片加载
         */
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(
                ImageView.ScaleType.CENTER_CROP).build();

        x.image().bind(ivXUtils, "https://ss0.bdstatic" + "" +
                        ".com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png",
                imageOptions);

        HttpUtil.sendJsonRequest(HttpUtil.HTTP_POST, "", "", new HttpRequestCallBack<String>() {
            @Override
            public void onSucceed(String result) {

                LogUtils.d(result);
            }

            @Override
            public void onFailed(Throwable ex, boolean isOnCallback) {

                if (ex instanceof HttpException) {
                    LogUtils.d(((HttpException) ex).getCode() + " " + ex.getMessage());
                }
            }
        });
    }
}
