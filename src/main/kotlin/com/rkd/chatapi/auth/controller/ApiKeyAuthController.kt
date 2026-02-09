package com.rkd.chatapi.auth.controller

import com.rkd.chatapi.auth.dto.response.ApiKeyRegisterResponse
import com.rkd.chatapi.auth.dto.response.LoginResponse
import com.rkd.chatapi.auth.service.ApiKeyRegistrationService
import com.rkd.chatapi.auth.service.AuthUserService
import com.rkd.chatapi.auth.util.CookieUtil
import com.rkd.chatapi.common.security.JwtTokenProvider
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
    private val apiKeyRegistrationService: ApiKeyRegistrationService,
    private val authUserService: AuthUserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val cookieUtil: CookieUtil
) {

    @PostMapping("/apiKey")
    fun registerApiKey(@RequestHeader("X-API-KEY") apiKey: String): ResponseEntity<ApiKeyRegisterResponse> {
        val userId = apiKeyRegistrationService.registerApiKey(apiKey)
        return ResponseEntity.ok(ApiKeyRegisterResponse(userId = userId))
    }

    @GetMapping("/login")
    fun login(@RequestHeader("X-API-KEY") apiKey: String): ResponseEntity<LoginResponse> {
        val userId = authUserService.login(apiKey)
        val token = jwtTokenProvider.createToken(userId)
        val cookie = cookieUtil.createAccessTokenCookie(userId = userId)
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(
                LoginResponse(
                    userId = userId,
                    accessToken = token,
                    expiresInSeconds = jwtTokenProvider.getExpirationSeconds()
                )
            )
    }
}
