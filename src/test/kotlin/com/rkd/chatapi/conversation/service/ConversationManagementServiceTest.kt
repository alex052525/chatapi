package com.rkd.chatapi.conversation.service

import com.rkd.chatapi.conversation.domain.ConversationReader
import com.rkd.chatapi.conversation.domain.ConversationWriter
import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.dto.request.ConversationCreateRequest
import com.rkd.chatapi.user.domain.UserReader
import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.conversation.exception.ConversationAccessDeniedException
import com.rkd.chatapi.conversation.exception.ConversationNotExistException
import com.rkd.chatapi.user.exception.UserNotExistException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class ConversationManagementServiceTest {

    @InjectMocks
    private lateinit var conversationManagementService: ConversationManagementService

    @Mock
    private lateinit var userReader: UserReader

    @Mock
    private lateinit var conversationReader: ConversationReader

    @Mock
    private lateinit var conversationWriter: ConversationWriter

    @Test
    fun `createConversation returns conversation id`() {
        val user = User(apiKey = "hashed-key", apiKeyEnc = "enc-key").apply {
            id = 1L
        }
        val request = ConversationCreateRequest(title = "hello")
        whenever(userReader.findUserById(1L)).thenReturn(user)
        whenever(conversationWriter.save(any<Conversation>())).thenAnswer { invocation ->
            (invocation.arguments[0] as Conversation).apply { id = 10L }
        }

        val response = conversationManagementService.createConversation(1L, request)

        assertThat(response.conversationId).isEqualTo(10L)
    }

    @Test
    fun `createConversation throws when user not found`() {
        val request = ConversationCreateRequest(title = "hello")
        whenever(userReader.findUserById(1L)).thenThrow(UserNotExistException())

        org.junit.jupiter.api.assertThrows<UserNotExistException> {
            conversationManagementService.createConversation(1L, request)
        }
    }

    // --- deleteConversation ---

    @Test
    fun `deleteConversation deletes successfully`() {
        val owner = User(apiKey = "hashed-key", apiKeyEnc = "enc-key").apply { id = 1L }
        val conversation = Conversation(user = owner, title = "to delete").apply { id = 10L }

        whenever(conversationReader.findConversationById(10L)).thenReturn(conversation)

        conversationManagementService.deleteConversation(userId = 1L, conversationId = 10L)

        verify(conversationWriter).delete(conversation)
    }

    @Test
    fun `deleteConversation throws when conversation not found`() {
        whenever(conversationReader.findConversationById(10L)).thenThrow(ConversationNotExistException())

        org.junit.jupiter.api.assertThrows<ConversationNotExistException> {
            conversationManagementService.deleteConversation(userId = 1L, conversationId = 10L)
        }
    }

    @Test
    fun `deleteConversation throws when user is not owner`() {
        val owner = User(apiKey = "hashed-key", apiKeyEnc = "enc-key").apply { id = 1L }
        val otherUserId = 999L
        val conversation = Conversation(user = owner, title = "owner's chat").apply { id = 10L }

        whenever(conversationReader.findConversationById(10L)).thenReturn(conversation)

        org.junit.jupiter.api.assertThrows<ConversationAccessDeniedException> {
            conversationManagementService.deleteConversation(userId = otherUserId, conversationId = 10L)
        }
    }
}
