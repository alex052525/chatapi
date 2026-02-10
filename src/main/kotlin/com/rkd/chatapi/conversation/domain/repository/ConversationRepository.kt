package com.rkd.chatapi.conversation.domain.repository

import com.rkd.chatapi.conversation.domain.entity.Conversation
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Conversation, Long>
