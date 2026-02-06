package com.rkd.chatapi.user.service

import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.domain.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @InjectMocks
    private lateinit var userService: UserService

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `findOrCreateUserByApiKey returns existing user id`() {
        // given
        val existing = User().apply {
            id = 1L
            apiKey = "hashed-key"
        }
        whenever(userRepository.findByApiKey("hashed-key")).thenReturn(existing)

        // when
        val result = userService.createUserByApiKey("hashed-key")

        // then
        assertThat(result).isEqualTo(1L)
    }

    @Test
    fun `findOrCreateUserByApiKey creates new user when not found`() {
        // given
        whenever(userRepository.findByApiKey("hashed-key")).thenReturn(null)
        whenever(userRepository.save(any<User>())).thenAnswer { invocation ->
            (invocation.arguments[0] as User).apply { id = 2L }
        }

        // when
        val result = userService.createUserByApiKey("hashed-key")

        // then
        assertThat(result).isEqualTo(2L)
    }
}
