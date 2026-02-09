package com.rkd.chatapi.common.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date

@Service
class JwtTokenProvider(
    @Value("\${security.jwt-secret}") private val secret: String,
    @Value("\${security.jwt-issuer}") private val issuer: String,
    @Value("\${security.jwt-expiration-seconds}") private val expirationSeconds: Long
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun createToken(userId: Long): String {
        val now = Instant.now()
        val exp = now.plusSeconds(expirationSeconds)
        return Jwts.builder()
            .subject(userId.toString())
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun parseUserId(token: String): Long {
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
        return claims.subject.toLong()
    }

    fun getExpirationSeconds(): Long = expirationSeconds
}
