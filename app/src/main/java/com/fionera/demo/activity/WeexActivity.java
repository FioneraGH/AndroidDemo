package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fionera.base.util.LogCat;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fionera
 */
public class WeexActivity
        extends AppCompatActivity
        implements IWXRenderListener {

    private static String CURRENT_IP = "10.42.0.1";
    private static final String WEEX_INDEX_URL = "http://" + CURRENT_IP +
            ":12580/examples/build/index.js";

    private WXSDKInstance mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weex);

        mInstance = new WXSDKInstance(this);
        mInstance.registerRenderListener(this);
        renderPage(mInstance, getPackageName(), WXFileUtils.loadAsset("app.weex.js", this),
                WEEX_INDEX_URL);
    }

    private void renderPage(WXSDKInstance mInstance, String packageName, String template,
                            String source) {
        Map<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, source);
        mInstance.render(packageName, template, options, "{\"os\":\"android\"}",
                WXRenderStrategy.APPEND_ASYNC);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        setContentView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        ShowToast.show("render failed");
        LogCat.d(msg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mInstance != null) {
            mInstance.onActivityStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInstance != null) {
            mInstance.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mInstance != null) {
            mInstance.onActivityPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mInstance != null) {
            mInstance.onActivityStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInstance != null) {
            mInstance.onActivityDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (mInstance != null) {
            if (!mInstance.onBackPressed()) {
                finish();
            }
        }
    }
}
