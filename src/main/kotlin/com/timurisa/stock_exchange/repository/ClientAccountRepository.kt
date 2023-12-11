package com.timurisa.stock_exchange.repository

import com.timurisa.stock_exchange.domain.ClientAccount
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface ClientAccountRepository : ReactiveCrudRepository<ClientAccount, String> {
    fun findByLastNameAndFirstNameAndMiddleNameAndPhoneAndEmail(
        lastName: String?, firstName: String?, middleName: String?, phone: String?, email: String?
    ): Flux<ClientAccount>
}
