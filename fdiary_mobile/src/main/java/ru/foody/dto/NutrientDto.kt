package ru.guybydefault.foody.domain

data class NutrientDto(
    val id: Int,
    val nutrientType: Int,
    val value: Double,
    val nutrientsInfo: Int
) {
}