package com.rkd.chatapi.auth.util

import com.rkd.chatapi.common.security.JwtAuthFilter
import com.rkd.chatapi.common.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class CookieUtil(
    private val jwtTokenProvider: JwtTokenProvider,
    @Value("\${security.jwt-cookie-secure}") private val cookieSecure: Boolean,
    @Value("\${security.jwt-cookie-path}") private val cookiePath: String
) {
    fun createAccessTokenCookie(
        userId: Long
    ): ResponseCookie {
        val token = jwtTokenProvider.createToken(userId)
        return ResponseCookie.from(JwtAuthFilter.ACCESS_TOKEN_COOKIE, token)
            .httpOnly(true)
            .secure(cookieSecure)
            .path(cookiePath)
            .maxAge(jwtTokenProvider.getExpirationSeconds())
            .build()
    }
}
