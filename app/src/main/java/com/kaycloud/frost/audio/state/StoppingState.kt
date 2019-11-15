package com.kaycloud.frost.audio.state

/**
 * Created by jiangyunkai on 2019/11/15
 */

class StoppingState : IPlayState() {

    override fun play(path: String) {
        context?.let {
            it.setState(it.playingState)
            it.getState()?.play(path)
        }
    }

    override fun stop() {
        context?.activityContext?.getAudioPlayer()?.stopMusic()
    }
}