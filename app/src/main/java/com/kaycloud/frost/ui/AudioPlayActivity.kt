package com.kaycloud.frost.ui

import androidx.appcompat.app.AppCompatActivity
import com.kaycloud.frost.R

/**
 * Created by jiangyunkai on 2019/11/15
 */

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.kaycloud.frost.audio.AudioPlayer
import com.kaycloud.frost.audio.PlayCallback
import com.kaycloud.frost.audio.state.StateContext
import com.kaycloud.frost.audio.util.AudioHelper
import com.kaycloud.frost.audio.util.RealPathUtil
import kotlinx.android.synthetic.main.activity_audio_play.*

/**
 * author: jiangyunkai
 * Created_at: 2019-11-14
 */
class AudioPlayActivity : AppCompatActivity() {

    private var mUri: Uri? = null
    private var mPath: String? = null
    private var mFileName: String? = null

    private val stateContext: StateContext = StateContext(this)

    private var animation: ObjectAnimator? = null

    private var mPlayer: AudioPlayer? = null

    private val playCallback = object : PlayCallback {

        override fun onPause() {
            iv_play.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_audio_play))
            rotateCover(false)
            Log.d(TAG, "onPause")
        }

        override fun onStart() {
            iv_play.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_audio_pause))
            rotateCover(true)
            Log.d(TAG, "onStart")
        }

        override fun onCompleted() {
            iv_play.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_audio_play))
            Log.d(TAG, "onCompleted")
        }

        override fun onError() {
            Log.d(TAG, "onAudioFilePlayError")
            mPlayer?.stopMusic()
            iv_play.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_audio_play))
        }

        override fun onProgress(progress: Long) {
            Log.d(TAG, "onAudioFilePlayProgress:$progress")
            tv_progress.text = TimeUtils.getFormatTimeString(progress, "min:sec")
        }
    }

    private fun initView() {
        iv_play.setOnClickListener {
            Log.d(TAG, "play click")
            if (mUri == null) {
                return@setOnClickListener
            }
            if (mPlayer == null) {
                initPlayerAndState()
            }

            mPlayer?.let {
                //正在播放
                if (it.getCurrentPosition() > 0) {
                    if (it.isPlaying()) {
                        stateContext.pause()
                    } else {
                        stateContext.resume()
                    }
                } else {
                    //是否要恢复进度
                    mFileName?.apply {
                        if (isAudioFast()) {
                            stateContext.play(RealPathUtil.getPath(this@AudioPlayActivity, mUri))
                        }
                    }
                }
            }
        }
    }

    private fun initPlayerAndState() {
        mPlayer = AudioPlayer(this, mUri as Uri, playCallback)
        stateContext.setState(stateContext.stoppedState)
    }

    private fun prepare(savedInstanceState: Bundle?) {
        PermissionHelper.checkStoragePermission(this, object : IPermissionListener {

            override fun onPermissionGranted(permission: Array<out String>) {
                Log.d(TAG, "有权限")
                checkIntent(savedInstanceState)
            }

            override fun onPermissionDenied(permission: Array<out String>) {
                PermissionHelper.showStorageRemindDialog(this@AudioPlayActivity,
                    object : IPermissionListener {
                        override fun onPermissionGranted(permission: Array<out String>) {
                            Log.d(TAG, "有权限")
                            checkIntent(savedInstanceState)
                        }

                        override fun onPermissionDenied(permission: Array<out String>) {
                            Log.d(TAG, "无权限")
                            toast(R.string.no_storage_permissions)
                        }
                    })
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarManager.INSTANCE.setStatusBarColor(this, Color.TRANSPARENT)
        setContentView(R.layout.activity_audio_play)
        initView()
        prepare(savedInstanceState)
    }

    private fun checkIntent(savedInstanceState: Bundle? = null) {
        mUri = intent.data ?: return
        val uri = mUri.toString()
        Log.d(TAG, "uri:$uri")
        mFileName = getFilenameFromUri(mUri!!)

        initPlayerAndState()


        fun parseAudioInfo() {
            tv_audio_name.text = mFileName
            iv_cover.setImageBitmap(
                AudioHelper.getAlbumArt(RealPathUtil.getPath(this, mUri)))
            tv_author.text = AudioHelper.getArtist(RealPathUtil.getPath(this, mUri))
            tv_duration.text = AudioHelper.getDuration(RealPathUtil.getPath(this, mUri))
        }

        parseAudioInfo()
    }

    private fun setSystemUIVisible(show: Boolean) {
        if (show) {
            var uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            uiFlags = uiFlags or 0x00001000
            window.decorView.systemUiVisibility = uiFlags
        } else {
            var uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            uiFlags = uiFlags or 0x00001000
            window.decorView.systemUiVisibility = uiFlags
        }
    }

    override fun onPause() {
        super.onPause()
        stateContext.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stateContext.stop()
    }

    fun getAudioPlayer(): AudioPlayer? {
        return mPlayer
    }

    private fun rotateCover(rotate: Boolean) {
        if (animation == null) {
            animation = ObjectAnimator.ofFloat(iv_cover, "rotation", 0f, 360f).apply {
                duration = 7000
                repeatCount = Animation.INFINITE
                repeatMode = ValueAnimator.RESTART
                interpolator = LinearInterpolator()
            }
        }
        if (rotate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && animation!!.isPaused) {
                animation?.resume()
            } else {
                animation?.start()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animation?.pause()
            } else {
                animation?.cancel()
            }
        }
    }
}