package com.rkd.chatapi.chat.service

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.message.domain.repository.MessageRepository
import com.rkd.chatapi.chat.dto.request.ChatCompletionRequest
import com.rkd.chatapi.chat.adapter.OpenAiChatAdapter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ChatCompletionServiceTest {

    @InjectMocks
    private lateinit var chatCompletionService: ChatCompletionService

    @Mock
    private lateinit var conversationRepository: ConversationRepository

    @Mock
    private lateinit var messageRepository: MessageRepository

    @Mock
    private lateinit var openAiChatAdapter: OpenAiChatAdapter

    @Test
    fun `createCompletion saves user and assistant messages and returns response`() {
        val conversation = Conversation().apply { id = 1L }
        val request = ChatCompletionRequest(conversationId = 1L, content = "hi")

        whenever(conversationRepository.findById(1L)).thenReturn(Optional.of(conversation))
        whenever(openAiChatAdapter.completeChat(1L, "hi")).thenReturn("answer")

        var saveCount = 0
        whenever(messageRepository.save(any<Message>())).thenAnswer { invocation ->
            saveCount += 1
            (invocation.arguments[0] as Message).apply {
                id = if (saveCount == 1) 100L else 200L
            }
        }

        val response = chatCompletionService.completeChat(1L, request)

        Assertions.assertThat(response.messageId).isEqualTo(200L)
        Assertions.assertThat(response.answer).isEqualTo("answer")
    }
}
