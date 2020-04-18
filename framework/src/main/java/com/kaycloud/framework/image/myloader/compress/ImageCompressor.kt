package com.kaycloud.framework.image.myloader.compress

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.kaycloud.framework.image.myloader.IImageCompressor
import java.io.FileDescriptor
import java.io.InputStream

/**
 * Created by jiangyunkai on 2020/4/13
 * 图片压缩实现类
 *
 * 将图片加载进内存需要考虑的一些因素：
 *  1. 在内存中完整加载图片的估计内存使用量
 *  2. 根据应用的其他内存要求，愿意分配用于加载此图片的内存量
 *  3. 图片要载入到的目标View的尺寸
 *  4. 当前设备的屏幕大小和密度
 *
 *
 *  核心思想是通过BitmapFactory.Options设置采样率，按需加载缩小后的图片，降低内存占用
 *
 *  对于1024*1024像素的图片来说，假设采用ARGB8888存储，占用内存为4MB。如果inSampleSize为2，采样后的图片内存占用为512*512*4，即1MB。
 */

class ImageCompressor : IImageCompressor {

    companion object {
        private var instance: ImageCompressor? = null
        fun getInstance(): ImageCompressor {
            return instance ?: synchronized(ImageCompressor::class) {
                instance ?: ImageCompressor().apply {
                    instance = this
                }
            }
        }
    }

    /**
     * 根据图片原始尺寸和需求尺寸计算压缩比例。
     * 采样率是2的幂次的原因是解码器使用的最终值将向下舍入为最接近的2的幂：https://developer.android.google.cn/reference/android/graphics/BitmapFactory.Options#inSampleSize
     * @param options 图片的尺寸信息
     * @param reqHeight 目标高度
     * @param reqWidth 目标宽度
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1 //采样率

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * 从本地资源文件加载Bitmap
     */
    fun decodeSampleBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true //首先使用inJustDecodeBounds = true来测量尺寸，不分配内存
            BitmapFactory.decodeResource(res, resId, this)

            //计算采样率
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            //用设置的采样率进行解码
            inJustDecodeBounds = false
            BitmapFactory.decodeResource(res, resId, this)

        }

    }

    /**
     * 从字节数组加载Bitmap
     */
    fun decodeSampleBitmapFromByteArray(
        data: ByteArray,
        offset: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true //首先使用inJustDecodeBounds = true来测量尺寸，不分配内存
            BitmapFactory.decodeByteArray(data, offset, data.size, this)

            //计算采样率
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            //用设置的采样率进行解码
            inJustDecodeBounds = false
            BitmapFactory.decodeByteArray(data, offset, data.size, this)
        }

    }


    /**
     * 从文件路径加载Bitmap
     */
    fun decodeSampleBitmapFromFile(
        pathName: String,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true //首先使用inJustDecodeBounds = true来测量尺寸，不分配内存
            BitmapFactory.decodeFile(pathName, this)

            //计算采样率
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            //用设置的采样率进行解码
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(pathName, this)

        }

    }

    /**
     * 从IO流加载Bitmap
     */
    fun decodeSampleBitmapFromStream(
        inputStream: InputStream,
        outPadding: Rect,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true //首先使用inJustDecodeBounds = true来测量尺寸，不分配内存
            BitmapFactory.decodeStream(inputStream, outPadding, this)

            //计算采样率
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            //用设置的采样率进行解码
            inJustDecodeBounds = false
            BitmapFactory.decodeStream(inputStream, outPadding, this)

        }

    }

    /**
     * 从IO流加载Bitmap
     */
    fun decodeSampleBitmapFromFileDescriptor(
        fd: FileDescriptor,
        outPadding: Rect,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true //首先使用inJustDecodeBounds = true来测量尺寸，不分配内存
            BitmapFactory.decodeFileDescriptor(fd, outPadding, this)

            //计算采样率
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            //用设置的采样率进行解码
            inJustDecodeBounds = false
            BitmapFactory.decodeFileDescriptor(fd, outPadding, this)

        }

    }
}
