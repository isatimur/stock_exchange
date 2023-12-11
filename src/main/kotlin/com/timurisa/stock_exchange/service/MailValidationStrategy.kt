package com.timurisa.stock_exchange.service

import com.timurisa.stock_exchange.domain.ClientAccount

class MailValidationStrategy : ValidationStrategy {
    override fun validate(clientAccount: ClientAccount) {
        if (clientAccount.firstName == null || clientAccount.email == null) {
            throw IllegalArgumentException("firstName and email can't be null for source mail")
        }
    }
}