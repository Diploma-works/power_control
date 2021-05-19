package ru.foody.dto.auth

data class AuthRequest(
    var login: String,
    var password: String
)
