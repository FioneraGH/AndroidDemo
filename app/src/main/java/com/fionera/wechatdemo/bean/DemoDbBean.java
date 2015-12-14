package com.fionera.wechatdemo.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by fionera on 15-12-6.
 */
@Table(name = "demoTbl")
public class DemoDbBean {

    /**
     * 这个属性是id，但id不是自动生成的
     */
    @Column(name = "id", isId = true, autoGen = false)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + age;
    }
}
