package com.fionera.wechatdemo.util;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpResponseUtils {

    /**
     * 根据返回的JSON状态码判断请求
     */
    public static boolean hasGetData(String result) {
        int statusCode;
        try {
            JSONObject object = new JSONObject(result);
            statusCode = object.getInt("statusCode");
        } catch (JSONException e) {
            e.printStackTrace();
            statusCode = 404;
        }
        return (statusCode == 200);
    }

    public static String getStatusMsg(String result) {
        String statusMsg = "";
        try {
            if (result != null) {
                JSONObject object = new JSONObject(result);
                statusMsg = object.getString("statusMsg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            statusMsg = "数据异常，服务器发生未知错误";
        }
        System.out.println("--------------------");
        return statusMsg;
    }
}
