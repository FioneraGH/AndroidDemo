package com.fionera.demo.activity;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.fionera.demo.R;
import com.fionera.demo.util.ShowToast;
import com.fionera.demo.weex.CustomViewComponent;
import com.fionera.demo.weex.URLHelperModule;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;
import com.taobao.weex.utils.WXViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeexActivity
        extends AppCompatActivity {

    private static String CURRENT_IP = "10.42.0.1";
    private static final String WEEX_INDEX_URL = "http://" + CURRENT_IP +
            ":12580/examples/build/index.js";

    private LinearLayout viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weex);

        viewGroup = (LinearLayout) findViewById(R.id.activity_weex);

        WXSDKInstance mInstance = new WXSDKInstance(this);
        mInstance.registerRenderListener(new IWXRenderListener() {
            @Override
            public void onViewCreated(WXSDKInstance instance, View view) {
                viewGroup.addView(view);
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
        renderPage(mInstance, getPackageName(), WXFileUtils.loadAsset("hello_weex.js", this),
                WEEX_INDEX_URL, "{\"os\":\"android\"}");
    }

    private void renderPage(WXSDKInstance mInstance, String packageName, String template,
                            String source, String jsonInitData) {
        Map<String, Object> options = new HashMap<>();
//        options.put(WXSDKInstance.BUNDLE_URL, source);
        mInstance.render(packageName, template, options, jsonInitData,
                WXViewUtils.getScreenWidth(this), WXViewUtils.getScreenHeight(this),
                WXRenderStrategy.APPEND_ASYNC);
    }
}
