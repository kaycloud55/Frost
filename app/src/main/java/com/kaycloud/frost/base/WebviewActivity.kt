package com.kaycloud.frost.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.kaycloud.frost.R

class WebviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        val url = intent.getStringExtra("url")
        val webview = findViewById<WebView>(R.id.webview)
        webview.loadUrl(url)
    }
}
