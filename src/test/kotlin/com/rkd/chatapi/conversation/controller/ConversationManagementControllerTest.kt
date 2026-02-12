package com.rkd.chatapi.conversation.controller

import com.rkd.chatapi.common.security.JwtAuthToken
import com.rkd.chatapi.conversation.dto.request.ConversationCreateRequest
import com.rkd.chatapi.conversation.dto.response.ConversationCreateResponse
import com.rkd.chatapi.conversation.service.ConversationManagementService
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
class ConversationManagementControllerTest {

    @Mock
    private lateinit var conversationManagementService: ConversationManagementService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        val controller = ConversationManagementController(conversationManagementService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(AuthenticationPrincipalArgumentResolver())
            .build()
    }

    @Test
    fun `create conversation returns id`() {
        val userId = 1L
        val title = "hello"
        val conversationId = 10L

        val request = ConversationCreateRequest(title)
        whenever(conversationManagementService.createConversation(userId, request))
            .thenReturn(ConversationCreateResponse(conversationId))

        val authentication = JwtAuthToken(userId).apply { isAuthenticated = true }
        SecurityContextHolder.getContext().authentication = authentication

        try {
            mockMvc.post("/api/conversations") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"title":"$title"}"""
            }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.conversationId") { value(conversationId) }
                }
        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}
