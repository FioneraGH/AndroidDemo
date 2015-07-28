package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.JsonObjectBean;
import com.fionera.wechatdemo.bean.JsonObjectSubBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONActivity extends Activity {

    private Button btnGenJson;
    private Button btnDepJson;
    private TextView tvGenJson;
    private TextView tvDepJson;
    private JsonObjectBean jsonObjectBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        btnGenJson = (Button) findViewById(R.id.btnGenJson);
        btnDepJson = (Button) findViewById(R.id.btnDepJson);
        tvGenJson = (TextView) findViewById(R.id.tvGenJson);
        tvDepJson = (TextView) findViewById(R.id.tvDepJson);

        // 生成JavaBean用于生成Json字符串
        jsonObjectBean = new JsonObjectBean();
        jsonObjectBean.setName("Hello");
        JsonObjectSubBean jsonObjectSubBean1 = new JsonObjectSubBean();
        jsonObjectSubBean1.setSubName("World");
        JsonObjectSubBean jsonObjectSubBean2 = new JsonObjectSubBean();
        jsonObjectSubBean2.setSubName(" !");
        List<JsonObjectSubBean> jsonObjectSubBeanList = new ArrayList<JsonObjectSubBean>();
        jsonObjectSubBeanList.add(jsonObjectSubBean1);
        jsonObjectSubBeanList.add(jsonObjectSubBean2);
        jsonObjectBean.setJsonObjectSubBean(jsonObjectSubBeanList);
        jsonObjectBean.setAge(10);

        btnGenJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                tvGenJson.setText(gson.toJson(jsonObjectBean));
            }
        });

        btnDepJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "";
                try {
                    JSONObject jsonObject = new JSONObject(tvGenJson.getText().toString());

                    String name = jsonObject.getString("name");
                    JSONArray jsonObjectSubBeans = jsonObject.getJSONArray("jsonObjectSubBean");
                    String subName1 =  jsonObjectSubBeans.getJSONObject(0).getString("subName");
                    String subName2 =  jsonObjectSubBeans.getJSONObject(1).getString("subName");
                    int age = jsonObject.getInt("age");
                    info = name + " " + subName1 + " " + subName2 + " " + age;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvDepJson.setText(info);
            }
        });

    }

}
