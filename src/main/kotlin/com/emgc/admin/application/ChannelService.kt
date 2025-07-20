package com.emgc.admin.application

import com.emgc.admin.presentation.request.ChannelRegisterRequest
import com.emgc.core.domain.Channel
import com.emgc.core.domain.ChannelRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChannelService(
    private val channelRepository: ChannelRepository,
) {
    fun getChannelList(): List<Channel> {
        return channelRepository.findAll()
    }

    fun register(request: ChannelRegisterRequest) {
        val channel = Channel.create(
            channelName = request.channelName,
            channelId = request.channelId,
            type = request.type,
        )
        channelRepository.save(channel)
    }
}