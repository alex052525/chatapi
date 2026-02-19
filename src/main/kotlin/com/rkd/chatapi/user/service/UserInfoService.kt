package com.rkd.chatapi.user.service

import com.rkd.chatapi.user.domain.UserReader
import com.rkd.chatapi.user.validator.UserValidator
import org.springframework.stereotype.Service

@Service
class UserInfoService(
    private val userReader: UserReader,
    private val userValidator: UserValidator
) {
    fun findUserByApiKey(hashedApiKey: String): Long {
        userValidator.validateUserRegistered(hashedApiKey)

        return userReader.findUserByApiKey(hashedApiKey)!!.id!!
    }
}
