package com.emgc.admin.presentation.request

import com.emgc.livestreamrecorder.enums.ChannelType

data class ChannelRegisterRequest(
    val channelName: String,
    val channelId: String,
    val type: ChannelType,
)