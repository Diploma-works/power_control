package ru.guybydefault.foody.domain

import java.time.Instant

data class ProductDto(
    val id: Int,
    var name: String,
    var nutrientsInfo: NutrientsInfoDto,
    var dateCreated: Instant,
    var dateUpdated: Instant,
    val user: User
) {
}