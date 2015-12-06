package com.fionera.wechatdemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.fionera.wechatdemo.DemoApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class HttpUtil {

    public static String HTTP_GET = "GET";
    public static String HTTP_POST = "POST";

    public static <T> Callback.Cancelable sendJsonRequest(String method, String url, String json,
            HttpRequestCallBack<T> callBack) {

        Log.i("接口 url = ", url);
        Log.i("接口 json = ", json);

        if (!HttpUtil.isNetworkConnected(DemoApplication.getInstance())) {
            Toast.makeText(DemoApplication.getInstance(), "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show();
            return null;
        }

        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setConnectTimeout(0);

        try {
            if (json != null) {
                params.setBodyContent(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return method.equals(HTTP_POST) ? x.http().post(params, callBack) : x.http().get(params,
                callBack);

    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
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
