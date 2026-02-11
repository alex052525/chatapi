package com.rkd.chatapi.message.domain.repository

import com.rkd.chatapi.message.domain.entity.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long>
