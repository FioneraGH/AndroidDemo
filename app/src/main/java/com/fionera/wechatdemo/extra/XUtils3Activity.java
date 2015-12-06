package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.util.HttpRequestCallBack;
import com.fionera.wechatdemo.util.HttpUtil;
import com.fionera.wechatdemo.util.LogUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 内容视图加载
 */
@ContentView(R.layout.activity_xutils)
public class XUtils3Activity extends Activity {

    /**
     * View绑定控制反转
     */
    @ViewInject(R.id.btn_xutils)
    private Button btnXUtils;
    @ViewInject(R.id.iv_xutils)
    private ImageView ivXUtils;

    /**
     * 事件驱动
     *
     * @param v
     */
    @Event(R.id.btn_xutils)
    private void btnClick(View v) {

        Toast.makeText(XUtils3Activity.this, v.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);

        /**
         * 图片加载
         */
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(
                ImageView.ScaleType.CENTER_CROP).build();

        x.image().bind(ivXUtils, "https://ss0.bdstatic" + "" +
                        ".com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png",
                imageOptions);

        HttpUtil.sendJsonRequest(HttpUtil.HTTP_POST, "http://console.awu.cn/api/1.html",
                "", new HttpRequestCallBack<String>() {
                    @Override
                    public void onSucceed(String result) {

                        LogUtils.d(result);
                    }

                    @Override
                    public void onFailed(Throwable ex, boolean isOnCallback) {

                        LogUtils.d(isOnCallback + " " + ex.toString());
                    }
                });
    }
}
