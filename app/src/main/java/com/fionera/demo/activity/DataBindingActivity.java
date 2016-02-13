package com.fionera.demo.activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fionera.demo.R;
import com.fionera.demo.bean.DemoUserBean;
import com.fionera.demo.databinding.ActivityDataBindingBinding;
import com.fionera.demo.util.ShowToast;


public class DataBindingActivity
        extends AppCompatActivity {

    private DemoUserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDataBindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        user = new DemoUserBean();
        user.setAdult(true);
        user.setFirstName("Hello");
        user.setLastName("World");
        binding.setUser(user);

        extractUidFromUri();
    }

    public void changeUser(View v) {
        user.setFirstName("hello");
    }

    private void extractUidFromUri() {
        Uri uri = getIntent().getData();
        if (uri != null && Uri.parse("mxn://profile").getScheme().equals(uri.getScheme())) {
            ShowToast.show(uri.getQueryParameter("uid"));
        }
    }
}
