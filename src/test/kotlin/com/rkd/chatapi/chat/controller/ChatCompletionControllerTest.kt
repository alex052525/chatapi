package com.rkd.chatapi.chat.controller

import com.rkd.chatapi.chat.service.ChatCompletionService
import com.rkd.chatapi.common.security.JwtAuthToken
import com.rkd.chatapi.chat.dto.request.ChatCompletionRequest
import com.rkd.chatapi.chat.dto.response.ChatCompletionResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class ChatCompletionControllerTest {

    @Mock
    private lateinit var chatCompletionService: ChatCompletionService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        val controller = ChatCompletionController(chatCompletionService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(AuthenticationPrincipalArgumentResolver())
            .build()
    }

    @Test
    fun `createCompletion returns messageId and answer`() {
        val userId = 1L
        val request = ChatCompletionRequest(conversationId = 10L, content = "hi")
        val response = ChatCompletionResponse(messageId = 100L, answer = "answer")

        whenever(chatCompletionService.completeChat(userId, request)).thenReturn(response)

        val authentication = JwtAuthToken(userId).apply { isAuthenticated = true }
        SecurityContextHolder.getContext().authentication = authentication

        try {
            mockMvc.post("/api/chat/completions") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"conversationId":10,"content":"hi"}"""
            }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.messageId") { value(100L) }
                    jsonPath("$.answer") { value("answer") }
                }
        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}
