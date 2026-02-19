package com.rkd.chatapi.conversation.domain

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.conversation.exception.ConversationNotExistException
import org.springframework.stereotype.Component

@Component
class ConversationReader(
    private val conversationRepository: ConversationRepository
) {
    fun findById(conversationId: Long): Conversation {
        return conversationRepository.findById(conversationId)
            .orElseThrow { ConversationNotExistException() }
    }
}
