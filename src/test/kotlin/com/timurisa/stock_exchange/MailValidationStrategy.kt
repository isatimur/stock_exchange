package com.timurisa.stock_exchange

import com.timurisa.stock_exchange.domain.ClientAccountBuilder
import com.timurisa.stock_exchange.service.MailValidationStrategy
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class MailValidationStrategyTest {

    private val strategy = MailValidationStrategy()

    @Test
    fun `validate success for mail strategy`() {
        val clientAccount = ClientAccountBuilder()
            .withId(id = "1")
            .withFirstName(firstName = "John")
            .withEmail(email = "john@example.com")
            .build()
        strategy.validate(clientAccount)
        // If no exception is thrown, the test passes
    }

    @Test
    fun `validate failure for mail strategy`() {
        val clientAccount = ClientAccountBuilder()
            .withId(id = "1")
            .withFirstName(firstName = "John")
            .build()
        assertThrows(IllegalArgumentException::class.java) {
            strategy.validate(clientAccount)
        }
    }
}
