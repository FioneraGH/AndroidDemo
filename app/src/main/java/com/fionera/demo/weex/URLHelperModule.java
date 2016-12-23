package com.fionera.demo.weex;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.taobao.weex.bridge.JSCallback;
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

    @WXModuleAnno
    public void openURL(String url, JSCallback jsCallback){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Uri uri = Uri.parse("http:" + url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(WEEX_CATEGORY);
        mWXSDKInstance.getContext().startActivity(intent);

        jsCallback.invoke(System.currentTimeMillis());
    }
}
