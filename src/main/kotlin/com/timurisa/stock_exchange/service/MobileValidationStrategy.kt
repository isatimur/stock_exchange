package com.timurisa.stock_exchange.service

import com.timurisa.stock_exchange.domain.ClientAccount

class MobileValidationStrategy : ValidationStrategy {
    override fun validate(clientAccount: ClientAccount) {
        if (clientAccount.phone == null) {
            throw IllegalArgumentException("Phone can't be null for source mobile")
        }
    }
}
