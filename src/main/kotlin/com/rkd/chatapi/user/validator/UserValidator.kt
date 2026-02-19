package com.rkd.chatapi.user.validator

import com.rkd.chatapi.user.domain.UserReader
import com.rkd.chatapi.user.exception.UserAlreadyExistException
import com.rkd.chatapi.user.exception.UserNotExistException
import org.springframework.stereotype.Component

@Component
class UserValidator(
    private val userReader: UserReader
) {
    fun validateUserNotRegistered(hashedApiKey: String) {
        val existing = userReader.findUserByApiKey(hashedApiKey)
        if (existing?.id != null) {
            throw UserAlreadyExistException()
        }
    }

    fun validateUserRegistered(hashedApiKey: String) {
        val existing = userReader.findUserByApiKey(hashedApiKey)
        if (existing?.id == null) {
            throw UserNotExistException()
        }
    }
}
