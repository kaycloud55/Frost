package com.kaycloud.frost.audio.state

/**
 * Created by jiangyunkai on 2019/11/15
 */
class PlayingState : IPlayState() {

    override fun play(path: String) {
        context?.activityContext?.getAudioPlayer()?.playMusic(path)
    }

    override fun pause() {
        context?.let {
            it.setState(it.pausedState)
            it.getState()?.pause()
        }
    }

    override fun stop() {
        context?.let {
            it.setState(it.stoppedState)
            it.getState()?.stop()
        }
    }
}