package com.djawnstj.stomp.auth.dto.signin

data class LogInResponse(
    val accessToken: String,
    val refreshToken: String
)
