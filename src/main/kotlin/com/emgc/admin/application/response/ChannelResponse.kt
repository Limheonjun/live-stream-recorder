package com.emgc.admin.application.response

import com.emgc.livestreamrecorder.enums.ChannelType
import com.emgc.livestreamrecorder.enums.RecordingStatus

data class ChannelResponse(
    val id: Long,
    val channelName: String,
    val channelId: String,
    val type: ChannelType,
    val status: RecordingStatus,
    val isDeleted: Boolean
)
