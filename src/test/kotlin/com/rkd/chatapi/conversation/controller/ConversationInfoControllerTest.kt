package com.rkd.chatapi.conversation.controller

import com.rkd.chatapi.common.security.JwtAuthToken
import com.rkd.chatapi.conversation.dto.response.ConversationInfoResponse
import com.rkd.chatapi.conversation.dto.response.ConversationListResponse
import com.rkd.chatapi.conversation.service.ConversationInfoService
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
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class ConversationInfoControllerTest {

    @Mock
    private lateinit var conversationInfoService: ConversationInfoService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        val controller = ConversationInfoController(conversationInfoService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(AuthenticationPrincipalArgumentResolver())
            .build()
    }

    @Test
    fun `getConversations returns list`() {
        val userId = 1L
        val response = ConversationListResponse(
            conversations = listOf(
                ConversationInfoResponse(title = "first"),
                ConversationInfoResponse(title = "second")
            ),
            nextCursor = null,
            hasNext = false
        )
        whenever(conversationInfoService.getConversations(userId, null, 10))
            .thenReturn(response)

        val authentication = JwtAuthToken(userId).apply { isAuthenticated = true }
        SecurityContextHolder.getContext().authentication = authentication

        try {
            mockMvc.get("/api/conversations") {
                param("size", "10")
            }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.conversations.length()") { value(2) }
                    jsonPath("$.conversations[0].title") { value("first") }
                    jsonPath("$.conversations[1].title") { value("second") }
                    jsonPath("$.hasNext") { value(false) }
                    jsonPath("$.nextCursor") { doesNotExist() }
                }
        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}
