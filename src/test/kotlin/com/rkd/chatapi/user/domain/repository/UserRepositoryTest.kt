package com.rkd.chatapi.user.domain.repository

import com.rkd.chatapi.user.domain.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserRepositoryTest(
    @Autowired private val userRepository: UserRepository
) {

    @Test
    fun `findByApiKey returns user when apiKey matches`() {
        // given
        val user = User().apply {
            apiKey = "hashed-key"
        }
        val saved = userRepository.save(user)

        // when
        val found = userRepository.findByApiKey("hashed-key")

        // then
        assertThat(found).isNotNull
        assertThat(found?.id).isEqualTo(saved.id)
        assertThat(found?.apiKey).isEqualTo("hashed-key")
    }

    @Test
    fun `findByApiKey returns null when apiKey does not match`() {
        // given
        val user = User().apply {
            apiKey = "hashed-key"
        }
        userRepository.save(user)

        // when
        val found = userRepository.findByApiKey("missing-key")

        // then
        assertThat(found).isNull()
    }
}
