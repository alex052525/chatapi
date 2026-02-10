package com.rkd.chatapi.chat.controller

import com.rkd.chatapi.common.annotation.LoginUser
import com.rkd.chatapi.chat.dto.request.ChatCompletionRequest
import com.rkd.chatapi.chat.dto.response.ChatCompletionResponse
import com.rkd.chatapi.chat.service.ChatCompletionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatCompletionController(
    private val chatCompletionService: ChatCompletionService
) {
    @PostMapping("/completions")
    fun createCompletion(
        @RequestBody request: ChatCompletionRequest,
        @LoginUser userId: Long
    ): ResponseEntity<ChatCompletionResponse> {
        val chatCompletionResponse = chatCompletionService.completeChat(userId, request)
        return ResponseEntity.ok(chatCompletionResponse)
    }
}