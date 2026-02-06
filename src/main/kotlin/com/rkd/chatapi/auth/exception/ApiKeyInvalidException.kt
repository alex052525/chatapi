package com.rkd.chatapi.auth.exception

import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.exception.BusinessException

class ApiKeyInvalidException : BusinessException(ErrorCode.INVALID_API_KEY) {
}