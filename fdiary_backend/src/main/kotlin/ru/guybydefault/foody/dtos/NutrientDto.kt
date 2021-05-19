package ru.guybydefault.foody.dtos

data class NutrientDto(
    val id: Int,
    val nutrientType: Int,
    val value: Double,
    val nutrientsInfo: Int
) {
}