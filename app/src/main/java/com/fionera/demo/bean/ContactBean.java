package com.fionera.demo.bean;

import android.databinding.ObservableField;

import kotlin.properties.ObservableProperty;

/**
 * Created by fionera on 16-2-6.
 */
public class ContactBean {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();
}
