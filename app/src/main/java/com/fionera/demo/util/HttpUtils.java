package com.fionera.demo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fionera.demo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class HttpUtils {

    public static Callback.Cancelable request(HttpMethod method, String url, JSONObject jsonObject,
            HttpRequestCallBack<String> callBack) {

        String json = "";
        if (method.equals(HttpMethod.POST) && jsonObject != null) {
            try {
                json = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtils.d("接口 URL = " + url);
        LogUtils.d("接口 JSON = " + json);

        if (!HttpUtils.isNetworkConnected(DemoApplication.getInstance())) {
            ShowToast.show("网络未连接，请检查网络设置！");
            callBack.onNoNetwork();
            return null;
        }

        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);

        try {
            if (json != null) {
                params.setBodyContent(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (method == HttpMethod.POST) {
            return x.http().post(params, callBack);
        }

        if (method == HttpMethod.GET) {
            return x.http().get(params, callBack);
        }

        return null;
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
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

}
