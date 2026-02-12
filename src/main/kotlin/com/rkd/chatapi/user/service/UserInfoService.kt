package com.rkd.chatapi.user.service

import com.rkd.chatapi.user.domain.repository.UserRepository
import com.rkd.chatapi.user.validator.UserValidator
import org.springframework.stereotype.Service

@Service
class UserInfoService(
    private val userRepository: UserRepository,
    private val userValidator: UserValidator
) {
    fun findUserByApiKey(hashedApiKey: String): Long {
        userValidator.validateUserRegistered(hashedApiKey)

        return userRepository.findByApiKey(hashedApiKey)!!.id!!
    }
}
