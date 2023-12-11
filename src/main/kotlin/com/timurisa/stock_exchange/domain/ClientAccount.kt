package com.timurisa.stock_exchange.domain

import java.time.LocalDate

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
