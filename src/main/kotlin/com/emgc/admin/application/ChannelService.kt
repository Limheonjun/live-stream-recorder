package com.emgc.admin.application

import com.emgc.admin.application.response.ChannelResponse
import com.emgc.admin.presentation.request.ChannelRegisterRequest
import com.emgc.core.domain.Channel
import com.emgc.core.domain.ChannelRepository
import com.emgc.core.holder.ChannelDetail
import com.emgc.core.holder.ChannelHolder
import com.emgc.livestreamrecorder.enums.RecordingStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChannelService(
    private val channelRepository: ChannelRepository,
    private val channelHolder: ChannelHolder
) {
    fun getChannelList(): List<ChannelResponse> {
        val channels = channelRepository.findAll()
        return channels.map { channel ->
            ChannelResponse(
                id = channel.id!!,
                channelName = channel.channelName,
                channelId = channel.channelId,
                type = channel.type,
                status = channelHolder.data.first { it.channelId == channel.channelId }.recordingStatus,
                isDeleted = channel.isDeleted
            )
        }
    }

    fun register(request: ChannelRegisterRequest) {
        val channel = Channel.create(
            channelName = request.channelName,
            channelId = request.channelId,
            type = request.type,
        )
        channelRepository.save(channel)
        channelHolder.data.add(ChannelDetail(
            channelId = request.channelId,
            recordingStatus = RecordingStatus.IDLE,
            channel = channel
        ))
    }
}