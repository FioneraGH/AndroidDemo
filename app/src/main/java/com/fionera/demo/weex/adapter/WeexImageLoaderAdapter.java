package com.fionera.demo.weex.adapter;

import android.widget.ImageView;

import com.fionera.multipic.common.ImageUtil;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

/**
 * WeexImageLoaderAdapter
 *
 * @author fionera
 * @date 16-7-11
 */

public class WeexImageLoaderAdapter
        implements IWXImgLoaderAdapter {
    @Override
    public void setImage(final String url, final ImageView view, WXImageQuality quality,
                         WXImageStrategy strategy) {
        WXSDKManager.getInstance().postOnUiThread(() -> ImageUtil.loadImage(url, view), 0);
    }
}
