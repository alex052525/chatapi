package com.rkd.chatapi.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    INTERNAL_SERVER_ERROR("GLB0001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_DENIED("GLB0002", "Access denied", HttpStatus.UNAUTHORIZED),
    INPUT_INVALID_VALUE("GLB0003", "Invalid input value", HttpStatus.BAD_REQUEST)
}
