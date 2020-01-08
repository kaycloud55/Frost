package com.kaycloud.frost.module.audio.state

import com.kaycloud.frost.module.audio.AudioPlayActivity


/**
 * Created by jiangyunkai on 2019/11/15
 */
class StateContext(val activityContext: AudioPlayActivity) {

    val playingState = PlayingState()
    val pausedState = PausingState()
    val stoppedState = StoppingState()
    val resumingState = ResumingState()

    private var state: IPlayState? = stoppedState

    fun setState(_state: IPlayState) {
        this.state = _state
        state?.setStateContext(this)
    }

    fun getState() = state

    fun play(path: String) {
        this.state?.play(path)
    }

    fun pause() {
        state?.pause()
    }

    fun resume() {
        state?.resume()
    }

    fun stop() {
        state?.stop()
    }
}