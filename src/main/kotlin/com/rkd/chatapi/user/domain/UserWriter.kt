package com.rkd.chatapi.user.domain

import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.domain.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserWriter(
    private val userRepository: UserRepository
) {
    fun save(user: User): User {
        return userRepository.save(user)
    }
}
