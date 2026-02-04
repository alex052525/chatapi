package com.rkd.chatapi.user.domain.entity

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.common.domain.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(name = "apiKey", nullable = false, length = 128)
    open var apiKey: String? = null,

    @OneToMany(mappedBy = "user")
    open var conversations: MutableList<Conversation> = mutableListOf()
) : BaseTimeEntity()
