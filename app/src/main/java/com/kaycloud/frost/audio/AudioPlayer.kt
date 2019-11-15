package com.kaycloud.frost.audio

/**
 * Created by jiangyunkai on 2019/11/15
 */
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.yy.appbase.extensions.isNotNullOrEmpty
import com.yy.base.taskexecutor.YYTaskExecutor
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

class AudioPlayer(val context: Context, val uri: Uri, val callback: PlayCallback) :
    IAudioHandler {

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    private var mediaPlayer: MediaPlayer? = null

    private var isInitialized = false
    private var mTimer: Timer? = null
    private var mFilePath: String? = null
    private var lastPlayProgess: Int = 0

    init {
        YYTaskExecutor.execute {
            mediaPlayer = MediaPlayer.create(context, uri)
            isInitialized = true
            mediaPlayer?.setOnCompletionListener {
                callback.onCompleted()
            }
            mTimer = Timer()
        }
    }

    override fun playMusic(path: String) {
        mediaPlayer?.let {
            if (!isInitialized || it.isPlaying) {
                return
            }
            try {
                if (path.isNotNullOrEmpty()) {
                    mFilePath = path
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
                            val pos = it.currentPosition
                            YYTaskExecutor.postToMainThread {
                                lastPlayProgess = pos
                                callback.onProgress(pos.toLong())
                            }
                        }
                    }
                }, 0, 100)
            }
        }
    }

    override fun pauseMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                lastPlayProgess = it.currentPosition
                callback.onPause()
            }
        }
    }

    override fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.reset()
                lastPlayProgess = 0
            }
        }
        onDestroyed()
    }

    private fun onDestroyed() {
        mediaPlayer?.apply {
            stop()
            release()
            mediaPlayer = null
        }
        mTimer?.cancel()
        mTimer = null
        isInitialized = false
        lastPlayProgess = 0
    }

    override fun resumeMusic() {
        mediaPlayer?.apply {
            seekTo(lastPlayProgess)
            start()
        }
    }

    override fun isPlaying(): Boolean {
        mediaPlayer?.let {
            return it.isPlaying
        }
        return false
    }

    override fun seekTo(progress: Int) {
        mediaPlayer?.seekTo(progress)
    }
}

interface PlayCallback {

    fun onCompleted()

    fun onError()

    fun onProgress(progress: Long)

    fun onPause()

    fun onStart()
}