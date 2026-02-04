package com.rkd.chatapi.common.error.exception

import com.rkd.chatapi.common.error.ErrorCode

open class BusinessException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)
