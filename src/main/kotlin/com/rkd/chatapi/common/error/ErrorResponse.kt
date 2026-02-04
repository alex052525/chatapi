package com.rkd.chatapi.common.error

data class ErrorResponse(
    val businessCode: String,
    val errorMessage: String,
    val errors: List<FieldError> = listOf()
) {
    data class FieldError(
        val field: String,
        val value: Any?,
        val reason: String
    )

    companion object {
        fun of(errorCode: ErrorCode, errors: List<FieldError> = listOf()): ErrorResponse {
            return ErrorResponse(
                businessCode = errorCode.code,
                errorMessage = errorCode.message,
                errors = errors
            )
        }
    }
}
