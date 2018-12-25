package com.fionera.demo.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(LayoutInflater.from(RvWithHeaderActivity.this)
                        .inflate(R.layout.rv_recent_session_item, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

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
