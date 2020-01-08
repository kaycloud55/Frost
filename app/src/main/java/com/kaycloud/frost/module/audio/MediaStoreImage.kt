package com.kaycloud.frost.module.audio

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import java.util.*

/**
 * author: jiangyunkai
 * Created_at: 2019-11-19
 */
data class MediaStoreImage(
    val id: Long,
    val displayName: String,
    val dataToken: Date,
    val contentUri: Uri
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<MediaStoreImage>() {

            override fun areItemsTheSame(
                oldItem: MediaStoreImage,
                newItem: MediaStoreImage
            ): Boolean = oldItem.id == newItem.id


            override fun areContentsTheSame(
                oldItem: MediaStoreImage,
                newItem: MediaStoreImage
            ): Boolean = oldItem == newItem

        }
    }
}