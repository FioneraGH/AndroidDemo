package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.application.DemoApplication;
import com.fionera.wechatdemo.util.BitmapCache;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class VolleyActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        imageView = (ImageView) findViewById(R.id.iv_volley);

        VolleyGET();
        VolleyPOST();
        VolleyLoader();
        DemoApplication.getRequestQueue().start();
    }

    @Override
    protected void onDestroy() {

        DemoApplication.getRequestQueue().stop();
        DemoApplication.getRequestQueue().cancelAll("testStringGET");
        DemoApplication.getRequestQueue().cancelAll("testStringPOST");
        DemoApplication.getRequestQueue().cancelAll("testJsonGET");
        DemoApplication.getRequestQueue().cancelAll("testJsonPOST");
        DemoApplication.getRequestQueue().cancelAll("testImageRequest");
        super.onDestroy();
    }

    private void VolleyGET() {
        /**
         * 普通请求GET方式
         */
        String url = "http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.d("Volley", s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Volley", volleyError.getCause().toString());
            }
        });
        stringRequest.setTag("testStringGET");
        DemoApplication.getRequestQueue().add(stringRequest);

        /**
         * Json请求GET方式
         */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
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

    private void VolleyPOST() {
        /**
         * 普通请求POST方式
         */
        String url = "http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.d("Volley", s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Volley", volleyError.getCause().toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone", "123456");
                return map;
            }
        };
        stringRequest.setTag("testStringPOST");
        DemoApplication.getRequestQueue().add(stringRequest);

        /**
         * Json请求POST方式
         */
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", "123456");
        JSONObject jsonObject = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject, new Response.Listener<JSONObject>() {
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

    public void VolleyLoader() {

        String url = "http://www.baidu.com/img/bd_logo.png";
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        });
        imageRequest.setTag("testImageRequest");
        DemoApplication.getRequestQueue().add(imageRequest);

        ImageLoader imageLoader = new ImageLoader(DemoApplication.getRequestQueue(),
                new BitmapCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get("http://news.baidu.com/resource/img/logo_news_276_88.png?date=20150104",
                imageListener);
    }

}
