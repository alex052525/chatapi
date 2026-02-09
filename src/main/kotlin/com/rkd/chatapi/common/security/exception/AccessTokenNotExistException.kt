package com.rkd.chatapi.common.security.exception

import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.exception.BusinessException

class AccessTokenNotExistException : BusinessException(ErrorCode.ACCESS_TOKEN_NOT_EXIST) {
}