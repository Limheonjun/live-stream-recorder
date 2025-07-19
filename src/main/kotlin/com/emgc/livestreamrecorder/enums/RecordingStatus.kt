package com.emgc.livestreamrecorder.enums

enum class RecordingStatus(
    private val description: String,
) {
    IDLE("녹화를 시작하지 않은 상태"),
    RECORDING("녹화가 진행중인 상태"),
}