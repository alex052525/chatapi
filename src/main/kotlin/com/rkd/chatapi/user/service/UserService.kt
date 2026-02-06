package com.rkd.chatapi.user.service

import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.domain.repository.UserRepository
import com.rkd.chatapi.user.exception.ApiKeyAlreadyExistException
import com.rkd.chatapi.user.validator.UserValidator
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userValidator: UserValidator
) {
    fun createUserByApiKey(hashedApiKey: String): Long {
        userValidator.validateUserNotRegistered(hashedApiKey)

        val user = User().apply {
            this.apiKey = hashedApiKey
        }
        return userRepository.save(user).id!!
    }
}