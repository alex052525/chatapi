package com.rkd.chatapi.common.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtAuthToken(
    private val userId: Long
) : AbstractAuthenticationToken(listOf(SimpleGrantedAuthority("ROLE_USER"))) {

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any = userId
}
