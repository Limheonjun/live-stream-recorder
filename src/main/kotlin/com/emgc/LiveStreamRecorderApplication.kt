package com.emgc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class LiveStreamRecorderApplication

fun main(args: Array<String>) {
    runApplication<LiveStreamRecorderApplication>(*args)
}
