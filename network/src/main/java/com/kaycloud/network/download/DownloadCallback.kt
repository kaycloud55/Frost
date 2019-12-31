package com.kaycloud.network.download

/**
 * author: jiangyunkai
 * Created_at: 2019-12-31
 */
interface DownloadCallback {

    fun onSuccess()

    fun onFail()

    fun onProgress(progress: Float)
}