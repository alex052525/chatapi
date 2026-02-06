package com.rkd.chatapi.user.validator

import com.rkd.chatapi.user.exception.UserAlreadyExistException
import com.rkd.chatapi.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component

@Component
class UserValidator (
    private val userRepository: UserRepository
) {
    fun validateUserNotRegistered(hashedApiKey: String) {
        val existing = userRepository.findByApiKey(hashedApiKey)
        if (existing != null && existing.id != null) {
            throw UserAlreadyExistException()
        }
    }
}