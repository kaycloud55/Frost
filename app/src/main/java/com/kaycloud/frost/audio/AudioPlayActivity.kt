package com.kaycloud.frost.audio

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.drawable.ClipDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.kaycloud.framework.ext.getFileNameFromUri
import com.kaycloud.framework.ext.toast
import com.kaycloud.framework.util.TimeUtils
import com.kaycloud.framework.AppExecutors
import com.kaycloud.frost.R
import com.kaycloud.frost.audio.state.StateContext
import com.kaycloud.frost.audio.util.AudioHelper
import com.kaycloud.frost.audio.util.MusicUtils
import com.kaycloud.frost.audio.util.RealPathUtil
import com.kaycloud.frost.extension.isNotNullOrEmpty
import com.kaycloud.frost.base.BaseActivity
import com.orhanobut.logger.Logger
import com.yy.hiyo.camera.audio.AudioPlayer
import com.yy.hiyo.camera.audio.PlayCallback
import kotlinx.android.synthetic.main.activity_audio_play.*

/**
 * author: jiangyunkai
 * Created_at: 2019-11-14
 */
class AudioPlayActivity : BaseActivity() {

    private val TAG = "AudioPlayActivity"

    private var mUri: Uri? = null
    private var mPath: String? = null
    private var mCurrentIndex = 0
    private var mPlayList = mutableListOf<Music>()
    private val stateContext: StateContext = StateContext(this)
    private lateinit var mCoverRotateAnimator: ObjectAnimator
    private var mPlayer: AudioPlayer? = null

    private var mIvPlay: ImageView? = null
    /**
     * 播放器回调
     */
    private val playCallback = object : PlayCallback {

        override fun onPause() {
            iv_play.setImageDrawable(resources.getDrawable(R.drawable.icon_audio_play, null))
            rotateCover(false)
            Log.d(TAG, "onPause")
        }

        override fun onStart() {
            iv_play.setImageDrawable(resources.getDrawable(R.drawable.icon_audio_pause, null))
            rotateCover(true)
            Log.d(TAG, "onStart")
        }

        override fun onCompleted() {
            iv_play.setImageDrawable(resources.getDrawable(R.drawable.icon_audio_play, null))
            rotateCover(false)
            Log.d(TAG, "onCompleted")
        }

        override fun onError() {
            Log.d(TAG, "onAudioFilePlayError")
            mPlayer?.stopMusic()
            iv_play.setImageDrawable(resources.getDrawable(R.drawable.icon_audio_play, null))
        }

        override fun onProgress(currentMs: Int, totalMs: Int) {
            tv_progress.text = TimeUtils.millis2String(currentMs.toLong(), "mm:ss")
            val drawable = progress.drawable as ClipDrawable
            drawable.level = ((currentMs * 1f / totalMs) * 10000).toInt()
        }
    }

    private fun changeMusic(index: Int) {
        toast("changeMusic")

        if (mPlayer == null) {
            initPlayerAndState()
        }
        if (mPlayList.isNotEmpty()) {
            val item = when {
                mCurrentIndex >= mPlayList.size -> {
                    mCurrentIndex = 0
                    mPlayList.first()
                }
                mCurrentIndex < 0 -> {
                    mCurrentIndex = mPlayList.size - 1
                    mPlayList.last()
                }
                else -> mPlayList[mCurrentIndex]
            }
            mPath = item.path
            mPlayer?.reset()
            if (mPath.isNotNullOrEmpty()) {
                stateContext.play(mPath!!)
                showAudioInfo(item)
            }
        } else {
            toast("播放失败")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_play)
        setImmersive()
        initUI()
        prepareAudio(savedInstanceState)
    }

    private fun initUI() {

        iv_play.setOnClickListener {
            Log.d(TAG, "play click")
            //正在播放
            mPlayer?.let {
                if (it.getCurrentPosition() > 0) {
                    if (it.isPlaying()) {
                        stateContext.pause()
                    } else {
                        stateContext.resume()
                    }
                } else {
                    changeMusic(mCurrentIndex)
                }
            }
        }
        iv_prev.setOnClickListener {
            changeMusic(mCurrentIndex--)
        }
        iv_next.setOnClickListener {
            changeMusic(mCurrentIndex++)
        }
        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun initPlayerAndState() {
        mPlayer = AudioPlayer(this, mUri as Uri, playCallback)
        stateContext.setState(stateContext.stoppedState)
    }

    /**
     * 检查权限、解析播放信息、初始化播放器等
     */
    private fun prepareAudio(savedInstanceState: Bundle?) {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionCallback {
            override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<out String>,
                grantResults: IntArray
            ) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    parseAudioInfo()
                } else {
                    Logger.t(TAG).i("无权限")
                    parseAudioInfo()
                }
            }

        })

    }

    private fun parseAudioInfo() {

        fun buildUpAudio() {
            val fileName = getFileNameFromUri(mUri)
            val filePath = RealPathUtil.getPath(this, mUri)
            val artist = AudioHelper.getArtist(filePath)
            val duration = AudioHelper.getDuration(filePath)
            val music = Music().apply {
                path = filePath
                title = fileName
                this.artist = artist
                this.duration = duration?.toLong() ?: 0
            }
            mPlayList.add(music)
            mCurrentIndex = 0
            AppExecutors.getInstance().getMainThread().execute {
                showAudioInfo(music)
            }
        }

        mUri = intent.data ?: return

        AppExecutors.getInstance().getDiskIO().execute {
            initPlayerAndState()
            buildUpAudio()
        }
        AppExecutors.getInstance().getDiskIO().execute {
            mPlayList.addAll(MusicUtils.scanMusic(this))
        }
    }

    private fun showAudioInfo(music: Music) {
        val album = AudioHelper.getAlbumArt(music.path)
        if (album != null) {
            iv_cover.setImageBitmap(album)
            iv_blur_album.setImageBitmap(album)
            bg_blur_album.startBlur()
        } else {
            iv_cover.setImageDrawable(
                resources.getDrawable(R.drawable.icon_default_audio_cover)
            )
            bg_blur_album.pauseBlur()
            iv_blur_album.setImageResource(0)
        }

        tv_author.text = music.artist
        tv_duration.text = TimeUtils.millis2String(music.duration, "mm:ss")
        tv_audio_name.text = music.title
    }

    override fun onDestroy() {
        stateContext.stop()
        super.onDestroy()
    }

    fun getAudioPlayer(): AudioPlayer? {
        return mPlayer
    }

    /**
     * 封面旋转动画
     */
    private fun rotateCover(rotate: Boolean) {
        if (!this::mCoverRotateAnimator.isInitialized) {
            mCoverRotateAnimator = ObjectAnimator.ofFloat(iv_cover, "rotation", 0f, 360f).apply {
                duration = 7000
                repeatCount = Animation.INFINITE
                repeatMode = ValueAnimator.RESTART
                interpolator = LinearInterpolator()
            }
        }
        if (rotate) {
            if (mCoverRotateAnimator.isPaused) {
                mCoverRotateAnimator.resume()
            } else {
                mCoverRotateAnimator.start()
            }
        } else {
            mCoverRotateAnimator.pause()
        }
    }
}