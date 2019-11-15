package com.kaycloud.frost.audio.state

/**
 * Created by jiangyunkai on 2019/11/15
 */

abstract class IPlayState {

    protected var context: StateContext? = null

    fun setStateContext(_context: StateContext) {
        this.context = _context
    }

    open fun play(path: String) {}

    open fun pause() {}

    open fun resume() {}

    open fun stop() {}
}