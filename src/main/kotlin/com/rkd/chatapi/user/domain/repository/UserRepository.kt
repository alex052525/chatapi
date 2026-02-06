package com.rkd.chatapi.user.domain.repository

import com.rkd.chatapi.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByApiKey(apiKey: String): User?
}
