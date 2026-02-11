package com.rkd.chatapi.conversation.controller

import com.rkd.chatapi.common.annotation.LoginUser
import com.rkd.chatapi.conversation.dto.request.ConversationCreateRequest
import com.rkd.chatapi.conversation.dto.response.ConversationCreateResponse
import com.rkd.chatapi.conversation.service.ConversationService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/conversations")
class ConversationController(
    private val conversationService: ConversationService
) {
    @PostMapping
    fun createConversation(
        @RequestBody conversationCreateRequest: ConversationCreateRequest,
        @LoginUser userId: Long
    ): ResponseEntity<ConversationCreateResponse> {
        val response = conversationService.createConversation(userId, conversationCreateRequest)
        return ResponseEntity.ok(response)
    }
}
