package com.emgc.livestreamrecorder.scheduler


import com.emgc.core.domain.ChannelRepository
import com.emgc.livestreamrecorder.enums.RecordingStatus
import com.emgc.livestreamrecorder.extractor.StreamUrlExtractor
import com.emgc.livestreamrecorder.recorder.StreamRecorder
import com.emgc.livestreamrecorder.util.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class LiveStreamScheduler(
    private val streamUrlExtractor: StreamUrlExtractor,
    private val streamRecorder: StreamRecorder,
    private val ioCoroutineScope: CoroutineScope,
    private val channelRepository: ChannelRepository,
) {
    private val log = logger()

    @Scheduled(cron = "0 */5 * * * *")
    fun record() {
        val channels = channelRepository.findAll()

        for (channel in channels) {
            if (channel.status == RecordingStatus.RECORDING) {
                continue
            }

            val url = streamUrlExtractor.extractUrl(channel.channelName)
            if (url.isEmpty()) {
                continue
            }

            channel.record()
            channelRepository.save(channel)

            ioCoroutineScope.launch {
                try {
                    log.info("\"${channel.channelName}\"의 방송 녹화를 시작합니다.")
                    streamRecorder.record(url, channel.channelName)
                    log.info("\"${channel.channelName}\"의 방송 녹화를 중지합니다.")
                } finally {
                    channel.idle()
                    channelRepository.save(channel)
                }
            }
        }

    }
}