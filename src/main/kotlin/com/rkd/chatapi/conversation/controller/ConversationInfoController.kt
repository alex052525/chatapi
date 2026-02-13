package com.rkd.chatapi.conversation.controller

import com.rkd.chatapi.common.annotation.LoginUser
import com.rkd.chatapi.conversation.dto.response.ConversationListResponse
import com.rkd.chatapi.conversation.service.ConversationInfoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/conversations")
class ConversationInfoController(
    private val conversationInfoService: ConversationInfoService
) {
    @GetMapping
    fun getConversations(
        @LoginUser userId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "3") size: Int
    ): ResponseEntity<ConversationListResponse> {
        val response = conversationInfoService.getConversations(userId, cursor, size)
        return ResponseEntity.ok(response)
    }
}
