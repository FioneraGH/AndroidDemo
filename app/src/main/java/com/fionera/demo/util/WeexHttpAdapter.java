package com.fionera.demo.util;

import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;

import org.xutils.http.RequestParams;

import java.util.Map;

/**
 * WeexHttpAdapter
 * Created by fionera on 17-1-21 in AndroidDemo.
 */

public class WeexHttpAdapter
        implements IWXHttpAdapter {
    private final static String HTTP_GET = "GET";
    private final static String HTTP_POST = "POST";
    private final static String HTTP_DELETE = "DELETE";
    private final static String HTTP_PUT = "PUT";

    @Override
    public void sendRequest(WXRequest request, OnHttpListener listener) {
        listener.onHttpStart();
        switch (request.method.toUpperCase()) {
            case HTTP_GET:
                HttpUtils.get(request.url, null,
                        new HttpUtils.HttpRequestCallBack<String>(request.url) {
                            @Override
                            public void onSucceed(String json) {
                                WXResponse response = new WXResponse();
                                response.statusCode = "200";
                                response.data = json;
                                listener.onHttpFinish(response);
                            }

                            @Override
                            public void onFailed(String reason) {
                                WXResponse response = new WXResponse();
                                response.statusCode = "-100";
                                response.errorMsg = reason;
                                listener.onHttpFinish(response);
                            }

                            @Override
                            public void onNoNetwork() {

                            }
                        });
                break;
            case HTTP_POST:
                RequestParams requestParams = new RequestParams(request.url);
                Map<String, String> params = request.paramMap;
                for (String key : params.keySet()) {
                    requestParams.addBodyParameter(key, params.get(key));
                }
                HttpUtils.post(requestParams,
                        new HttpUtils.HttpRequestCallBack<String>(request.url) {
                            @Override
                            public void onSucceed(String json) {
                                WXResponse response = new WXResponse();
                                response.statusCode = "200";
                                response.data = json;
                                listener.onHttpFinish(response);
                            }

                            @Override
                            public void onFailed(String reason) {
                                WXResponse response = new WXResponse();
                                response.statusCode = "-100";
                                response.errorMsg = reason;
                                listener.onHttpFinish(response);
                            }

                            @Override
                            public void onNoNetwork() {

                            }
                        });
                break;
            case HTTP_DELETE:
                break;
            case HTTP_PUT:
                break;
        }
    }
}
