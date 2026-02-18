package com.rkd.chatapi.message.domain.repository

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.message.domain.entity.Message
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MessageRepository : JpaRepository<Message, Long> {
    fun findByConversationOrderByCreatedAtDesc(conversation: Conversation, pageable: Pageable): List<Message>

    @Query("SELECT m FROM Message m WHERE m.conversation = :conversation AND m.id < :cursor ORDER BY m.createdAt DESC")
    fun findByConversationAndCursorOrderByCreatedAtDesc(conversation: Conversation, cursor: Long, pageable: Pageable): List<Message>
}
