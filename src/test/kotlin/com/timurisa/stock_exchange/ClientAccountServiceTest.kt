package com.timurisa.stock_exchange

import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDate

class ClientAccountServiceTest {

    private lateinit var service: ClientAccountService
    private val clientAccountContext: ClientAccountContext = Mockito.mock(ClientAccountContext::class.java)
    private val clientAccountRepository: ClientAccountRepository = Mockito.mock(ClientAccountRepository::class.java)

    @BeforeEach
    fun setUp() {
        service = ClientAccountService(clientAccountContext, clientAccountRepository)
    }

    private fun createTestClientAccount(): ClientAccount {
        return ClientAccount(
            id = "123",
            bankId = null,
            lastName = "Doe",
            firstName = "John",
            middleName = null,
            birthDate = LocalDate.of(1990, 1, 1),
            passportNumber = "9876 543212",
            birthPlace = "Nowhere",
            phone = "79185678901",
            email = "john.doe@mail.com",
            registrationAddress = "Somewhere",
            residenceAddress = "Somewhere"
        )
    }
    @Test
    fun `should validate and create account when given valid account and source`() {
        val clientAccount = createTestClientAccount() // Use of extracted function
        val source = "mobile"
        Mockito.`when`(clientAccountRepository.save(clientAccount)).thenReturn(Mono.just(clientAccount))
        val result = service.validateAndCreateAccount(clientAccount, source)
        StepVerifier.create(result)
            .expectNext(clientAccount)
            .verifyComplete()
        Mockito.verify(clientAccountContext).validateSource(clientAccount, source)
        Mockito.verify(clientAccountRepository).save(clientAccount)
    }
}
