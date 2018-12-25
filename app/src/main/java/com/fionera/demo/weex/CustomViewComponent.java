package com.fionera.demo.weex;

import android.content.Context;
import android.widget.TextView;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

import androidx.annotation.NonNull;

/**
 * @author fionera
 * @date 16-9-3
 */
public class CustomViewComponent
        extends WXComponent<TextView> {

    public CustomViewComponent(WXSDKInstance instance, WXVContainer parent,
                               BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected TextView initComponentHostView(@NonNull Context context) {
        return new TextView(context);
    }

    @WXComponentProp(name = Constants.Value.TEXT)
    public void setTextValue(String text) {
        ((TextView) getRealView()).setText(text);
    }

    @WXComponentProp(name = "size")
    public void setTextSizeValue(int size) {
        ((TextView) getRealView()).setTextSize(size);
    }
}
