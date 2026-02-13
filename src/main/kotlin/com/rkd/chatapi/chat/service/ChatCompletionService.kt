package com.rkd.chatapi.chat.service

import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.message.domain.MessageRole
import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.message.domain.repository.MessageRepository
import com.rkd.chatapi.chat.dto.request.ChatCompletionRequest
import com.rkd.chatapi.chat.dto.response.ChatCompletionResponse
import com.rkd.chatapi.chat.adapter.OpenAiChatAdapter
import com.rkd.chatapi.chat.dto.OpenAiChatMessage
import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.exception.ConversationNotExistException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ChatCompletionService(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val openAiChatAdapter: OpenAiChatAdapter,
    @Value("\${openai.history-limit}") private val historyLimit: Int
) {
    fun completeChat(userId: Long, request: ChatCompletionRequest): ChatCompletionResponse {
        val conversation = conversationRepository.findById(request.conversationId)
            .orElseThrow { ConversationNotExistException() }

        // 이전 메시지 조회 (최근 N개, 시간순 정렬)
        val previousMessages = getPreviosuMessages(conversation)
        saveUserMessage(conversation, request.content)

        // 이전 메시지 + 현재 메시지를 함께 전달
        val allMessages = previousMessages + OpenAiChatMessage(role = "user", content = request.content)
        val answer = openAiChatAdapter.completeChat(userId, allMessages)
        val savedAssistant = saveAssistantMessage(conversation, answer)

        return ChatCompletionResponse(
            messageId = savedAssistant.id!!,
            answer = answer
        )
    }

    private fun saveUserMessage(conversation: Conversation, content: String) {
        val userMessage = Message(
            conversation = conversation,
            role = MessageRole.USER,
            content = content
        )
        messageRepository.save(userMessage)
    }

    private fun saveAssistantMessage(conversation: Conversation, content: String): Message {
        val assistantMessage = Message(
            conversation = conversation,
            role = MessageRole.ASSISTANT,
            content = content
        )
        return messageRepository.save(assistantMessage)
    }

    private fun getPreviosuMessages(conversation: Conversation): List<OpenAiChatMessage> {
        return messageRepository
            .findByConversationOrderByCreatedAtDesc(conversation, PageRequest.of(0, historyLimit))
            .reversed()
            .map { OpenAiChatMessage(role = it.role.name.lowercase(), content = it.content) }
    }
}
