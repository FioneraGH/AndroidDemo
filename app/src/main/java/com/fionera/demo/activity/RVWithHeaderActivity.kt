package com.fionera.demo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fionera.demo.R
import com.fionera.demo.util.ShowToast
import kotlinx.android.synthetic.main.activity_rvwith_header.*

class RVWithHeaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rvwith_header)

        iv_with_header.setOnClickListener({ShowToast.show("shoot")})
        rv_with_header.layoutManager = LinearLayoutManager(this@RVWithHeaderActivity, LinearLayoutManager.VERTICAL, false)
        rv_with_header.adapter = object : RecyclerView.Adapter<MyViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                return MyViewHolder(
                        LayoutInflater.from(this@RVWithHeaderActivity).inflate(R.layout.rv_recent_session_item, parent, false))
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            }

            override fun getItemCount(): Int {
                return 10
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
