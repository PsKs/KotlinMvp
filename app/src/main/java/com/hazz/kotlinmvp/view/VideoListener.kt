package com.hazz.kotlinmvp.view

import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack

/**
 * Created by xuhao on 2017/11/27.
 * desc: VideoAllCallBack callback
 */
interface VideoListener : StandardVideoAllCallBack {

    // The loading is successful, objects[0] is the title, and object[1] is the current player (full screen or non-full screen)
    override fun onPrepared(url: String, vararg objects: Any) {

    }

    // Click the start button to play, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickStartIcon(url: String, vararg objects: Any) {

    }

    // Click the start button in the wrong state, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickStartError(url: String, vararg objects: Any) {

    }

    // Click the start button in the playback state --->Stop, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickStop(url: String, vararg objects: Any) {

    }

    // Click the start button in the full-screen playback state --->Stop, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickStopFullscreen(url: String, vararg objects: Any) {

    }

    // Click the start button in the paused state --->Play, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickResume(url: String, vararg objects: Any) {

    }

    // Click the start button in the full-screen pause state--->play, objects[0] is title, object[1] is the current player (full screen or non-full screen)
    override fun onClickResumeFullscreen(url: String, vararg objects: Any) {

    }

    // Click on the blank and pop up the seekbar, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickSeekbar(url: String, vararg objects: Any) {

    }

    // Click on the full-screen seekbar, objects[0] is the title, and object[1] is the current player (full screen or non-full screen)
    override fun onClickSeekbarFullscreen(url: String, vararg objects: Any) {

    }

    // After playing, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onAutoComplete(url: String, vararg objects: Any) {

    }

    // Go to full screen, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onEnterFullscreen(url: String, vararg objects: Any) {

    }

    // Exit full screen, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onQuitFullscreen(url: String, vararg objects: Any) {

    }

    // Enter the small window, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onQuitSmallWidget(url: String, vararg objects: Any) {

    }

    // Exit the small window, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onEnterSmallWidget(url: String, vararg objects: Any) {

    }

    // Touch to adjust the sound, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onTouchScreenSeekVolume(url: String, vararg objects: Any) {

    }

    // Touch to adjust the progress, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onTouchScreenSeekPosition(url: String, vararg objects: Any) {

    }

    // Touch to adjust the brightness, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onTouchScreenSeekLight(url: String, vararg objects: Any) {

    }

    // Play error, objects[0] is the title, object[1] is the current player (full screen or not full screen)
    override fun onPlayError(url: String, vararg objects: Any) {

    }

    // Click the blank area to start playing, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickStartThumb(url: String, vararg objects: Any) {

    }

    // Click on the blank area in the playback, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickBlank(url: String, vararg objects: Any) {

    }

    // Click on a blank area in full-screen playback, objects[0] is the title, object[1] is the current player (full screen or non-full screen)
    override fun onClickBlankFullscreen(url: String, vararg objects: Any) {

    }
}