package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;

/**
 * @author fionera
 */
public class RvWithHeaderActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvwith_header);

        ImageView imageView = findViewById(R.id.iv_with_header);

        imageView.setOnClickListener(view -> ShowToast.show("shoot"));

        RecyclerView recyclerView = findViewById(R.id.rv_with_header);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {

            @Override
             public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
                return new MyViewHolder(
                        LayoutInflater.from(RvWithHeaderActivity.this).inflate(R.layout.rv_recent_session_item, parent, false));
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