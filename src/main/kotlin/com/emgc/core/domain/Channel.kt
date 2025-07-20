package com.emgc.core.domain

import com.emgc.livestreamrecorder.enums.ChannelType
import jakarta.persistence.*

@Entity
@Table(name = "channel")
class Channel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(name = "channel_name")
    val channelName: String,

    @Column(name = "channel_id")
    val channelId: String,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    val type: ChannelType,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
) {
    companion object {
        fun create(
            channelName: String,
            channelId: String,
            type: ChannelType
        ): Channel {
            return Channel(
                id = null,
                channelName = channelName,
                channelId = channelId,
                type = type,
            )
        }
    }
}