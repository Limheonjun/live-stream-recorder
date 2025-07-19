package com.emgc.livestreamrecorder.recorder

interface StreamRecorder {
    fun record(url: String, channelName: String)
}