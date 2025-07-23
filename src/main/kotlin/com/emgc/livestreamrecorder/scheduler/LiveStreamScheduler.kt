package com.emgc.livestreamrecorder.scheduler


import com.emgc.core.holder.ChannelHolder
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
    private val channelHolder: ChannelHolder,
    private val streamUrlExtractor: StreamUrlExtractor,
    private val streamRecorder: StreamRecorder,
    private val ioCoroutineScope: CoroutineScope,
) {
    private val log = logger()

    @Scheduled(cron = "0 */5 * * * *")
    fun record() {
        for (channelDetail in channelHolder.data) {
            if (channelDetail.recordingStatus == RecordingStatus.RECORDING) {
                continue
            }

            val channel = channelDetail.channel
            val url = streamUrlExtractor.extractUrl(channel.channelId)
            if (url.isEmpty()) {
                continue
            }

            ioCoroutineScope.launch {
                channelDetail.record()
                log.info("\"${channel.channelName}\"의 방송 녹화를 시작합니다.")
                streamRecorder.record(url, channel.channelName)
                log.info("\"${channel.channelName}\"의 방송 녹화를 중지합니다.")
                channelDetail.idle()
            }
        }

    }
}