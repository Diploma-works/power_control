package ru.foody.dto.auth


data class RegistrationRequest(
    var login: String,
    var password: String
) {
}