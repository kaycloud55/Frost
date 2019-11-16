package com.kaycloud.frost.audio

/**
 * author: jiangyunkai
 * Created_at: 2019-11-14
 */
interface IAudioHandler {

    fun playMusic(path: String)

    fun pauseMusic()

    fun stopMusic()

    fun resumeMusic()

    fun getCurrentPosition(): Int

    fun seekTo(progress: Int)

    fun isPlaying(): Boolean
}