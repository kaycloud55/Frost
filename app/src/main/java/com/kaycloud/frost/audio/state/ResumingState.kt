package com.kaycloud.frost.audio.state

/**
 * Created by jiangyunkai on 2019/11/15
 */
class ResumingState : IPlayState() {

    override fun play(path: String) {
        context?.let {
            it.setState(it.playingState)
            it.getState()?.play(path)
        }
    }

    override fun pause() {
        context?.let {
            it.setState(it.pausedState)
            it.getState()?.pause()
        }
    }

    override fun resume() {
        context?.activityContext?.getAudioPlayer()?.playMusic("")
    }

    override fun stop() {
        context?.let {
            it.setState(it.stoppedState)
            it.getState()?.stop()
        }
    }
}