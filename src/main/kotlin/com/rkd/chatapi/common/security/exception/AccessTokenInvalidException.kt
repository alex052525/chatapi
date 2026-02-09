package com.rkd.chatapi.common.security.exception

import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.exception.BusinessException

class AccessTokenInvalidException : BusinessException(ErrorCode.INVALID_ACCESS_TOKEN) {
}