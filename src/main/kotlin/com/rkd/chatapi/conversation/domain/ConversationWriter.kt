package com.rkd.chatapi.conversation.domain

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import org.springframework.stereotype.Component

@Component
class ConversationWriter(
    private val conversationRepository: ConversationRepository
) {
    fun save(conversation: Conversation): Conversation {
        return conversationRepository.save(conversation)
    }

    fun delete(conversation: Conversation) {
        conversationRepository.delete(conversation)
    }
}
