package com.rkd.chatapi.user.exception

import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.exception.BusinessException

class UserNotExistException : BusinessException(ErrorCode.USER_NOT_FOUND) {
}