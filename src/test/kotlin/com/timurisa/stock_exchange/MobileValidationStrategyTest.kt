package com.timurisa.stock_exchange

import com.timurisa.stock_exchange.domain.ClientAccountBuilder
import com.timurisa.stock_exchange.service.MobileValidationStrategy
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class MobileValidationStrategyTest {

    private val strategy = MobileValidationStrategy()

    @Test
    fun `validate success for mobile strategy`() {
        val clientAccount = ClientAccountBuilder()
            .withPhone("71234567890")
            .build()

        strategy.validate(clientAccount)
        // Success if no exception is thrown
    }

    @Test
    fun `validate failure phone number on mobile strategy`() {
        assertThrows(IllegalArgumentException::class.java) {
            ClientAccountBuilder()
                .withPhone("91234567890")
                .build()
        }

    }


    @Test
    fun `validate failure for mobile strategy when phone is missing`() {
        val clientAccount = ClientAccountBuilder()
            .build()

        assertThrows(IllegalArgumentException::class.java) {
            strategy.validate(clientAccount)
        }
    }
}
