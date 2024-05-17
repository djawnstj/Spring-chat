package com.djawnstj.stomp.config;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@EnableWebSocketMessageBroker
@Configuration
class StompConfig: WebSocketMessageBrokerConfigurer {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(StompConfig::class.java)
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        log.debug("configureMessageBroker called.")

        // 메시지 전송 URL
        registry.setApplicationDestinationPrefixes("/pub") // 구독자 -> 서버(메세지보낼때)
        // 메시지 구독 URL
        registry.enableSimpleBroker("/sub") // 브로커 -> 구독자들(메세지받을때)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        log.debug("registerStompEndpoints called.")

        registry.addEndpoint("/chat")
            .setAllowedOriginPatterns("*")
//            .withSockJS()
    }
}
