package com.djawnstj.stomp.chat.dto.request

import java.time.LocalDateTime

data class ChatRequest(
    val sender: String,
    val roomId: Long,
    val message: String = "",
    val sendTime: LocalDateTime = LocalDateTime.now()
)
