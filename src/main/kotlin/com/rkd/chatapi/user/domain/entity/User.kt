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
class User(
    apiKey: String,
    apiKeyEnc: String? = null
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "apiKey", nullable = false, length = 128)
    var apiKey: String = apiKey
        private set

    @Column(name = "apiKeyEnc", columnDefinition = "text")
    var apiKeyEnc: String? = apiKeyEnc
        private set

    @OneToMany(mappedBy = "user")
    var conversations: MutableList<Conversation> = mutableListOf()
}
