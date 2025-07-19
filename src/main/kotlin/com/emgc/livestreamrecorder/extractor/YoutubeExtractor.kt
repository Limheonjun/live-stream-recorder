package com.emgc.livestreamrecorder.extractor

import com.emgc.livestreamrecorder.extension.EMPTY
import com.emgc.livestreamrecorder.util.logger
import com.google.api.services.youtube.YouTube
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class YoutubeExtractor(
    private val youtube: YouTube,
    @Value("\${youtube.google-api.key}") private val apiKey: String,
) : StreamUrlExtractor {
    private val log = logger()

    override fun extractUrl(youtubeChannelId: String): String {
        val response = youtube.search().list("id,snippet")
            .apply {
                key = apiKey
                channelId = youtubeChannelId
                type = "video"
                maxResults = 20
                order = "date"
            }.execute()

        val liveStreamVideoId = response.items
            .firstOrNull { it.snippet.liveBroadcastContent == "live" }
            ?.id
            ?.videoId
            .orEmpty()

        if (liveStreamVideoId.isEmpty()) {
            return String.EMPTY
        }

        return "$YOUTUBE_BASE_URL$YOUTUBE_WATCH_PATH?$YOUTUBE_V_QUERY_STRING=$liveStreamVideoId"
    }

    companion object {
        private const val YOUTUBE_BASE_URL = "https://www.youtube.com"
        private const val YOUTUBE_WATCH_PATH = "/watch"
        private const val YOUTUBE_V_QUERY_STRING = "v"
    }
}