package ru.guybydefault.foody.dtos

import javax.validation.constraints.NotEmpty

data class AuthRequest(
    @NotEmpty
    var login: String?,
    @NotEmpty
    var password: String?
)
