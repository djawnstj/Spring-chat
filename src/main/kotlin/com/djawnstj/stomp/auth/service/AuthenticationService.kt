package com.djawnstj.stomp.auth.service

import com.djawnstj.stomp.auth.dto.refresh.TokenRefreshRequest
import com.djawnstj.stomp.auth.dto.signin.LogInRequest
import com.djawnstj.stomp.auth.dto.signin.LogInResponse
import com.djawnstj.stomp.auth.entity.AuthenticationCredentials
import com.djawnstj.stomp.auth.repository.TokenRedisRepository
import com.djawnstj.stomp.common.exception.ErrorCode
import com.djawnstj.stomp.common.exception.GlobalException
import com.djawnstj.stomp.member.entity.Member
import org.djawnstj.store.auth.dto.refresh.TokenRefreshResponse
import com.djawnstj.stomp.member.repository.MemberQueryRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthenticationService(
    private val tokenRepository: TokenRedisRepository,
    private val memberQueryRepository: MemberQueryRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {

    @Transactional
    fun logIn(request: LogInRequest): LogInResponse {
        val member: Member = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.loginId, request.loginPassword)
        ).principal as Member

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)

        tokenRepository.save(authenticationCredentials)

        return authenticationCredentials.run { LogInResponse(accessToken, refreshToken) }
    }

    @Transactional
    fun refreshToken(request: TokenRefreshRequest): TokenRefreshResponse {
        val presentedRefreshToken = request.refreshToken

        val phoneNumber: String = jwtService.getUsername(presentedRefreshToken!!)
        val jti = jwtService.getJti(presentedRefreshToken)

        val member = memberQueryRepository.findByPhoneNumber(phoneNumber).isValidMember()

        val foundTokens = tokenRepository.findByJti(jti).isValidToken()

        validateRefreshToken(presentedRefreshToken, foundTokens.refreshToken, member)

        validateActiveAccessToken(foundTokens.accessToken, jti)

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)

        tokenRepository.save(authenticationCredentials)

        return authenticationCredentials.run { TokenRefreshResponse(accessToken, refreshToken) }
    }

    private fun validateActiveAccessToken(accessToken: String, jti: String) {
        if (!jwtService.checkTokenExpiredByTokenString(accessToken)) {
            tokenRepository.deleteByJti(jti)
            throw GlobalException(ErrorCode.INVALID_TOKEN_REISSUE_REQUEST)
        }
    }

    private fun validateRefreshToken(jwt: String, refreshToken: String, user: UserDetails) {
        if (jwt != refreshToken) throw GlobalException(ErrorCode.MISS_MATCH_TOKEN)
        if (!jwtService.isTokenValid(jwt, user)) throw GlobalException(ErrorCode.INVALID_REFRESH_TOKEN)
    }

    private fun Member?.isValidMember(): Member = this ?: throw GlobalException(ErrorCode.MEMBER_NOT_FOUND)

    private fun AuthenticationCredentials?.isValidToken(): AuthenticationCredentials =
        this?.let { this } ?: throw throw GlobalException(ErrorCode.TOKEN_NOT_FOUND)

}



