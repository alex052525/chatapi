package com.rkd.chatapi.user.service

import com.rkd.chatapi.user.domain.UserWriter
import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.validator.UserValidator
import org.springframework.stereotype.Service

@Service
class UserManagementService(
    private val userWriter: UserWriter,
    private val userValidator: UserValidator
) {
    fun createUserByApiKey(hashedApiKey: String, encryptedApiKey: String): Long {
        userValidator.validateUserNotRegistered(hashedApiKey)

        val user = User(apiKey = hashedApiKey, apiKeyEnc = encryptedApiKey)
        return userWriter.save(user).id!!
    }
}
