package com.rkd.chatapi.common.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.rkd.chatapi.common.error.ErrorCode
import com.rkd.chatapi.common.error.ErrorResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyAuthFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath ?: ""
        return path != "/api/auth/login" && path != "/api/auth/apiKey"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val apiKey = request.getHeader("X-API-KEY")
        if (apiKey.isNullOrBlank()) {
            writeError(response, ErrorCode.API_KEY_NOT_EXIST)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun writeError(response: HttpServletResponse, errorCode: ErrorCode) {
        response.status = errorCode.httpStatus.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        objectMapper.writeValue(response.writer, ErrorResponse.of(errorCode))
    }
}
