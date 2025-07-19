package com.emgc.livestreamrecorder.config

import com.emgc.livestreamrecorder.enums.ChannelType
import com.emgc.livestreamrecorder.enums.RecordingStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChannelConfig {

    @Bean
    fun channels(): List<Channel> {
        return listOf(

        )
    }
}

data class Channel(
    val channelName: String,
    val channelId: String,
    val type: ChannelType,
    var status: RecordingStatus
) {
    fun record() {
        this.status = RecordingStatus.RECORDING
    }

    fun idle() {
        this.status = RecordingStatus.IDLE
    }
}