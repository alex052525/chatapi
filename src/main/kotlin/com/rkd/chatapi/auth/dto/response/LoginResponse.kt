package com.rkd.chatapi.auth.dto.response

data class LoginResponse(
    val userId: Long,
    val accessToken: String,
    val expiresInSeconds: Long
)
