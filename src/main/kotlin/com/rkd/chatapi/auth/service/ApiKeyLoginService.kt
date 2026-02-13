package com.rkd.chatapi.auth.service

import com.rkd.chatapi.auth.validator.ApiKeyValidator
import com.rkd.chatapi.auth.dto.response.LoginResponse
import com.rkd.chatapi.common.security.ApiKeyHasher
import com.rkd.chatapi.common.security.JwtTokenProvider
import com.rkd.chatapi.user.service.UserInfoService
import org.springframework.stereotype.Service

@Service
class ApiKeyLoginService (
    private val apiKeyHasher: ApiKeyHasher,
    private val apiKeyValidator: ApiKeyValidator,
    private val userInfoService: UserInfoService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun login(apiKey: String): LoginResponse {
        apiKeyValidator.validateApiKey(apiKey)

        val hashedApiKey = apiKeyHasher.hash(apiKey)
        val userId = userInfoService.findUserByApiKey(hashedApiKey)
        val token = jwtTokenProvider.createToken(userId)
        return LoginResponse(
            userId = userId,
            accessToken = token,
            expiresInSeconds = jwtTokenProvider.getExpirationSeconds()
        )
    }
}
