package com.timurisa.stock_exchange

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
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
import java.time.LocalDate


@SpringBootApplication
class StockExchangeApplication

fun main(args: Array<String>) {
    runApplication<StockExchangeApplication>(*args)
}

data class ClientAccount(
    val id: String?,
    val bankId: String?,
    val lastName: String?,
    val firstName: String?,
    val middleName: String?,
    val birthDate: LocalDate?,
    val passportNumber: String?,
    val birthPlace: String?,
    val phone: String?,
    val email: String?,
    val registrationAddress: String?,
    val residenceAddress: String?
)


class ClientAccountBuilder {
    var id: String? = null
    var bankId: String? = null
    var lastName: String? = null
    var firstName: String? = null
    var middleName: String? = null
    var birthDate: LocalDate? = null
    var passportNumber: String? = null
    var birthPlace: String? = null
    var phone: String? = null
    var email: String? = null
    var registrationAddress: String? = null
    var residenceAddress: String? = null

    fun withId(id: String) = apply { this.id = id }
    fun withBankId(bankId: String) = apply { this.bankId = bankId }
    fun withLastName(lastName: String) = apply { this.lastName = lastName }
    fun withFirstName(firstName: String) = apply { this.firstName = firstName }
    fun withMiddleName(middleName: String) = apply { this.middleName = middleName }
    fun withBirthDate(birthDate: LocalDate) = apply { this.birthDate = birthDate }
    fun withPassportNumber(passportNumber: String) = apply { this.passportNumber = passportNumber }
    fun withBirthPlace(birthPlace: String) = apply { this.birthPlace = birthPlace }
    fun withPhone(phone: String) = apply { this.phone = phone }
    fun withEmail(email: String) = apply { this.email = email }
    fun withRegistrationAddress(registrationAddress: String) = apply { this.registrationAddress = registrationAddress }
    fun withResidenceAddress(residenceAddress: String) = apply { this.residenceAddress = residenceAddress }

    fun validate() {
        require(id == null || id!!.isNotEmpty()) { "ID must not be empty" }
        require(firstName == null || firstName!!.isNotEmpty()) { "Name must not be empty" }
        require(bankId == null || bankId!!.isNotEmpty()) { "Bank id must not be empty" }
        require(lastName == null || lastName!!.isNotEmpty()) { "Last name must not be empty" }
        require(middleName == null || middleName!!.isNotEmpty()) { "Middle name must not be empty" }

        val emailRegex = Regex("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b")
        require(email == null || emailRegex.matches(email!!)) { "Invalid email format" }

        val passportNumberRegex = Regex("\\d{4} \\d{6}")
        require(passportNumber == null || passportNumberRegex.matches(passportNumber!!)) { "Invalid passport number format, it should be XXXX XXXXXX" }

        val phoneRegex = Regex("7\\d{10}")
        require(phone == null || phoneRegex.matches(phone!!)) { "Invalid phone number format, it should be 7XXXXXXXXXX" }

        val birthDateMin = LocalDate.parse("1900-01-01")
        val birthDateMax = LocalDate.now().minusYears(18)
        require(birthDate == null || (birthDate!!.isAfter(birthDateMin) && birthDate!!.isBefore(birthDateMax))) { "Invalid birth date, it should be between 1900-01-01 and 18 years ago" }
    }
    fun build(): ClientAccount = ClientAccount(
        id = id,
        bankId = bankId,
        lastName = lastName,
        firstName = firstName,
        middleName = middleName,
        birthDate = birthDate,
        passportNumber = passportNumber,
        birthPlace = birthPlace,
        phone = phone,
        email = email,
        registrationAddress = registrationAddress,
        residenceAddress = residenceAddress
    ).apply { validate() }
}


interface ClientAccountRepository : ReactiveCrudRepository<ClientAccount, String> {
    fun findByLastNameAndFirstNameAndMiddleNameAndPhoneAndEmail(
        lastName: String?, firstName: String?, middleName: String?, phone: String?, email: String?
    ): Flux<ClientAccount>
}

interface ValidationStrategy {
    fun validate(clientAccount: ClientAccount)
}

class MailValidationStrategy : ValidationStrategy {
    override fun validate(clientAccount: ClientAccount) {
        if (clientAccount.firstName == null || clientAccount.email == null) {
            throw IllegalArgumentException("firstName and email can't be null for source mail")
        }
    }
}

class MobileValidationStrategy : ValidationStrategy {
    override fun validate(clientAccount: ClientAccount) {
        if (clientAccount.phone == null) {
            throw IllegalArgumentException("Phone can't be null for source mobile")
        }
    }
}


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

