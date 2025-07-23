package com.emgc.core.holder

import com.emgc.core.domain.Channel
import com.emgc.core.domain.ChannelRepository
import com.emgc.livestreamrecorder.enums.RecordingStatus
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class ChannelHolder(
    private val channelRepository: ChannelRepository
) {
    lateinit var data: MutableList<ChannelDetail>

    @PostConstruct
    fun load() {
        data = channelRepository.findAll().map {
            ChannelDetail(
                channelId = it.channelId,
                recordingStatus = RecordingStatus.IDLE,
                channel = it
            )
        }.toMutableList()
    }

    fun add(channelDetail: ChannelDetail) {
        this.data.add(channelDetail)
    }
}

data class ChannelDetail(
    val channelId: String,
    var recordingStatus: RecordingStatus,
    val channel: Channel
) {
    fun record() {
        this.recordingStatus = RecordingStatus.RECORDING
    }

    fun idle() {
        this.recordingStatus = RecordingStatus.IDLE
    }
}