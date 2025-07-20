package com.emgc.livestreamrecorder.extractor

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class YoutubeExtractor : StreamUrlExtractor {

    override fun extractUrl(channelName: String): String {
        return "${YOUTUBE_BASE_URL}/${channelName}/${YOUTUBE_LIVE_PATH}"
    }

    companion object {
        private const val YOUTUBE_BASE_URL = "https://www.youtube.com"
        private const val YOUTUBE_LIVE_PATH = "/live"
    }
}