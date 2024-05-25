package com.djawnstj.stomp.chat.api

import com.djawnstj.stomp.chat.dto.request.ChatRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionConnectEvent

@Controller
class ChatController(
    private val template: SimpMessagingTemplate
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatController::class.java)
    }

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectEvent?) {
        log.debug("Received a new web socket connection")
    }

    @MessageMapping("/chat/enter")
    fun enter(request: ChatRequest) {
        log.debug("enter called - {}", request)
        val enterNotify = "${request.sender} 님이 입장하셨습니다."
        template.convertAndSend("/sub/chat/${request.roomId}", enterNotify)
    }

    @MessageMapping("/message")
    fun message(request: ChatRequest) {
        log.debug("message called - {}", request)
        template.convertAndSend("/sub/chat/${request.roomId}", request)
    }

}
