package com.rkd.chatapi.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class ApiKeyRegisterRequest(
    @field:NotBlank(message = "apiKey is required")
    val apiKey: String
)
