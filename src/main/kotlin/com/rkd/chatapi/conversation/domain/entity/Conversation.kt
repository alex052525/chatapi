package com.rkd.chatapi.conversation.domain.entity

import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.common.domain.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
open class Conversation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    open var user: User? = null,

    @Column(name = "title", nullable = false, length = 255)
    open var title: String? = null,

    @OneToMany(mappedBy = "conversation")
    open var messages: MutableList<Message> = mutableListOf()
) : BaseTimeEntity()
