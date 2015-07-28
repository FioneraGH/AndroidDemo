package com.fionera.wechatdemo.bean;

import java.util.List;

/**
 * Created by fionera on 15-7-28.
 */
public class JsonObjectBean {

    private String name;
    private List<JsonObjectSubBean> jsonObjectSubBean;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JsonObjectSubBean> getJsonObjectSubBean() {
        return jsonObjectSubBean;
    }

    public void setJsonObjectSubBean(List<JsonObjectSubBean> jsonObjectSubBean) {
        this.jsonObjectSubBean = jsonObjectSubBean;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
