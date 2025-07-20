package com.emgc.core.config

import com.emgc.core.domain.Channel
import com.emgc.core.domain.ChannelRepository
import com.emgc.livestreamrecorder.enums.RecordingStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn

@Configuration
class ChannelConfig(
    private val channelRepository: ChannelRepository
) {

    @Bean
    @DependsOn("entityManagerFactory")
    fun channelRecordingStatus(): List<ChannelDetail> {
        return channelRepository.findAll().map {
            ChannelDetail(
                channelId = it.channelId,
                recordingStatus = RecordingStatus.IDLE,
                channel = it
            )
        }
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