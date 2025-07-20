package com.emgc.core.domain

import com.emgc.livestreamrecorder.enums.ChannelType
import com.emgc.livestreamrecorder.enums.RecordingStatus
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: RecordingStatus,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
) {
    fun record() {
        this.status = RecordingStatus.RECORDING
    }

    fun idle() {
        this.status = RecordingStatus.IDLE
    }

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
                status = RecordingStatus.IDLE,
            )
        }
    }
}