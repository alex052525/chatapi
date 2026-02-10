package com.rkd.chatapi.chat.service

import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.message.domain.MessageRole
import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.message.domain.repository.MessageRepository
import com.rkd.chatapi.chat.dto.request.ChatCompletionRequest
import com.rkd.chatapi.chat.dto.response.ChatCompletionResponse
import com.rkd.chatapi.chat.adapter.OpenAiChatAdapter
import com.rkd.chatapi.conversation.exception.ConversationNotExistException
import org.springframework.stereotype.Service

@Service
class ChatCompletionService(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val openAiChatAdapter: OpenAiChatAdapter
) {
    fun completeChat(userId: Long, request: ChatCompletionRequest): ChatCompletionResponse {
        val conversation = conversationRepository.findById(request.conversationId)
            .orElseThrow { ConversationNotExistException() }

        val userMessage = Message().apply {
            this.conversation = conversation
            this.role = MessageRole.USER
            this.content = request.content
        }
        messageRepository.save(userMessage)

        val answer = openAiChatAdapter.completeChat(userId, request.content)
        val assistantMessage = Message().apply {
            this.conversation = conversation
            this.role = MessageRole.ASSISTANT
            this.content = answer
        }
        val savedAssistant = messageRepository.save(assistantMessage)

        return ChatCompletionResponse(
            messageId = savedAssistant.id!!,
            answer = answer
        )
    }
}
