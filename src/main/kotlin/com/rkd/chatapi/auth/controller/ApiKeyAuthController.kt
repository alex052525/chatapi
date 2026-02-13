package com.rkd.chatapi.auth.controller

import com.rkd.chatapi.auth.dto.response.ApiKeyRegisterResponse
import com.rkd.chatapi.auth.dto.response.LoginResponse
import com.rkd.chatapi.auth.service.ApiKeyManagementService
import com.rkd.chatapi.auth.service.ApiKeyLoginService
import com.rkd.chatapi.auth.util.CookieUtil
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class ApiKeyAuthController(
    private val apiKeyManagementService: ApiKeyManagementService,
    private val apiKeyLoginService: ApiKeyLoginService,
    private val cookieUtil: CookieUtil
) {

    @PostMapping("/apiKey")
    fun registerApiKey(@RequestHeader("X-API-KEY") apiKey: String): ResponseEntity<ApiKeyRegisterResponse> {
        val registerApiKeyResponse = apiKeyManagementService.registerApiKey(apiKey)
        return ResponseEntity.ok(registerApiKeyResponse)
    }

    @GetMapping("/login")
    fun login(@RequestHeader("X-API-KEY") apiKey: String): ResponseEntity<LoginResponse> {
        val loginResponse = apiKeyLoginService.login(apiKey)
        val cookie = cookieUtil.createAccessTokenCookie(token = loginResponse.accessToken)
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(loginResponse)
    }
}
