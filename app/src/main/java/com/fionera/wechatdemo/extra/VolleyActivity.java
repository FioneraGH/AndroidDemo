package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.application.DemoApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class VolleyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        VolleyGET();
        VolleyPOST();;
    }

    private void VolleyGET(){
        /**
         * 普通请求GET方式
         */
        String url = "http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("Volley", s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Volley",volleyError.getCause().toString());
            }
        });
        stringRequest.setTag("testStringGET");
        DemoApplication.getRequestQueue().add(stringRequest);

        /**
         * Json请求GET方式
         */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("Volley", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Volley", volleyError.getCause().toString());
            }
        });
        jsonObjectRequest.setTag("testJsonGET");
        DemoApplication.getRequestQueue().add(jsonObjectRequest);
    }

    private void VolleyPOST(){
        /**
         * 普通请求POST方式
         */
        String url = "http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("Volley", s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Volley", volleyError.getCause().toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("phone","123456");
                return map;
            }
        };
        stringRequest.setTag("testStringPOST");
        DemoApplication.getRequestQueue().add(stringRequest);

        /**
         * Json请求POST方式
         */
        Map<String,String> map = new HashMap<String, String>();
        map.put("phone","123456");
        JSONObject jsonObject = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("Volley", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Volley", volleyError.getCause().toString());
            }
        });
        jsonObjectRequest.setTag("testJsonPOST");
        DemoApplication.getRequestQueue().add(jsonObjectRequest);
    }


}
