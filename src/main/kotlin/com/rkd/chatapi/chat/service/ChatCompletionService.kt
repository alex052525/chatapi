package com.rkd.chatapi.chat.service

import com.rkd.chatapi.conversation.domain.ConversationReader
import com.rkd.chatapi.message.domain.MessageReader
import com.rkd.chatapi.message.domain.MessageWriter
import com.rkd.chatapi.message.domain.MessageRole
import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.chat.dto.request.ChatCompletionRequest
import com.rkd.chatapi.chat.dto.response.ChatCompletionResponse
import com.rkd.chatapi.chat.dto.response.ChatStreamChunk
import com.rkd.chatapi.chat.adapter.OpenAiChatAdapter
import com.rkd.chatapi.chat.dto.OpenAiChatMessage
import com.rkd.chatapi.conversation.domain.entity.Conversation
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ChatCompletionService(
    private val conversationReader: ConversationReader,
    private val messageReader: MessageReader,
    private val messageWriter: MessageWriter,
    private val openAiChatAdapter: OpenAiChatAdapter,
    @Value("\${openai.history-limit}") private val historyLimit: Int
) {
    fun completeChat(userId: Long, request: ChatCompletionRequest): ChatCompletionResponse {
        val (conversation, allMessages) = prepareChat(request)

        val answer = openAiChatAdapter.completeChat(userId, allMessages)
        val savedAssistant = saveAssistantMessage(conversation, answer)

        return ChatCompletionResponse(
            messageId = savedAssistant.id!!,
            answer = answer
        )
    }

    fun completeChatStream(userId: Long, request: ChatCompletionRequest): Flux<ChatStreamChunk> {
        val (conversation, allMessages) = prepareChat(request)
        val contentBuffer = StringBuilder()

        return openAiChatAdapter.completeChatStream(userId, allMessages)
            .doOnNext { chunk -> contentBuffer.append(chunk) }
            .map { chunk -> ChatStreamChunk(content = chunk) }
            .doOnComplete { saveAssistantMessage(conversation, contentBuffer.toString()) }
    }

    private fun prepareChat(request: ChatCompletionRequest): Pair<Conversation, List<OpenAiChatMessage>> {
        val conversation = conversationReader.findConversationById(request.conversationId)
        val previousMessages = getPreviousMessages(conversation)
        saveUserMessage(conversation, request.content)
        val allMessages = previousMessages + OpenAiChatMessage(role = "user", content = request.content)
        return Pair(conversation, allMessages)
    }

    private fun saveUserMessage(conversation: Conversation, content: String) {
        val userMessage = Message(
            conversation = conversation,
            role = MessageRole.USER,
            content = content
        )
        messageWriter.save(userMessage)
    }

    private fun saveAssistantMessage(conversation: Conversation, content: String): Message {
        val assistantMessage = Message(
            conversation = conversation,
            role = MessageRole.ASSISTANT,
            content = content
        )
        return messageWriter.save(assistantMessage)
    }

    private fun getPreviousMessages(conversation: Conversation): List<OpenAiChatMessage> {
        return messageReader
            .findMessagesByConversation(conversation, PageRequest.of(0, historyLimit))
            .reversed()
            .map { OpenAiChatMessage(role = it.role.name.lowercase(), content = it.content) }
    }
}
