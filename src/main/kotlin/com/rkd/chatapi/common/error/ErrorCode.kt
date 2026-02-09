package com.rkd.chatapi.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    // common
    INTERNAL_SERVER_ERROR("GLB0001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_DENIED("GLB0002", "Access denied", HttpStatus.UNAUTHORIZED),
    INPUT_INVALID_VALUE("GLB0003", "Invalid input value", HttpStatus.BAD_REQUEST),

    // auth
    INVALID_API_KEY("AUTH0001", "유효하지 않은 API Key 입니다.", HttpStatus.BAD_REQUEST),

    // user
    USER_ALREADY_EXIST("USER0001", "이미 등록된 유저입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER0002", "유저를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
}
