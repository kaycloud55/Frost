package com.kaycloud.framework.image.config

import android.widget.ImageView

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 * 图片加载配置
 */
open class ImageConfig(
    val url: String?,
    val imageView: ImageView,
    val placeHolder: Int = 0,
    var errorPic: Int = 0
)
