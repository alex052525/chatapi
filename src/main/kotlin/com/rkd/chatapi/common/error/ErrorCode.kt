package com.rkd.chatapi.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    INTERNAL_SERVER_ERROR("GLB0001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_DENIED("GLB0002", "Access denied", HttpStatus.UNAUTHORIZED),
    INPUT_INVALID_VALUE("GLB0003", "Invalid input value", HttpStatus.BAD_REQUEST),
    INVALID_API_KEY("AUTH0001", "Invalid API key", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXIST("AUTH0002", "이미 등록된 유저입니다.", HttpStatus.BAD_REQUEST)
}
