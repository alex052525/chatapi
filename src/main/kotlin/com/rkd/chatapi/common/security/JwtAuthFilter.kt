package com.rkd.chatapi.common.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.rkd.chatapi.common.error.ErrorResponse
import com.rkd.chatapi.common.error.exception.BusinessException
import com.rkd.chatapi.common.security.exception.AccessTokenInvalidException
import com.rkd.chatapi.common.security.exception.AccessTokenNotExistException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    companion object {
        const val ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN_COOKIE"
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath ?: ""
        return !path.startsWith("/api/") || path.startsWith("/api/auth/")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = extractToken(request) ?: throw AccessTokenNotExistException()

            val userId = parseUserId(token) ?: throw AccessTokenInvalidException()

            setAuthentication(userId)
            filterChain.doFilter(request, response)
        } catch (ex: BusinessException) {
            val errorCode = ex.errorCode
            response.status = errorCode.httpStatus.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            objectMapper.writeValue(response.writer, ErrorResponse.of(errorCode))
        }
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val token = request.cookies
            ?.firstOrNull { it.name == ACCESS_TOKEN_COOKIE }
            ?.value
        return if (token.isNullOrBlank()) null else token
    }

    private fun parseUserId(token: String): Long? {
        return try {
            jwtTokenProvider.parseUserId(token)
        } catch (ex: Exception) {
            null
        }
    }

    private fun setAuthentication(userId: Long) {
        val authentication = JwtAuthToken(userId)
        authentication.isAuthenticated = true
        SecurityContextHolder.getContext().authentication = authentication
    }

}
