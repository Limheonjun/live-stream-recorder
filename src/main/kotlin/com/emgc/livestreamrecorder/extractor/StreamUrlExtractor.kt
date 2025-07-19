package com.emgc.livestreamrecorder.extractor

interface StreamUrlExtractor {
    fun extractUrl(channelId: String): String
}