package com.timurisa.stock_exchange.service

import com.timurisa.stock_exchange.domain.ClientAccount

class GosuslugiValidationStrategy : ValidationStrategy {
    override fun validate(clientAccount: ClientAccount) {
        if (clientAccount.id == null ||
            clientAccount.bankId == null ||
            clientAccount.lastName == null ||
            clientAccount.firstName == null ||
            clientAccount.middleName == null ||
            clientAccount.birthDate == null ||
            clientAccount.passportNumber == null ||
            clientAccount.birthPlace == null ||
            clientAccount.phone == null ||
            clientAccount.registrationAddress == null
        ) {
            throw IllegalArgumentException("All fields except email and residenceAddress can't be null for source gosuslugi")
        }
    }
}