package com.kaycloud.framework.image.myloader

import android.content.Context
import android.os.Environment
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by jiangyunkai on 2020/4/13
 */
object CacheUtil {

    fun hashKeyFromUrl(url: String): String {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(url.toByteArray())
            bytesToHexString(
                digest.digest()
            )
        } catch (e: NoSuchAlgorithmException) {
            url.hashCode().toString()
        }
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in 0..bytes.size) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    fun getDiskCacheDir(context: Context, uniqueName: String): File {
        //还要加一个isExternalStorageRemovable
        val cachePath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            context.externalCacheDir!!.path
        } else {
            context.cacheDir.path
        }
        return File(cachePath + File.separator + uniqueName)
    }

    fun getAvailableSpace(path: File): Long {
        return path.usableSpace
    }

}