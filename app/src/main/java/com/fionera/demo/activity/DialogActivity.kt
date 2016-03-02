package com.fionera.demo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient

import com.fionera.demo.R
import com.fionera.demo.util.LogCat
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        LogCat.d("create")
        wv_in_dialog.setWebViewClient(WebViewClient())
        wv_in_dialog.clearCache(true)
        wv_in_dialog.loadUrl("file:///android_asset/hello.html?params=hello")
    }

    override fun onDestroy() {

        super.onDestroy()
        LogCat.d("destroy")
    }
}
