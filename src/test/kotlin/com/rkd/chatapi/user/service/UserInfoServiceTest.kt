package com.rkd.chatapi.user.service

import com.rkd.chatapi.user.domain.UserReader
import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.validator.UserValidator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class UserInfoServiceTest {

    @InjectMocks
    private lateinit var userInfoService: UserInfoService

    @Mock
    private lateinit var userReader: UserReader

    @Mock
    private lateinit var userValidator: UserValidator

    @Test
    fun `findUserByApiKey returns existing user id`() {
        // given
        val existing = User(apiKey = "hashed-key", apiKeyEnc = "enc-key").apply {
            id = 1L
        }
        whenever(userReader.findUserByApiKey("hashed-key")).thenReturn(existing)

        // when
        val result = userInfoService.findUserByApiKey("hashed-key")

        // then
        assertThat(result).isEqualTo(1L)
    }
}
