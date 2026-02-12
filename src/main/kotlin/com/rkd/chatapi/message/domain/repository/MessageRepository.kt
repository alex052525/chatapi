package com.rkd.chatapi.message.domain.repository

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.message.domain.entity.Message
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long> {
    fun findByConversationOrderByCreatedAtDesc(conversation: Conversation, pageable: Pageable): List<Message>
}
