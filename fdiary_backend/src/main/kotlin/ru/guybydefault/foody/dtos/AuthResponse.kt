package ru.guybydefault.foody.dtos

import ru.guybydefault.foody.domain.User

data class AuthResponse(
    var login: String,
    var token: String
)