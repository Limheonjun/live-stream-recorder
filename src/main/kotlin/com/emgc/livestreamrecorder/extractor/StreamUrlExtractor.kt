package com.emgc.livestreamrecorder.extractor

interface StreamUrlExtractor {
    fun extractUrl(channelName: String): String
}