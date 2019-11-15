package com.kaycloud.frost.audio.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import com.kaycloud.framework.ext.TAG

/**
 * Created by jiangyunkai on 2019/11/15
 */

object AudioHelper {

    private val mediaMetadataRetriever: MediaMetadataRetriever = MediaMetadataRetriever()

    fun getAlbumArt(path: String): Bitmap? {
        try {
            mediaMetadataRetriever.setDataSource(path)
            val cover = mediaMetadataRetriever.embeddedPicture
            return BitmapFactory.decodeByteArray(cover, 0, cover.size)
        } catch (e: Exception) {
            Log.d(TAG, "getAlbumArt fail")
        }
        return null
    }

    fun getArtist(path: String): String? {
        mediaMetadataRetriever.setDataSource(path)
        return mediaMetadataRetriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_ARTIST)
    }

    fun getDuration(path: String): String? {
        mediaMetadataRetriever.setDataSource(path)
        val duration =
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        if (duration.isNotNullOrEmpty()) {
            return TimeUtils.getFormatTimeString(duration.toLong(), "min:sec")
        }
        return ""
    }
}