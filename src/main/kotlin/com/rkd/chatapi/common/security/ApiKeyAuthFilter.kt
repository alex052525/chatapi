package com.rkd.chatapi.common.security

import com.rkd.chatapi.auth.exception.ApiKeyInvalidException
import com.rkd.chatapi.auth.validator.ApiKeyValidator
import com.rkd.chatapi.auth.exception.ApiKeyNotExistException
import com.rkd.chatapi.user.domain.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyAuthFilter(
    private val userRepository: UserRepository,
    private val apiKeyHasher: ApiKeyHasher,
    private val apiKeyValidator: ApiKeyValidator
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
        val apiKey = extractApiKey(request) ?: throw ApiKeyNotExistException()

        if (isRegistrationPath(request)) {
            apiKeyValidator.validateApiKey(apiKey)
            filterChain.doFilter(request, response)
            return
        }

        val userId = resolveUserId(apiKey) ?: throw ApiKeyInvalidException()
        setAuthentication(userId)
        filterChain.doFilter(request, response)
    }

    private fun extractApiKey(request: HttpServletRequest): String? {
        val apiKey = request.getHeader("X-API-KEY")
        return if (apiKey.isNullOrBlank()) null else apiKey
    }

    private fun isRegistrationPath(request: HttpServletRequest): Boolean {
        val path = request.servletPath ?: ""
        return path == "/api/auth/apiKey"
    }

    private fun resolveUserId(apiKey: String): Long? {
        val hashed = apiKeyHasher.hash(apiKey)
        val user = userRepository.findByApiKey(hashed)
        return user?.id
    }

    private fun setAuthentication(userId: Long) {
        val authentication = ApiKeyAuthToken(userId)
        authentication.isAuthenticated = true
        SecurityContextHolder.getContext().authentication = authentication
    }

}
