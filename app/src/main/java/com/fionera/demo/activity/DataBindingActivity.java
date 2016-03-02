package com.fionera.demo.activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fionera.demo.R;
import com.fionera.demo.bean.DemoUserBean;
import com.fionera.demo.databinding.ActivityDataBindBinding;
import com.fionera.demo.util.ShowToast;


public class DataBindingActivity
        extends AppCompatActivity {

    private DemoUserBean user;

    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDataBindBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_data_bind);
        user = new DemoUserBean();
        user.setAdult(true);
        user.setFirstName("Hello");
        user.setLastName("World");
        binding.setUser(user);
        imageview = binding.ivDataBindingAnim;

        extractUidFromUri();
    }

    public void changeUser(View v) {
        user.setFirstName("hello");
        imageview.setVisibility(
                imageview.isShown() ? View.GONE : View.VISIBLE);
    }

    private void extractUidFromUri() {
        Uri uri = getIntent().getData();
        if (uri != null && Uri.parse("mxn://profile").getScheme().equals(uri.getScheme())) {
            ShowToast.show(uri.getQueryParameter("uid"));
        }
    }
}
