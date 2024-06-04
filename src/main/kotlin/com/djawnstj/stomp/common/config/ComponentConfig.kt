package com.djawnstj.stomp.common.config

import com.djawnstj.stomp.common.provider.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ComponentConfig {

    @Bean
    fun Base64Provider(): Base64Provider = JavaBase64Provider()

    @Bean
    fun uuidProvider(): UUIDProvider = JavaUUIDProvider()

    @Bean
    fun timeProvider(): TimeProvider = ZonedDateTimeProvider()

}
