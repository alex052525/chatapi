package com.rkd.chatapi.auth.service

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
    fun registerApiKey(apiKey: String): Long {
        apiKeyValidator.validateApiKey(apiKey)

        val hashed = apiKeyHasher.hash(apiKey)
        return userService.createUserByApiKey(hashed)
    }
}
