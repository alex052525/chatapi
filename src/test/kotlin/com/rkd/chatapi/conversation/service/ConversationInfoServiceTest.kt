package com.rkd.chatapi.conversation.service

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.message.domain.MessageRole
import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.message.domain.repository.MessageRepository
import com.rkd.chatapi.user.domain.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageRequest
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ConversationInfoServiceTest {

    @InjectMocks
    private lateinit var conversationInfoService: ConversationInfoService

    @Mock
    private lateinit var conversationRepository: ConversationRepository

    @Mock
    private lateinit var messageRepository: MessageRepository

    private val user = User(apiKey = "hashed-key", apiKeyEnc = "enc-key").apply { id = 1L }
    private val conversation = Conversation(user = user, title = "test").apply { id = 10L }

    private fun conversation(id: Long, title: String): Conversation {
        return Conversation(user = user, title = title).apply { this.id = id }
    }

    private fun message(id: Long, role: MessageRole, content: String): Message {
        return Message(conversation = conversation, role = role, content = content).apply { this.id = id }
    }

    // --- getConversations ---

    @Test
    fun `getConversations returns list without cursor`() {
        val conversations = listOf(
            conversation(5L, "newest"),
            conversation(4L, "older")
        )
        whenever(conversationRepository.findByUserIdOrderByCreatedAtDesc(eq(1L), any<PageRequest>()))
            .thenReturn(conversations)

        val response = conversationInfoService.getConversations(userId = 1L, cursor = null, size = 10)

        assertThat(response.conversations).hasSize(2)
        assertThat(response.conversations[0].conversationId).isEqualTo(5L)
        assertThat(response.conversations[0].title).isEqualTo("newest")
        assertThat(response.conversations[1].conversationId).isEqualTo(4L)
        assertThat(response.conversations[1].title).isEqualTo("older")
        assertThat(response.hasNext).isFalse()
        assertThat(response.nextCursor).isNull()
    }

    @Test
    fun `getConversations returns next page with cursor`() {
        val conversations = listOf(
            conversation(2L, "old"),
            conversation(1L, "oldest")
        )
        whenever(conversationRepository.findByUserAndCursorOrderByCreatedAtDesc(eq(1L), eq(3L), any<PageRequest>()))
            .thenReturn(conversations)

        val response = conversationInfoService.getConversations(userId = 1L, cursor = 3L, size = 10)

        assertThat(response.conversations).hasSize(2)
        assertThat(response.conversations[0].title).isEqualTo("old")
        assertThat(response.hasNext).isFalse()
        assertThat(response.nextCursor).isNull()
    }

    @Test
    fun `getConversations returns hasNext true when more data exists`() {
        val conversations = listOf(
            conversation(5L, "newest"),
            conversation(4L, "older"),
            conversation(3L, "old")
        )
        whenever(conversationRepository.findByUserIdOrderByCreatedAtDesc(eq(1L), any<PageRequest>()))
            .thenReturn(conversations)

        val response = conversationInfoService.getConversations(userId = 1L, cursor = null, size = 2)

        assertThat(response.conversations).hasSize(2)
        assertThat(response.hasNext).isTrue()
        assertThat(response.nextCursor).isEqualTo(4L)
    }

    @Test
    fun `getConversations returns empty list when no conversations`() {
        whenever(conversationRepository.findByUserIdOrderByCreatedAtDesc(eq(1L), any<PageRequest>()))
            .thenReturn(emptyList())

        val response = conversationInfoService.getConversations(userId = 1L, cursor = null, size = 10)

        assertThat(response.conversations).isEmpty()
        assertThat(response.hasNext).isFalse()
        assertThat(response.nextCursor).isNull()
    }

    // --- getConversationWithMessages ---

    @Test
    fun `getConversationWithMessages returns list without cursor`() {
        val messages = listOf(
            message(5L, MessageRole.ASSISTANT, "answer"),
            message(4L, MessageRole.USER, "question")
        )
        whenever(conversationRepository.findById(10L)).thenReturn(Optional.of(conversation))
        whenever(messageRepository.findByConversationOrderByCreatedAtDesc(eq(conversation), any<PageRequest>()))
            .thenReturn(messages)

        val response = conversationInfoService.getConversationWithMessages(conversationId = 10L, cursor = null, size = 10)

        assertThat(response.messages).hasSize(2)
        assertThat(response.messages[0].role).isEqualTo("ASSISTANT")
        assertThat(response.messages[0].content).isEqualTo("answer")
        assertThat(response.messages[1].role).isEqualTo("USER")
        assertThat(response.hasNext).isFalse()
        assertThat(response.nextCursor).isNull()
    }

    @Test
    fun `getConversationWithMessages returns next page with cursor`() {
        val messages = listOf(
            message(2L, MessageRole.USER, "old question"),
            message(1L, MessageRole.ASSISTANT, "old answer")
        )
        whenever(conversationRepository.findById(10L)).thenReturn(Optional.of(conversation))
        whenever(messageRepository.findByConversationAndCursorOrderByCreatedAtDesc(eq(conversation), eq(3L), any<PageRequest>()))
            .thenReturn(messages)

        val response = conversationInfoService.getConversationWithMessages(conversationId = 10L, cursor = 3L, size = 10)

        assertThat(response.messages).hasSize(2)
        assertThat(response.hasNext).isFalse()
        assertThat(response.nextCursor).isNull()
    }

    @Test
    fun `getConversationWithMessages returns hasNext true when more data exists`() {
        val messages = listOf(
            message(5L, MessageRole.ASSISTANT, "newest"),
            message(4L, MessageRole.USER, "newer"),
            message(3L, MessageRole.ASSISTANT, "old")
        )
        whenever(conversationRepository.findById(10L)).thenReturn(Optional.of(conversation))
        whenever(messageRepository.findByConversationOrderByCreatedAtDesc(eq(conversation), any<PageRequest>()))
            .thenReturn(messages)

        val response = conversationInfoService.getConversationWithMessages(conversationId = 10L, cursor = null, size = 2)

        assertThat(response.messages).hasSize(2)
        assertThat(response.hasNext).isTrue()
        assertThat(response.nextCursor).isEqualTo(4L)
    }

    @Test
    fun `getConversationWithMessages returns empty list when no messages`() {
        whenever(conversationRepository.findById(10L)).thenReturn(Optional.of(conversation))
        whenever(messageRepository.findByConversationOrderByCreatedAtDesc(eq(conversation), any<PageRequest>()))
            .thenReturn(emptyList())

        val response = conversationInfoService.getConversationWithMessages(conversationId = 10L, cursor = null, size = 10)

        assertThat(response.messages).isEmpty()
        assertThat(response.hasNext).isFalse()
        assertThat(response.nextCursor).isNull()
    }
}
