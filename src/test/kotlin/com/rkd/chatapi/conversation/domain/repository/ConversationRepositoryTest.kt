package com.rkd.chatapi.conversation.domain.repository

import com.rkd.chatapi.conversation.domain.entity.Conversation
import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.domain.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ConversationRepositoryTest @Autowired constructor(
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) {
    @Test
    fun `save stores conversation with user and title`() {
        val user = userRepository.save(User(apiKey = "hashed-key", apiKeyEnc = "enc-key"))
        val conversation = Conversation(
            user = user,
            title = "hello"
        )

        val saved = conversationRepository.save(conversation)

        assertThat(saved.id).isNotNull()
        assertThat(saved.title).isEqualTo("hello")
        assertThat(saved.user.id).isEqualTo(user.id)
    }
}
