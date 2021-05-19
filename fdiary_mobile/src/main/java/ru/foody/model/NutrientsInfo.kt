package ru.guybydefault.foody.domain

data class NutrientsInfo(
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carb: Double
) : BaseEntity() {
    lateinit var nutrients: List<Nutrient>
}