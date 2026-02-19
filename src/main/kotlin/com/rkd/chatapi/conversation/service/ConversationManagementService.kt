package com.rkd.chatapi.conversation.service

import com.rkd.chatapi.conversation.domain.ConversationReader
import com.rkd.chatapi.conversation.domain.ConversationWriter
import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.dto.request.ConversationCreateRequest
import com.rkd.chatapi.conversation.dto.response.ConversationCreateResponse
import com.rkd.chatapi.user.domain.repository.UserRepository
import com.rkd.chatapi.user.exception.UserNotExistException
import org.springframework.stereotype.Service

@Service
class ConversationManagementService(
    private val userRepository: UserRepository,
    private val conversationReader: ConversationReader,
    private val conversationWriter: ConversationWriter
) {
    fun createConversation(userId: Long, conversationCreateRequest: ConversationCreateRequest): ConversationCreateResponse {
        val user = userRepository.findById(userId).orElseThrow { UserNotExistException() }
        val conversation = Conversation(
            user = user,
            title = conversationCreateRequest.title
        )
        return ConversationCreateResponse(conversationWriter.save(conversation).id!!)
    }

    fun deleteConversation(userId: Long, conversationId: Long) {
        val conversation = conversationReader.findConversationById(conversationId)
        conversation.validateOwner(userId)
        conversationWriter.delete(conversation)
    }
}
