package com.djawnstj.stomp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StompTestApplication

fun main(args: Array<String>) {
    runApplication<StompTestApplication>(*args)
}
