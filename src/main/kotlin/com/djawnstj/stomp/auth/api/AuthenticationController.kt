package com.djawnstj.stomp.auth.api

import com.djawnstj.stomp.common.api.Response
import jakarta.validation.Valid
import com.djawnstj.stomp.auth.dto.refresh.TokenRefreshRequest
import org.djawnstj.store.auth.dto.refresh.TokenRefreshResponse
import com.djawnstj.stomp.auth.dto.signin.LogInRequest
import com.djawnstj.stomp.auth.dto.signin.LogInResponse
import com.djawnstj.stomp.auth.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/api/v1/auth/log-in")
    fun logIn(@Valid @RequestBody request: LogInRequest): Response<LogInResponse> =
        Response.success(authenticationService.logIn(request))

    @PostMapping("/api/v1/auth/refresh")
    fun refresh(@Valid @RequestBody request: TokenRefreshRequest): Response<TokenRefreshResponse> =
        Response.success(authenticationService.refreshToken(request))

}
