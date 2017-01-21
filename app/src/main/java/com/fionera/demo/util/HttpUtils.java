package com.fionera.demo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.fionera.demo.DemoApplication;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.app.DefaultParamsBuilder;
import org.xutils.x;

public class HttpUtils {

    public static Callback.Cancelable get(String url, RequestParams params, HttpRequestCallBack<String> callBack){
        if (!isNetworkConnected(DemoApplication.getInstance())) {
            ShowToast.show("网络未连接，请检查网络设置！");
            callBack.onNoNetwork();
            return null;
        }
        if (params == null) {
            params = new RequestParams(url);
        }
        LogCat.d("GET URL = " + url);
        LogCat.d("GET PARAMS = " + params.toString());
        return x.http().get(params, callBack);
    }

    public static Callback.Cancelable postJson(String url, JSONObject jsonObject,
            HttpRequestCallBack<String> callBack) {
        if (!isNetworkConnected(DemoApplication.getInstance())) {
            ShowToast.show("网络未连接，请检查网络设置！");
            callBack.onNoNetwork();
            return null;
        }

        String json = "";
        if (jsonObject != null) {
            json = jsonObject.toString();
        }
        LogCat.d("POST_JSON URL = " + url);
        LogCat.d("POST_JSON BODY = " + json);

        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);

        if (!TextUtils.isEmpty(json)) {
            params.setBodyContent(json);
        }
        return x.http().post(params, callBack);
    }

    public static Callback.Cancelable post(RequestParams params,
                                               HttpRequestCallBack<String> callBack) {
        if (!isNetworkConnected(DemoApplication.getInstance())) {
            ShowToast.show("网络未连接，请检查网络设置！");
            callBack.onNoNetwork();
            return null;
        }
        LogCat.d("POST URL = " + params.getUri());
        LogCat.d("POST BODY = " + params.toString());
        return x.http().post(params, callBack);
    }

    /**
     * 判断网络是否连接
     */
    private static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static abstract class HttpRequestCallBack<T>
            implements Callback.CommonCallback<T> {

        private String url;

        protected HttpRequestCallBack(String url) {
            this.url = url;
        }

        public abstract void onSucceed(String json);

        public abstract void onFailed(String reason);

        public abstract void onNoNetwork();

        @Override
        public void onSuccess(T result) {
            LogCat.d("接口 " + url + "\n RESULT = " + result);
            onSucceed(result.toString());
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            LogCat.e("接口 " + url + "\n FAILED = " + ex.getMessage());
            onFailed((ex instanceof HttpException) ? ex.getMessage() : "未知错误");
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    }
}
