package com.rkd.chatapi.auth.exception

import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.exception.BusinessException

class ApiKeyNotExistException : BusinessException(ErrorCode.API_KEY_NOT_EXIST) {
}