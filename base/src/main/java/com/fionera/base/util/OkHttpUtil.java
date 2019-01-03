package com.fionera.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.fionera.base.AppContextHolder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttpUtil
 *
 * @author fionera
 * @date 16-4-25
 */
public class OkHttpUtil {
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder().readTimeout(25,
            TimeUnit.SECONDS).addInterceptor(new LogInterceptor()).build();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void getEnqueue(String url, final NetCallBack responseCallback, Object tag) {
        try {
            getInnerEnqueue(url, responseCallback, tag);
        } catch (Exception e) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    responseCallback.onFailed("网络错误");
                }
            });
        }
    }

    /**
     * 开启异步线程访问网络
     */
    private static void getInnerEnqueue(String url, final NetCallBack responseCallback,
                                        final Object tag) throws Exception {
        if (isNetworkNotConnected(AppContextHolder.getAppContext())) {
            onNoNetwork(responseCallback);
            return;
        }
        Request request = new Request.Builder().url(url).tag(tag).build();
        OK_HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (TextUtils.equals(e.getMessage(), "Canceled") || TextUtils.equals(e.getMessage(),
                        "Socket closed")) {
                    return;
                }
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCallback.onFailed("网络超时");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCallback.onSucceed(result);
                    }
                });
            }
        });
    }

    public static void postEnqueue(String url, Map<String, String> params,
                                   final NetCallBack responseCallback, Object tag) {
        try {
            postInnerEnqueue(url, params, responseCallback, tag);
        } catch (Exception e) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    responseCallback.onFailed("网络错误");
                }
            });
        }
    }

    /**
     * 开启异步线程访问网络
     */
    private static void postInnerEnqueue(String url, final Map<String, String> params,
                                         final NetCallBack responseCallback,
                                         Object tag) throws Exception {
        if (isNetworkNotConnected(AppContextHolder.getAppContext())) {
            onNoNetwork(responseCallback);
            return;
        }
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        FormBody formBody = formBuilder.build();
        Request request = new Request.Builder().url(url).tag(tag).post(formBody).build();
        OK_HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCallback.onFailed("网络超时");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCallback.onSucceed(result);
                    }
                });
            }
        });
    }

    private static void parseResponse(Response response,
                                      final NetCallBack responseCallback) throws IOException {
        final String result = response.body().string();
        String returnCode = "404";
        String returnMsg = "网络超时";
        try {
            returnCode = new JSONObject(result).getString("returnCode");
            returnMsg = new JSONObject(result).getString("returnMsg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String finalMsg = returnMsg;
        if ("200".equals(returnCode)) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    responseCallback.onSucceed(result);
                }
            });
        } else {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    responseCallback.onFailed(finalMsg);
                }
            });
        }
    }

    public static void cancelCallWithTag(Object tag) {
        for (Call call : OK_HTTP_CLIENT.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : OK_HTTP_CLIENT.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    private static void onNoNetwork(NetCallBack callBack) {
        if (callBack instanceof NoNetCallBack) {
            ((NoNetCallBack) callBack).onNoNetwork();
        } else {
            callBack.onFailed("网络未连接，请检查网络设置");
        }
    }

    private static boolean isNetworkNotConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                // TODO: L+ is always true
                return !mNetworkInfo.isAvailable();
            }
        }
        return true;
    }

    public interface NetCallBack {
        void onSucceed(String json);

        void onFailed(String reason);
    }

    public interface NoNetCallBack
            extends NetCallBack {
        void onNoNetwork();
    }
}
