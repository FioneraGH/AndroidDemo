package com.fionera.demo.weex;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

/**
 * UrlHelperModule
 *
 * @author fionera
 * @date 16-9-22
 */
public class UrlHelperModule
        extends WXModule {
    private static final String WEEX_CATEGORY = "com.taobao.android.intent.category.WEEX";

    @JSMethod
    public void openURL(String url, JSCallback jsCallback){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Uri uri = Uri.parse("http://" + url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(WEEX_CATEGORY);
        mWXSDKInstance.getContext().startActivity(intent);

        jsCallback.invoke(System.currentTimeMillis());
    }
}
