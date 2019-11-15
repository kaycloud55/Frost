package com.kaycloud.frost.audio.state

/**
 * Created by jiangyunkai on 2019/11/15
 */
class PausingState : IPlayState() {

    override fun play(path: String) {
        context?.let {
            it.setState(it.playingState)
            it.getState()?.play(path)
        }
    }

    override fun pause() {
        context?.activityContext?.getAudioPlayer()?.pauseMusic()
    }

    override fun resume() {
        context?.let {
            it.setState(it.resumingState)
            it.getState()?.resume()
        }
    }

    override fun stop() {
        context?.let {
            it.setState(it.stoppedState)
            it.getState()?.stop()
        }
    }
}