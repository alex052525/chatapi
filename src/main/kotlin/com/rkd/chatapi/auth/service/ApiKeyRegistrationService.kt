package com.rkd.chatapi.auth.service

import com.rkd.chatapi.auth.dto.response.ApiKeyRegisterResponse
import com.rkd.chatapi.auth.validator.ApiKeyValidator
import com.rkd.chatapi.common.security.ApiKeyHasher
import com.rkd.chatapi.user.service.UserService
import org.springframework.stereotype.Service

@Service
class ApiKeyRegistrationService(
    private val apiKeyHasher: ApiKeyHasher,
    private val apiKeyValidator: ApiKeyValidator,
    private val userService: UserService
) {
    fun registerApiKey(apiKey: String): ApiKeyRegisterResponse {
        apiKeyValidator.validateApiKey(apiKey)

        val hashedApiKey = apiKeyHasher.hash(apiKey)
        return ApiKeyRegisterResponse(userService.createUserByApiKey(hashedApiKey))
    }
}
