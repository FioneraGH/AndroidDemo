package com.fionera.wechatdemo.util;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;

public abstract class HttpRequestCallBack<T> implements Callback.CommonCallback<T> {

    public abstract void onSucceed(String json);

    public abstract void onFailed(String reason);

    public abstract void onNoNetwork();

    @Override
    public void onSuccess(T result) {
        LogUtils.d("接口 RESULT " + result.toString());
        if (HttpResponseUtils.hasGetData(result.toString())) {
            this.onSucceed(result.toString());
        } else {
            if (result.toString().contains("<html>")) {
                this.onFailed("网络未连接，请检查网络设置");
            } else {
                this.onFailed(HttpResponseUtils.getStatusMsg(result.toString()));
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtils.e(ex.getMessage());
        this.onFailed((ex instanceof HttpException) ? ex.getMessage() : "未知错误");
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

}