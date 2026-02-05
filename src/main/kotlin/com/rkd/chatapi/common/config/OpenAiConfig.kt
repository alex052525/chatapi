package com.rkd.chatapi.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class OpenAiConfig {

    @Bean
    fun openAiRestClient(
        builder: RestClient.Builder,
        @Value("\${openai.api-key}") apiKey: String
    ): RestClient {
        return builder
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .build()
    }

    @Bean
    fun openAiWebClient(
        @Value("\${openai.api-key}") apiKey: String
    ): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .build()
    }
}
