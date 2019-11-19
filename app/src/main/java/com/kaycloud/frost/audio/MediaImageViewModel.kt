package com.kaycloud.frost.audio

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * author: jiangyunkai
 * Created_at: 2019-11-19
 */
class MediaImageViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val _images = MutableLiveData<List<MediaStoreImage>>()
    val images: LiveData<List<MediaStoreImage>> get() = _images

    fun loadImages() {
        viewModelScope.launch {
            val imageList = queryImages()
            _images.postValue(imageList)
        }
    }

    private suspend fun queryImages(): List<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()
        /**
         * ContentResolver执行比较慢，需要在单独的线程进行
         */
        withContext(Dispatchers.IO) {
            /**
             * 表示需要查询的具体列（类似表格，需要哪些信息就选中那几列），也可以是null,但是如果只执行那几列可以提高性能。
             */
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
            )

            /**
             * 查询语句，类似SQL。也可以传入null，那么所有的行都会返回，也就是不过滤。
             */
            val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"


            /**
             * 查询参数，用来替代查询语句中的`?`
             */
            val selectionArgs =
                arrayOf(dateToTimestamp(day = 22, month = 10, year = 2008).toString())

            /**
             * 排序逻辑
             */
            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, sortOrder
            )?.use { cursor: Cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateTakenColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val dateTaken = Date(cursor.getLong(dateTakenColumn))
                    val displayName = cursor.getString(displayNameColumn)
                    /**
                     * 由于已经使用了[MediaStore.Images.Media.EXTERNAL_CONTENT_URI]作为查询路径，所以可以作为基础uri拼接到ID前面
                     *
                     */
                    val contentUri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                    val image = MediaStoreImage(id, displayName, dateTaken, contentUri)
                    images += image

                }
            }
        }
        return images
    }

    /**
     * Convience method to convert a day/month/year date into a UNIX timestamp.
     *
     * We're suppressing the lint warning because we're not actually using the date formatter
     * to format the date to display, just to specify a format to use to parse it, and so the
     * locale warning doesn't apply.
     */
    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            formatter.parse("$day.$month.$year")?.time ?: 0
        }

}