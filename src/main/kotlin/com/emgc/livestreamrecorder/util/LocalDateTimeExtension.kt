package com.emgc.livestreamrecorder.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toYyyyMMddHHmmss(): String {
    val pattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    return this.format(pattern)
}