package com.djawnstj.stomp.auth.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.djawnstj.stomp.auth.config.JwtProperties
import com.djawnstj.stomp.auth.entity.AuthenticationCredentials
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

typealias Token = AuthenticationCredentials

@Repository
class TokenRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val jwtProperties: JwtProperties,
) {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    private val valueOperations: ValueOperations<String, String>
        get() = this.redisTemplate.opsForValue()

    fun save(authenticationCredentials: Token): Token {
        valueOperations[authenticationCredentials.jti] = objectMapper.writeValueAsString(authenticationCredentials)

        valueOperations.getAndExpire(authenticationCredentials.jti, jwtProperties.refreshTokenExpiration, TimeUnit.MILLISECONDS)
        
        return authenticationCredentials
    }

    fun findByJti(jti: String): Token? =
        valueOperations[jti]?.let { objectMapper.readValue(it, Token::class.java) } ?: run { null }

    fun deleteByJti(jti: String) {
        redisTemplate.delete(jti)
    }

    fun deleteAll() {
        redisTemplate.delete(redisTemplate.keys("*"))
    }
}
