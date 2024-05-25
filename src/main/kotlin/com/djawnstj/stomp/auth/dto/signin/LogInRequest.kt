package com.djawnstj.stomp.auth.dto.signin

import jakarta.validation.constraints.NotBlank

data class LogInRequest(
    @field:NotBlank(message = "ID 는 필수로 입력하셔야 합니다.")
    var loginId: String?,
    @field:NotBlank(message = "비밀번호는 필수로 입력하셔야 합니다.")
    var loginPassword: String?,
)
