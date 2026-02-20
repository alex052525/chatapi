package com.rkd.chatapi.message.domain

enum class MessageRole {
    USER,
    ASSISTANT;

    fun toOpenAiRole(): String = name.lowercase()
}