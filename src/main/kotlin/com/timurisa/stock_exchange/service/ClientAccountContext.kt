package com.timurisa.stock_exchange.service

import com.timurisa.stock_exchange.domain.ClientAccount
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class ClientAccountContext {

    private lateinit var strategies: Map<String, ValidationStrategy>

    @PostConstruct
    fun init() {
        strategies = mutableMapOf(
            "mail" to MailValidationStrategy(),
            "mobile" to MobileValidationStrategy(),
            "bank" to BankValidationStrategy(),
            "gosuslugi" to GosuslugiValidationStrategy()
        )
    }

    fun validateSource(clientAccount: ClientAccount, source: String) {
        strategies[source]?.validate(clientAccount)
            ?: throw IllegalArgumentException("Invalid source client")
    }
}