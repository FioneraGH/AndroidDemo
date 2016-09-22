package com.fionera.demo.weex;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fionera on 16-9-22.
 */
public class URLHelperModule
        extends WXModule {
    private static final String WEEX_CATEGORY = "com.taobao.android.intent.category.WEEX";

    @WXModuleAnno()
    public void openURL(String url, String callbackId){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Uri uri = Uri.parse("http:" + url);

        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.addCategory(WEEX_CATEGORY);
        mWXSDKInstance.getContext().startActivity(intent);

        Map<String, Object> result = new HashMap<>();
        result.put("ts", System.currentTimeMillis());
        WXBridgeManager.getInstance().callback(mWXSDKInstance.getInstanceId(), callbackId, result);
    }
}
