package com.prograils.joga.ui.video

import androidx.lifecycle.ViewModel

class VideoViewModel : ViewModel() {
    var playWhenReady: Boolean = true
    var currentWindow: Int = 0
    var playbackPosition: Long = 0
}