package com.fionera.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fionera.demo.R;
import com.fionera.base.util.ShowToast;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.util.HashMap;
import java.util.Map;

public class WeexActivity
        extends AppCompatActivity {

    private static String CURRENT_IP = "10.42.0.1";
    private static final String WEEX_INDEX_URL = "http://" + CURRENT_IP +
            ":12580/examples/build/index.js";

    private WXSDKInstance mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weex);

        mInstance = new WXSDKInstance(this);
        mInstance.registerRenderListener(new IWXRenderListener() {
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
            }
        });
        renderPage(mInstance, getPackageName(), WXFileUtils.loadAsset("app.weex.js", this),
                WEEX_INDEX_URL, "{\"os\":\"android\"}");
    }

    private void renderPage(WXSDKInstance mInstance, String packageName, String template,
                            String source, String jsonInitData) {
        Map<String, Object> options = new HashMap<>();
//        options.put(WXSDKInstance.BUNDLE_URL, source);
        mInstance.render(packageName, template, options, jsonInitData, WXRenderStrategy.APPEND_ASYNC);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mInstance.onActivityStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInstance.onActivityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mInstance.onActivityPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mInstance.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstance.onActivityDestroy();
    }
}
