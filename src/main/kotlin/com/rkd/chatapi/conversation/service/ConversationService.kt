package com.rkd.chatapi.conversation.service

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.conversation.dto.request.ConversationCreateRequest
import com.rkd.chatapi.conversation.dto.response.ConversationCreateResponse
import com.rkd.chatapi.user.domain.repository.UserRepository
import com.rkd.chatapi.user.exception.UserNotExistException
import org.springframework.stereotype.Service

@Service
class ConversationService(
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) {
    fun createConversation(userId: Long, conversationCreateRequest: ConversationCreateRequest): ConversationCreateResponse {
        val user = userRepository.findById(userId).orElseThrow { UserNotExistException() }
        val conversation = Conversation(
            user = user,
            title = conversationCreateRequest.title
        )
        return ConversationCreateResponse(conversationRepository.save(conversation).id!!)
    }
}
