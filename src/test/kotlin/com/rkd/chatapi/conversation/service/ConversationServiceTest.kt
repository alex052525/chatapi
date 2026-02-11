package com.rkd.chatapi.conversation.service

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.conversation.dto.request.ConversationCreateRequest
import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.domain.repository.UserRepository
import com.rkd.chatapi.user.exception.UserNotExistException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ConversationServiceTest {

    @InjectMocks
    private lateinit var conversationService: ConversationService

    @Mock
    private lateinit var conversationRepository: ConversationRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `createConversation returns conversation id`() {
        val user = User().apply {
            id = 1L
            apiKey = "hashed-key"
        }
        val request = ConversationCreateRequest(title = "hello")
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(user))
        whenever(conversationRepository.save(any<Conversation>())).thenAnswer { invocation ->
            (invocation.arguments[0] as Conversation).apply { id = 10L }
        }

        val response = conversationService.createConversation(1L, request)

        assertThat(response.conversationId).isEqualTo(10L)
    }

    @Test
    fun `createConversation throws when user not found`() {
        val request = ConversationCreateRequest(title = "hello")
        whenever(userRepository.findById(1L)).thenReturn(Optional.empty())

        org.junit.jupiter.api.assertThrows<UserNotExistException> {
            conversationService.createConversation(1L, request)
        }
    }
}
