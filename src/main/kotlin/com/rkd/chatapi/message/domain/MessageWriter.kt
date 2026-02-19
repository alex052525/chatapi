package com.rkd.chatapi.message.domain

import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.message.domain.repository.MessageRepository
import org.springframework.stereotype.Component

@Component
class MessageWriter(
    private val messageRepository: MessageRepository
) {
    fun save(message: Message): Message {
        return messageRepository.save(message)
    }
}
