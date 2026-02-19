package com.rkd.chatapi.chat.service

import com.rkd.chatapi.chat.dto.OpenAiChatMessage
import com.rkd.chatapi.conversation.domain.ConversationReader
import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.message.domain.MessageReader
import com.rkd.chatapi.message.domain.MessageWriter
import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.chat.dto.request.ChatCompletionRequest
import com.rkd.chatapi.chat.adapter.OpenAiChatAdapter
import com.rkd.chatapi.user.domain.entity.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Pageable

@ExtendWith(MockitoExtension::class)
class ChatCompletionServiceTest {

    private lateinit var chatCompletionService: ChatCompletionService

    @Mock
    private lateinit var conversationReader: ConversationReader

    @Mock
    private lateinit var messageReader: MessageReader

    @Mock
    private lateinit var messageWriter: MessageWriter

    @Mock
    private lateinit var openAiChatAdapter: OpenAiChatAdapter

    @BeforeEach
    fun setUp() {
        chatCompletionService = ChatCompletionService(
            conversationReader = conversationReader,
            messageReader = messageReader,
            messageWriter = messageWriter,
            openAiChatAdapter = openAiChatAdapter,
            historyLimit = 10
        )
    }

    @Test
    fun `createCompletion saves user and assistant messages and returns response`() {
        val conversation = Conversation(
            user = User(apiKey = "hashed-key", apiKeyEnc = "enc-key"),
            title = "hello"
        ).apply { id = 1L }
        val request = ChatCompletionRequest(conversationId = 1L, content = "hi")

        whenever(conversationReader.findConversationById(1L)).thenReturn(conversation)
        whenever(messageReader.findMessagesByConversation(any(), any<Pageable>()))
            .thenReturn(emptyList())
        whenever(openAiChatAdapter.completeChat(any(), any<List<OpenAiChatMessage>>())).thenReturn("answer")

        var saveCount = 0
        whenever(messageWriter.save(any<Message>())).thenAnswer { invocation ->
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
