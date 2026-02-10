package com.rkd.chatapi.user.domain.entity

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.common.domain.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
open class User : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "apiKey", nullable = false, length = 128)
    open lateinit var apiKey: String

    @Column(name = "apiKeyEnc", columnDefinition = "text")
    open var apiKeyEnc: String? = null

    @OneToMany(mappedBy = "user")
    open var conversations: MutableList<Conversation> = mutableListOf()
}
