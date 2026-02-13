package com.rkd.chatapi.conversation.domain.repository

import com.rkd.chatapi.conversation.domain.entity.Conversation
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ConversationRepository : JpaRepository<Conversation, Long> {

    fun findByUserIdOrderByCreatedAtDesc(userId: Long, pageable: Pageable): List<Conversation>

    @Query("SELECT c FROM Conversation c WHERE c.user.id = :userId AND c.id < :cursor ORDER BY c.createdAt DESC")
    fun findByUserAndCursorOrderByCreatedAtDesc(userId: Long, cursor: Long, pageable: Pageable): List<Conversation>
}
