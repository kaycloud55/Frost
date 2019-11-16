package com.kaycloud.framework.ext

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.lang.NullPointerException

/**
 * created by jiangyunkai on 2019/8/14.
 */
val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }


/**
 * https://developer.android.com/training/secure-file-sharing/retrieve-info.html
 *
 * https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
 */
fun Context.getFileNameFromUri(uri: Uri?): String {
    uri?.apply {
        var fileName = ""
        if (uri.scheme == ContentResolver.SCHEME_FILE) {
            fileName = uri.lastPathSegment ?: ""
        } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                fileName = cursor.getString(nameIndex)
            }
        }
        if (fileName.isEmpty()) {
            fileName = uri.path ?: ""
            val cut = fileName.lastIndexOf('/')
            if (cut != -1) {
                fileName = fileName.substring(cut + 1)
            }
        }
        return fileName
    }
    return ""
}

fun Context.getFileDetailFromUri(uri: Uri?): FileDetail? {
    uri?.apply {
        var fileDetail = FileDetail()
        if (uri.scheme == ContentResolver.SCHEME_FILE) {
            fileDetail.fileName = uri.lastPathSegment ?: ""
            try {
                val file = File(uri.path)
                fileDetail.fileSize = file.length()
                if (fileDetail.fileName.isNullOrEmpty()) {
                    fileDetail.fileName = file.name
                }
            } catch (e: NullPointerException) {
                Log.i(TAG, "getFileDetailFromUri error,NullPointerException")
            }
        } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                fileDetail.fileName = cursor.getString(nameIndex)
            }
        }
        if (fileDetail.fileName.isNullOrEmpty()) {
            val path = uri.path ?: ""
            val cut = path.lastIndexOf('/')
            if (cut != -1) {
                fileDetail.fileName = path.substring(cut + 1)
            }
        }
        return fileDetail
    }
    return null


}

class FileDetail {
    var fileName: String? = null
    var fileSize: Long? = null
}
