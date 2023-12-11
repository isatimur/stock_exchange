package com.timurisa.stock_exchange

import com.timurisa.stock_exchange.controller.ClientAccountController
import com.timurisa.stock_exchange.domain.ClientAccountBuilder
import com.timurisa.stock_exchange.service.ClientAccountService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

class ClientAccountControllerTest {

    private val service = mockk<ClientAccountService>()
    private val client = WebTestClient.bindToController(ClientAccountController(service)).build()

    @Test
    fun `create account successfully`() {
        val clientAccount = ClientAccountBuilder()
            .withFirstName("John")
            .withEmail("john@example.com")
            .build()

        every { service.validateAndCreateAccount(clientAccount, "mail") } returns Mono.just(clientAccount)

        client.post().uri("/clientAccounts")
            .contentType(MediaType.APPLICATION_JSON)
            .header("x-Source", "mail")
            .bodyValue(clientAccount)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.firstName").isEqualTo("John")
    }

    // Additional tests for other endpoints and failure scenarios...
}
