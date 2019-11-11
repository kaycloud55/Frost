package com.kaycloud.frost.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.kaycloud.frost.R

class PhotoDetailActivity : AppCompatActivity() {

    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        url = intent.getStringExtra("url")

        val photoView = findViewById<PhotoView>(R.id.photo_view)

        Glide.with(this)
            .load(url).into(photoView)
    }
}