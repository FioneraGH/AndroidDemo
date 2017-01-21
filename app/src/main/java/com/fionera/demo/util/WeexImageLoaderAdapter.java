package com.fionera.demo.util;

import android.widget.ImageView;

import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

import org.xutils.x;

/**
 * WeexImageLoaderAdapter
 * Created by fionera on 16-7-11.
 */

public class WeexImageLoaderAdapter
        implements IWXImgLoaderAdapter {
    @Override
    public void setImage(final String url, final ImageView view, WXImageQuality quality,
                         WXImageStrategy strategy) {
        WXSDKManager.getInstance().postOnUiThread(new Runnable() {
            @Override
            public void run() {
                x.image().bind(view, url);
            }
        }, 0);
    }
}
