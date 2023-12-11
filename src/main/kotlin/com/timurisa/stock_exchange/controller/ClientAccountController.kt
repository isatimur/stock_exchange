package com.timurisa.stock_exchange.controller

import com.timurisa.stock_exchange.domain.ClientAccount
import com.timurisa.stock_exchange.service.ClientAccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/clientAccounts")
class ClientAccountController(private val service: ClientAccountService) {

    @PostMapping
    fun createAccount(
        @RequestBody clientAccount: ClientAccount, @RequestHeader("x-Source") source: String
    ): Mono<ClientAccount> {
        return service.validateAndCreateAccount(clientAccount, source)
    }

    @GetMapping("/{id}")
    fun getAccountById(@PathVariable id: String): Mono<ClientAccount> {
        return service.findAccountById(id)
    }

    @GetMapping("/search")
    fun searchAccounts(
        @RequestParam(required = false) lastName: String?,
        @RequestParam(required = false) firstName: String?,
        @RequestParam(required = false) middleName: String?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) email: String?
    ): Flux<ClientAccount> {
        return service.searchAccounts(lastName, firstName, middleName, phone, email)
    }
}
