package ru.guybydefault.foody.domain

data class NutrientsInfoDto(
    val id: Int,
    val nutrients: List<NutrientDto>,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carb: Double
) {
}