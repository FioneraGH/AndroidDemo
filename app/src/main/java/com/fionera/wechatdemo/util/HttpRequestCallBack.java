package com.fionera.wechatdemo.util;

import org.xutils.common.Callback;

public abstract class HttpRequestCallBack<T> implements Callback.CommonCallback<T> {

    public abstract void onSucceed(T result);

    public abstract void onFailed(Throwable ex, boolean isOnCallback);

    @Override
    public void onSuccess(T result) {
        this.onSucceed(result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        this.onFailed(ex, isOnCallback);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

}