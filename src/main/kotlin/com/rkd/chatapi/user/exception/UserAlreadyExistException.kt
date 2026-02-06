package com.rkd.chatapi.user.exception

import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.exception.BusinessException

class UserAlreadyExistException : BusinessException(ErrorCode.USER_ALREADY_EXIST) {
}