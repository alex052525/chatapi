package com.rkd.chatapi.auth.validator

import com.rkd.chatapi.auth.exception.ApiKeyInvalidException
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

@Component
class ApiKeyValidator(
    private val restClientBuilder: RestClient.Builder
) {

    fun validateApiKey(apiKey: String) {
        val client = restClientBuilder
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .build()

        try {
            client.get()
                .uri("/models")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String::class.java)
        } catch (e: HttpClientErrorException.Unauthorized) {
            throw ApiKeyInvalidException()
        } catch (e: HttpClientErrorException.Forbidden) {
            throw ApiKeyInvalidException()
        }
    }
}
