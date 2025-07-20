package com.emgc.livestreamrecorder.extractor

import com.emgc.livestreamrecorder.extension.EMPTY
import com.emgc.livestreamrecorder.util.logger
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Channel
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.Video
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class YoutubeExtractor(
    private val youtube: YouTube,
    @Value("\${youtube.google-api.key}") private val apiKey: String,
) : StreamUrlExtractor {
    private val log = logger()

    override fun extractUrl(channelId: String): String {
        try {
            // 1. 채널의 업로드 플레이리스트 ID 가져오기
            val uploadPlaylistId = getUploadPlaylistId(channelId)
                ?: String.EMPTY

            // 2. 업로드 플레이리스트에서 최신 동영상 ID 가져오기
            val latestVideoId = getLatestVideoIdFromPlaylist(uploadPlaylistId)
                ?: String.EMPTY

            // 3. 최신 동영상의 라이브 상태 확인
            return if (isVideoLive(latestVideoId)) {
                return "$YOUTUBE_BASE_URL$YOUTUBE_WATCH_PATH?$YOUTUBE_V_QUERY_STRING=$channelId"
            } else {
                String.EMPTY
            }

        } catch (e: Exception) {
            log.error("Error checking live status for channel $channelId: ${e.message}")
            return String.EMPTY
        }


    }

    private fun getUploadPlaylistId(channelId: String): String? {
        val channelsResponse = youtube.channels()
            .list("contentDetails")
            .apply {
                id = channelId
                key = apiKey
            }.execute()


        val channels: List<Channel>? = channelsResponse.items
        return if (!channels.isNullOrEmpty()) {
            channels[0].contentDetails?.relatedPlaylists?.uploads
        } else {
            null
        }
    }

    private fun getLatestVideoIdFromPlaylist(channelPlaylistId: String): String? {
        val playlistItemsResponse = youtube.playlistItems()
            .list("snippet")
            .apply {
                playlistId = channelPlaylistId
                maxResults = 1
                key = apiKey
            }
            .execute()


        val items: List<PlaylistItem>? = playlistItemsResponse.items
        return if (items != null && items.isNotEmpty()) {
            items[0].snippet?.resourceId?.videoId
        } else {
            null
        }
    }

    private fun isVideoLive(videoId: String): Boolean {
        val videosResponse = youtube.videos()
            .list("snippet,liveStreamingDetails")
            .apply {
                id = videoId
                key = apiKey
            }
            .execute()


        val videos: List<Video>? = videosResponse.items
        if (videos != null && videos.isNotEmpty()) {
            val video = videos[0]
            val liveBroadcastContent = video.snippet?.liveBroadcastContent
            // liveBroadcastContent는 "live", "upcoming", "none" 중 하나
            return liveBroadcastContent == "live"
        }
        return false
    }

    companion object {
        private const val YOUTUBE_BASE_URL = "https://www.youtube.com"
        private const val YOUTUBE_WATCH_PATH = "/watch"
        private const val YOUTUBE_V_QUERY_STRING = "v"
    }
}
