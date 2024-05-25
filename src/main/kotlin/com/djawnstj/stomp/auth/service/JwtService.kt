package com.djawnstj.stomp.auth.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.djawnstj.stomp.auth.config.JwtProperties
import com.djawnstj.stomp.auth.entity.AuthenticationCredentials
import com.djawnstj.stomp.common.exception.ErrorCode
import com.djawnstj.stomp.common.exception.GlobalException
import com.djawnstj.stomp.common.provider.Base64Provider
import com.djawnstj.stomp.common.provider.TimeProvider
import com.djawnstj.stomp.common.provider.UUIDProvider
import com.djawnstj.stomp.member.entity.Member
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    private val tokenProvider: TokenProvider,
    private val jwtProperties: JwtProperties,
    private val objectMapper: ObjectMapper,
    private val base64Provider: Base64Provider,
    private val uuidProvider: UUIDProvider,
    private val timeProvider: TimeProvider,
) {

    fun getUsername(token: String): String = tokenProvider.extractUsername(token).isValidClaimFromToken()

    fun getJti(token: String): String = tokenProvider.extractJti(token).isValidClaimFromToken()

    private fun String?.isValidClaimFromToken(): String =
        if (isNullOrBlank()) throw GlobalException(ErrorCode.EMPTY_CLAIM)
        else this

    fun generateAuthenticationCredentials(member: Member): AuthenticationCredentials {
        val jti = uuidProvider.generateUuidString()

        val accessToken = generateAccessToken(member, jti)
        val refreshToken = generateRefreshToken(member, jti)

        return AuthenticationCredentials(jti, accessToken, refreshToken)
    }

    fun generateAccessToken(userDetails: UserDetails, jti: String): String =
        tokenProvider.buildToken(userDetails, jti, jwtProperties.accessTokenExpiration)

    fun generateRefreshToken(userDetails: UserDetails, jti: String): String =
        tokenProvider.buildToken(userDetails, jti, jwtProperties.refreshTokenExpiration)

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
        (getUsername(token) == userDetails.username) && isTokenActive(token)

    private fun isTokenActive(token: String): Boolean = tokenProvider.extractExpiration(token)?.after(Date()) ?: false

    fun checkTokenExpiredByTokenString(token: String): Boolean {
        val parts = token.split(".")

        if (parts.size != 3) throw GlobalException(ErrorCode.INVALID_ACCESS_TOKEN)

        val payload = String(base64Provider.decode(parts[1]))

        val expiration =
            objectMapper.readValue(payload, object : TypeReference<MutableMap<String, String>>() {})["exp"]?.toLong()
            ?: throw GlobalException(ErrorCode.INVALID_ACCESS_TOKEN)

        val current = timeProvider.getCurrentTimeMillis() / 1000

        return expiration <= current
    }

}
