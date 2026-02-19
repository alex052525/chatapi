package com.rkd.chatapi.user.domain

import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.domain.repository.UserRepository
import com.rkd.chatapi.user.exception.UserNotExistException
import org.springframework.stereotype.Component

@Component
class UserReader(
    private val userRepository: UserRepository
) {
    fun findUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { UserNotExistException() }
    }

    fun findUserByApiKey(hashedApiKey: String): User? {
        return userRepository.findByApiKey(hashedApiKey)
    }
}
