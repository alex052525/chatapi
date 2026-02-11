package com.rkd.chatapi.message.domain.repository

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.conversation.domain.repository.ConversationRepository
import com.rkd.chatapi.message.domain.MessageRole
import com.rkd.chatapi.message.domain.entity.Message
import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.domain.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class MessageRepositoryTest @Autowired constructor(
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) {
    @Test
    fun `save stores message with conversation and role`() {
        val user = userRepository.save(User(apiKey = "hashed-key"))
        val conversation = conversationRepository.save(
            Conversation(
                user = user,
                title = "hello"
            )
        )
        val message = Message(
            conversation = conversation,
            role = MessageRole.USER,
            content = "hi"
        )

        val saved = messageRepository.save(message)

        assertThat(saved.id).isNotNull()
        assertThat(saved.role).isEqualTo(MessageRole.USER)
        assertThat(saved.content).isEqualTo("hi")
        assertThat(saved.conversation.id).isEqualTo(conversation.id)
    }
}
