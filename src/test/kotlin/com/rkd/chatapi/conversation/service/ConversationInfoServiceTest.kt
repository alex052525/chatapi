package com.rkd.chatapi.conversation.service

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
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

@ExtendWith(MockitoExtension::class)
class ConversationInfoServiceTest {

    @InjectMocks
    private lateinit var conversationInfoService: ConversationInfoService

    @Mock
    private lateinit var conversationRepository: ConversationRepository

    private val user = User(apiKey = "hashed-key", apiKeyEnc = "enc-key").apply { id = 1L }

    private fun conversation(id: Long, title: String): Conversation {
        return Conversation(user = user, title = title).apply { this.id = id }
    }

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
        assertThat(response.conversations[0].title).isEqualTo("newest")
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
}
