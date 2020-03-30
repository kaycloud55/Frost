package com.yy.hiyo.camera.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.kaycloud.framework.ext.TAG
import com.kaycloud.framework.executor.AppTaskExecutor
import com.kaycloud.frost.module.audio.IAudioHandler
import com.kaycloud.frost.extension.isNotNullOrEmpty
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

/**
 * author: jiangyunkai
 * Created_at: 2019-11-14
 */
class AudioPlayer(val context: Context, val uri: Uri, val callback: PlayCallback) :
    IAudioHandler {

    private var mMediaPlayer: MediaPlayer? = null
    private var isInitialized = false
    private var mTimer: Timer? = null
    private var lastPlayProgress: Int = 0

    init {
        AppTaskExecutor.getInstance().executeOnDiskIO {
            mMediaPlayer = MediaPlayer.create(context, uri)
            isInitialized = true
            mMediaPlayer?.setOnCompletionListener {
                callback.onCompleted()
                it.reset()
                callback.onProgress(0, it.duration)
            }
            mTimer = Timer()
        }
    }

    override fun playMusic(path: String) {
        mMediaPlayer?.let {
            if (!isInitialized || it.isPlaying) {
                return
            }
            try {
                if (path.isNotNullOrEmpty()) {
                    it.reset()
                    it.setDataSource(path)
                    it.prepare()
                }
            } catch (e: IOException) {
                Log.d(TAG, "playMusic failed,exception:$e")
            }
            it.start()
            callback.onStart()
            mTimer?.apply {
                this.schedule(object : TimerTask() {
                    override fun run() {
                        if (it.isPlaying) {
                            AppTaskExecutor.getInstance().postToMainThread {
                                lastPlayProgress = it.currentPosition
                                callback.onProgress(it.currentPosition, it.duration)
                            }
                        }
                    }
                }, 0, 100)
            }
        }
    }

    override fun pauseMusic() {
        mMediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                lastPlayProgress = it.currentPosition
                callback.onPause()
            }
        }
    }

    override fun stopMusic() {
        mMediaPlayer?.apply {
            stop()
            reset()
            lastPlayProgress = 0
        }
        onDestroyed()
    }

    fun reset() {
        mMediaPlayer?.let {
            it.stop()
            it.reset()
            lastPlayProgress = 0
        }
    }

    private fun onDestroyed() {
        mTimer?.cancel()
        mMediaPlayer?.release()
        mMediaPlayer = null
        isInitialized = false
    }

    override fun resumeMusic() {
        mMediaPlayer?.apply {
            seekTo(lastPlayProgress)
            start()
        }
    }

    override fun isPlaying() = mMediaPlayer?.isPlaying ?: false

    override fun seekTo(progress: Int) {
        mMediaPlayer?.seekTo(progress)
    }

    override fun getCurrentPosition(): Int {
        return mMediaPlayer?.currentPosition ?: 0
    }
}

interface PlayCallback {

    fun onCompleted()

    fun onError()

    fun onProgress(currentMs: Int, totalMs: Int)

    fun onPause()

    fun onStart()
}