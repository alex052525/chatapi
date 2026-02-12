package com.rkd.chatapi.auth.service

import com.rkd.chatapi.auth.dto.response.ApiKeyRegisterResponse
import com.rkd.chatapi.auth.validator.ApiKeyValidator
import com.rkd.chatapi.common.security.ApiKeyEncryptor
import com.rkd.chatapi.common.security.ApiKeyHasher
import com.rkd.chatapi.user.service.UserManagementService
import org.springframework.stereotype.Service

@Service
class ApiKeyManagementService(
    private val apiKeyHasher: ApiKeyHasher,
    private val apiKeyValidator: ApiKeyValidator,
    private val apiKeyEncryptor: ApiKeyEncryptor,
    private val userManagementService: UserManagementService
) {
    fun registerApiKey(apiKey: String): ApiKeyRegisterResponse {
        apiKeyValidator.validateApiKey(apiKey)

        val hashedApiKey = apiKeyHasher.hash(apiKey)
        val encryptedApiKey = apiKeyEncryptor.encrypt(apiKey)
        return ApiKeyRegisterResponse(userManagementService.createUserByApiKey(hashedApiKey, encryptedApiKey))
    }
}
