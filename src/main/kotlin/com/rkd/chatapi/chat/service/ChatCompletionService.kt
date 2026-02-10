package com.rkd.chatapi.chat.service

import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.message.domain.MessageRole
import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.message.domain.repository.MessageRepository
import com.rkd.chatapi.chat.dto.request.ChatCompletionRequest
import com.rkd.chatapi.chat.dto.response.ChatCompletionResponse
import com.rkd.chatapi.chat.adapter.OpenAiChatAdapter
import com.rkd.chatapi.conversation.domain.entity.Conversation
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

        saveUserMessage(conversation, request.content)

        val answer = openAiChatAdapter.completeChat(userId, request.content)
        val savedAssistant = saveAssistantMessage(conversation, answer)

        return ChatCompletionResponse(
            messageId = savedAssistant.id!!,
            answer = answer
        )
    }

    private fun saveUserMessage(conversation: Conversation, content: String) {
        val userMessage = Message().apply {
            this.conversation = conversation
            this.role = MessageRole.USER
            this.content = content
        }
        messageRepository.save(userMessage)
    }

    private fun saveAssistantMessage(conversation: Conversation, content: String): Message {
        val assistantMessage = Message().apply {
            this.conversation = conversation
            this.role = MessageRole.ASSISTANT
            this.content = content
        }
        return messageRepository.save(assistantMessage)
    }
}
