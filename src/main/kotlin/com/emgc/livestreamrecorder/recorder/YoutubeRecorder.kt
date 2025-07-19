package com.emgc.livestreamrecorder.recorder

import com.emgc.livestreamrecorder.constants.YtDlp
import com.emgc.livestreamrecorder.util.logger
import com.emgc.livestreamrecorder.util.toYyyyMMddHHmmss
import org.springframework.stereotype.Component
import java.io.IOException
import java.time.LocalDateTime

@Component
class YoutubeRecorder: StreamRecorder {
    private val log = logger()

    override fun record(url: String, channelName: String) {
        val args = buildArgs(url, channelName)

        val exitCode = try {
            ProcessBuilder(args)
                .redirectErrorStream(true)
                .start()
                .consumeAndLog()
        } catch (e: IOException) {
            throw RuntimeException("Failed to start yt-dlp process", e)
        }

        if (exitCode != 0) {
            throw RuntimeException("yt-dlp exited with code $exitCode")
        }
    }

    private fun buildArgs(url: String, channelName: String): List<String> = listOf(
        YtDlp.YT_DLP,
        YtDlp.HLS_USE_MPEG,
        YtDlp.NO_PART,
        YtDlp.OUTPUT,
        "${DEFAULT_PATH}/${channelName}/${LocalDateTime.now().toYyyyMMddHHmmss()}_${channelName}.%(ext)s",
        YtDlp.MERGE_OUTPUT_FORMAT,
        "mp4",
        url
    ).apply { log.info(this.get(4)) }

    private fun Process.consumeAndLog(): Int = apply {
        inputStream.bufferedReader().forEachLine {
            val threadName = Thread.currentThread().name
            log.debug("$LOG_PREFIX [$threadName] $it")
        }
    }.waitFor()

    companion object {
        private const val DEFAULT_PATH = "/live-stream-recordings"
        private const val LOG_PREFIX = "[yt-dlp]"
    }
}