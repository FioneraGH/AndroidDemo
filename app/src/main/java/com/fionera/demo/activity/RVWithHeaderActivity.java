package com.fionera.demo.activity;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.fionera.demo.R;
import com.fionera.demo.util.ShowToast;

public class RVWithHeaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvwith_header);

        ImageView imageView = (ImageView) findViewById(R.id.iv_with_header);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToast.show("shoot");
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_with_header);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {

            @Override
             public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
                return new MyViewHolder(
                        LayoutInflater.from(RVWithHeaderActivity.this).inflate(R.layout.rv_recent_session_item, parent, false));
            }
            @Override
            public void onBindViewHolder(MyViewHolder holder,int position) {

            }

            @Override
            public int getItemCount() {
                return 10;
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
