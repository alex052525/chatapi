package com.rkd.chatapi.auth.controller

import com.rkd.chatapi.auth.service.ApiKeyManagementService
import com.rkd.chatapi.auth.service.ApiKeyLoginService
import com.rkd.chatapi.auth.util.CookieUtil
import com.rkd.chatapi.common.security.JwtAuthFilter
import com.rkd.chatapi.auth.dto.response.ApiKeyRegisterResponse
import com.rkd.chatapi.auth.dto.response.LoginResponse
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class ApiKeyAuthControllerTest {

    @Mock
    private lateinit var apiKeyManagementService: ApiKeyManagementService

    @Mock
    private lateinit var apiKeyLoginService: ApiKeyLoginService

    @Mock
    private lateinit var cookieUtil: CookieUtil

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        val controller = ApiKeyAuthController(
            apiKeyManagementService = apiKeyManagementService,
            apiKeyLoginService = apiKeyLoginService,
            cookieUtil = cookieUtil
        )
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `login returns token and sets cookie`() {
        val userId = 1L
        val token = "token-value"
        val cookie = ResponseCookie.from(JwtAuthFilter.ACCESS_TOKEN_COOKIE, token)
            .httpOnly(true)
            .path("/")
            .build()

        whenever(apiKeyLoginService.login("valid-api-key")).thenReturn(
            LoginResponse(
                userId = userId,
                accessToken = token,
                expiresInSeconds = 900
            )
        )
        whenever(cookieUtil.createAccessTokenCookie(token)).thenReturn(cookie)

        mockMvc.get("/api/auth/login") {
            header("X-API-KEY", "valid-api-key")
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                header { string("Set-Cookie", containsString("${JwtAuthFilter.ACCESS_TOKEN_COOKIE}=$token")) }
                jsonPath("$.userId") { value(userId) }
                jsonPath("$.accessToken") { value(token) }
                jsonPath("$.expiresInSeconds") { value(900) }
            }
    }

    @Test
    fun `registerApiKey returns user id`() {
        val userId = 1L
        whenever(apiKeyManagementService.registerApiKey("valid-api-key"))
            .thenReturn(ApiKeyRegisterResponse(userId = userId))

        mockMvc.post("/api/auth/apiKey") {
            header("X-API-KEY", "valid-api-key")
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.userId") { value(userId) }
            }
    }
}
