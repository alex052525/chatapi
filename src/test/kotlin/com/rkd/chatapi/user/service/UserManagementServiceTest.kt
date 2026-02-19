package com.rkd.chatapi.user.service

import com.rkd.chatapi.user.domain.UserWriter
import com.rkd.chatapi.user.domain.entity.User
import com.rkd.chatapi.user.validator.UserValidator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class UserManagementServiceTest {

    @InjectMocks
    private lateinit var userManagementService: UserManagementService

    @Mock
    private lateinit var userWriter: UserWriter

    @Mock
    private lateinit var userValidator: UserValidator

    @Test
    fun `createUserByApiKey creates new user`() {
        // given
        whenever(userWriter.save(any<User>())).thenAnswer { invocation ->
            (invocation.arguments[0] as User).apply { id = 2L }
        }

        // when
        val result = userManagementService.createUserByApiKey("hashed-key", "enc-key")

        // then
        assertThat(result).isEqualTo(2L)
    }
}
