package com.rkd.chatapi.message.domain.entity

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.common.domain.entity.BaseTimeEntity
import com.rkd.chatapi.message.domain.MessageRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne

@Entity
class Message(
    conversation: Conversation,
    role: MessageRole,
    content: String
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversationId", nullable = false)
    var conversation: Conversation = conversation
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 32)
    var role: MessageRole = role
        private set

    @Lob
    @Column(name = "content", nullable = false)
    var content: String = content
        private set
}
