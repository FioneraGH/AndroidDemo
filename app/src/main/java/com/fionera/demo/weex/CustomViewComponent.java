package com.fionera.demo.weex;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Created by fionera on 16-9-3.
 */
public class CustomViewComponent
        extends WXComponent<TextView> {

    public CustomViewComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent,
                               boolean isLazy) {
        super(instance, dom, parent, isLazy);
    }

    @Override
    protected TextView initComponentHostView(@NonNull Context context) {
        return new TextView(context);
    }

    @WXComponentProp(name = Constants.Value.TEXT)
    public void setTextValue(String text) {
        mHost.setText(text);
    }

    @WXComponentProp(name = "size")
    public void setTextSizeValue(int size) {
        mHost.setTextSize(size);
    }
}
