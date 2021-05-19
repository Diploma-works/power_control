package ru.foody.dto.auth

data class AuthResponse(
    var login: String,
    var token: String
)