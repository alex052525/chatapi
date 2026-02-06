package com.rkd.chatapi.user.service

import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun findOrCreateUserByApiKey(hashedApiKey: String): Long {
        val existing = userRepository.findByApiKey(hashedApiKey)
        if (existing != null && existing.id != null) {
            return existing.id!!
        }

        val user = User().apply {
            this.apiKey = hashedApiKey
        }
        return userRepository.save(user).id!!
    }
}