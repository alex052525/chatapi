package com.rkd.chatapi.auth.controller

import com.rkd.chatapi.auth.dto.request.ApiKeyRegisterRequest
import com.rkd.chatapi.auth.dto.response.ApiKeyRegisterResponse
import com.rkd.chatapi.auth.service.ApiKeyRegistrationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/auth")
class ApiKeyAuthController(
    private val apiKeyRegistrationService: ApiKeyRegistrationService
) {

    @PostMapping("/apiKey")
    fun registerApiKey(@Valid @RequestBody request: ApiKeyRegisterRequest): ResponseEntity<ApiKeyRegisterResponse> {
        val userId = apiKeyRegistrationService.registerApiKey(request.apiKey)
        return ResponseEntity.ok(ApiKeyRegisterResponse(userId = userId))
    }
}
