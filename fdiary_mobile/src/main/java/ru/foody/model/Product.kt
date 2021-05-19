package ru.guybydefault.foody.domain

import java.time.Instant

data class Product(
    var name: String,
    var nutrients: NutrientsInfo,
    var dateCreated: Instant,
    var dateUpdated: Instant,
    val user: User?
) : BaseEntity() {
}