package com.kaycloud.frost.module.image

import android.os.Bundle
import com.github.chrisbanes.photoview.PhotoView
import com.kaycloud.framework.image.imageloader.ImageLoader
import com.kaycloud.frost.R
import com.kaycloud.frost.base.BaseActivity

class PhotoDetailActivity : BaseActivity() {

    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        url = intent.getStringExtra("url")

        val photoView = findViewById<PhotoView>(R.id.photo_view)

        ImageLoader.loadImage(this, photoView, url)

    }
}
