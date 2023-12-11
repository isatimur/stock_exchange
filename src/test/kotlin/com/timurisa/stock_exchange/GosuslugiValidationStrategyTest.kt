package com.timurisa.stock_exchange

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate

class GosuslugiValidationStrategyTest {

    private val strategy = GosuslugiValidationStrategy()

    @Test
    fun `validate success for gosuslugi strategy`() {
        val clientAccount = ClientAccountBuilder()
            .withId("1")
            .withBankId("bank123")
            .withLastName("Doe")
            .withFirstName("John")
            .withMiddleName("Middle")
            .withBirthDate(LocalDate.of(1990, 1, 1))
            .withPassportNumber("1234 567890")
            .withBirthPlace("City")
            .withPhone("71234567890")
            .withRegistrationAddress("Some Address")
            .build()

        strategy.validate(clientAccount)
        // Success if no exception is thrown
    }

    @Test
    fun `validate failure for gosuslugi strategy when a required field is missing`() {
        val clientAccount = ClientAccountBuilder()
            .withFirstName("John")
            .build()

        assertThrows(IllegalArgumentException::class.java) {
            strategy.validate(clientAccount)
        }
    }
}
