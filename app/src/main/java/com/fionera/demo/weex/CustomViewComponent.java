package com.fionera.demo.weex;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXDomPropConstant;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Created by fionera on 16-9-3.
 */
public class CustomViewComponent
        extends WXComponent {
    public CustomViewComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent,
                               boolean isLazy) {
        super(instance, dom, parent, isLazy);
    }

    @Override
    protected View initComponentHostView(Context context) {
        return new TextView(context);
    }

    @Override
    public View getHostView() {
        return super.getHostView();
    }

    @WXComponentProp(name = WXDomPropConstant.WX_ATTR_INPUT_TYPE_TEXT)
    public void setTextValue(String text) {
        ((TextView) mHost).setText(text);
    }

    @WXComponentProp(name = "size")
    public void setTextSizeValue(int size) {
        ((TextView) mHost).setTextSize(size);
    }
}
