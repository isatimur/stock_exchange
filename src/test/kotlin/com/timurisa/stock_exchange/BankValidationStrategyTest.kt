package com.timurisa.stock_exchange

import com.timurisa.stock_exchange.domain.ClientAccountBuilder
import com.timurisa.stock_exchange.service.BankValidationStrategy
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BankValidationStrategyTest {

    private val strategy = BankValidationStrategy()

    @Test
    fun `validate success for bank strategy`() {
        val clientAccount = ClientAccountBuilder()
            .withBankId("bank123")
            .withLastName("Doe")
            .withFirstName("John")
            .withMiddleName("Middle")
            .withBirthDate(LocalDate.of(1990, 1, 1))
            .withPassportNumber("1234 567890")
            .build()

        strategy.validate(clientAccount)
        // Success if no exception is thrown
    }

    @Test
    fun `validate failure for bank strategy when a required field is missing`() {
        val clientAccount = ClientAccountBuilder()
            .withFirstName("John")
            .build()

        assertThrows(IllegalArgumentException::class.java) {
            strategy.validate(clientAccount)
        }
    }
}
