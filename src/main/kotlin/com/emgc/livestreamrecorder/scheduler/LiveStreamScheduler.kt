package com.emgc.livestreamrecorder.scheduler

import com.emgc.livestreamrecorder.config.Channel
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
    private val channels: List<Channel>,
    private val streamUrlExtractor: StreamUrlExtractor,
    private val streamRecorder: StreamRecorder,
    private val ioCoroutineScope: CoroutineScope
) {
    private val log = logger()

    @Scheduled(cron = "0 */1 * * * *")
    fun record() {
        for (channel in channels) {
            if (channel.status == RecordingStatus.RECORDING) {
                continue
            }

            val url = streamUrlExtractor.extractUrl(channel.channelId)
            if (url.isEmpty()) {
                continue
            }

            ioCoroutineScope.launch {
                log.info("\"${channel.channelName}\"의 방송 녹화를 시작합니다.")
                channel.record()
                streamRecorder.record(url, channel.channelName)
                channel.idle()
                log.info("\"${channel.channelName}\"의 방송 녹화를 중지합니다.")
            }
        }

    }
}