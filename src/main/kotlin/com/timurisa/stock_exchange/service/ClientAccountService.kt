package com.timurisa.stock_exchange.service

import com.timurisa.stock_exchange.domain.ClientAccount
import com.timurisa.stock_exchange.repository.ClientAccountRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ClientAccountService(
    private val clientAccountContext: ClientAccountContext,
    private val repository: ClientAccountRepository
) {

    fun validateAndCreateAccount(clientAccount: ClientAccount, source: String): Mono<ClientAccount> {
        clientAccountContext.validateSource(clientAccount, source)
        return repository.save(clientAccount)
    }

    fun findAccountById(id: String): Mono<ClientAccount> {
        return repository.findById(id)
    }

    fun searchAccounts(
        lastName: String?, firstName: String?, middleName: String?, phone: String?, email: String?
    ): Flux<ClientAccount> {
        return if (listOf(lastName, firstName, middleName, phone, email).any { it != null }) {
            repository.findByLastNameAndFirstNameAndMiddleNameAndPhoneAndEmail(
                lastName, firstName, middleName, phone, email
            )
        } else {
            Flux.empty()
        }
    }
}