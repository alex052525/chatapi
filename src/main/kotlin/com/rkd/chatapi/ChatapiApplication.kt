package com.rkd.chatapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ChatapiApplication

fun main(args: Array<String>) {
    runApplication<ChatapiApplication>(*args)
}
