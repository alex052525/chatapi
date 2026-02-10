package com.rkd.chatapi.auth.util

import com.rkd.chatapi.common.security.JwtAuthFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class CookieUtil(
    @Value("\${security.jwt-cookie-secure}") private val cookieSecure: Boolean,
    @Value("\${security.jwt-cookie-path}") private val cookiePath: String,
    @Value("\${security.jwt-expiration-seconds}") private val expirationSeconds: Long
) {
    fun createAccessTokenCookie(
        token: String
    ): ResponseCookie {
        return ResponseCookie.from(JwtAuthFilter.ACCESS_TOKEN_COOKIE, token)
            .httpOnly(true)
            .secure(cookieSecure)
            .path(cookiePath)
            .maxAge(expirationSeconds)
            .build()
    }
}
