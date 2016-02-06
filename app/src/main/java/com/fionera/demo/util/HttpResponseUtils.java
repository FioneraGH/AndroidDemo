package com.fionera.demo.util;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpResponseUtils {

    public static boolean hasGetData(String result) {
        int statusCode;
        try {
            JSONObject object = new JSONObject(result);
            if (object.has("statusCode")) {
                statusCode = object.getInt("statusCode");
            } else {
                statusCode = 404;
            }
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
                if (object.has("statusMsg")) {
                    statusMsg = object.getString("statusMsg");
                } else {
                    statusMsg = "数据异常，服务器发生未知错误";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            statusMsg = "数据异常，服务器发生未知错误";
        }
        return statusMsg;
    }
}
