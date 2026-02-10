package com.rkd.chatapi.chat.adapter

import com.rkd.chatapi.chat.dto.OpenAiChatMessage
import com.rkd.chatapi.chat.dto.request.OpenAiChatCompletionRequest
import com.rkd.chatapi.chat.dto.response.OpenAiChatCompletionResponse
import com.rkd.chatapi.common.security.ApiKeyEncryptor
import com.rkd.chatapi.user.domain.repository.UserRepository
import com.rkd.chatapi.user.exception.UserNotExistException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class OpenAiChatAdapter(
    private val userRepository: UserRepository,
    private val apiKeyEncryptor: ApiKeyEncryptor,
    @Value("\${openai.model}") private val model: String,
    @Value("\${openai.connect-timeout-ms}") private val connectTimeoutMs: Long,
    @Value("\${openai.read-timeout-ms}") private val readTimeoutMs: Long
) {
    fun completeChat(userId: Long, content: String): String {
        // 암호화된 API Key를 복호화해 OpenAI 호출에 사용할 원문 키 준비
        val apiKey = decryptApiKey(userId)

        // 타임아웃 설정을 포함한 요청 팩토리 구성
        val requestFactory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(connectTimeoutMs.toInt())
            setReadTimeout(readTimeoutMs.toInt())
        }

        // 복호화된 API Key로 OpenAI REST 클라이언트 생성
        val client = RestClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .requestFactory(requestFactory)
            .build()

        // OpenAI Chat Completions 요청 바디 구성
        val request = OpenAiChatCompletionRequest(
            model = model,
            messages = listOf(OpenAiChatMessage(role = "user", content = content))
        )

        // OpenAI에 요청을 보내고 응답 역직렬화
        val response = client.post()
            .uri("/chat/completions")
            .body(request)
            .retrieve()
            .body(OpenAiChatCompletionResponse::class.java)
            ?: throw IllegalStateException("OpenAI response is null")

        // 첫 번째 choice의 message.content만 추출하여 반환
        val message = response.choices.firstOrNull()?.message?.content
            ?: throw IllegalStateException("OpenAI response has no message")
        return message
    }

    private fun decryptApiKey(userId: Long): String {
        // 사용자 조회 후 암호화된 API Key를 복호화
        val user = userRepository.findById(userId).orElseThrow { UserNotExistException() }
        val encrypted = user.apiKeyEnc ?: throw IllegalStateException("Encrypted API key missing")
        return apiKeyEncryptor.decrypt(encrypted)
    }

}
