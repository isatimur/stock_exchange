package com.timurisa.stock_exchange.service

import com.timurisa.stock_exchange.domain.ClientAccount

class BankValidationStrategy : ValidationStrategy {
    override fun validate(clientAccount: ClientAccount) {
        if (clientAccount.bankId == null ||
            clientAccount.lastName == null ||
            clientAccount.firstName == null ||
            clientAccount.middleName == null ||
            clientAccount.birthDate == null ||
            clientAccount.passportNumber == null
        ) {
            throw IllegalArgumentException("BankId, lastName, firstName, middleName, birthDate, and passportNumber can't be null for source bank")
        }
    }
}