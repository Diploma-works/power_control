package ru.guybydefault.foody.domain

data class User(
    var login: String?,
    var password: String?
) : BaseEntity() {
}