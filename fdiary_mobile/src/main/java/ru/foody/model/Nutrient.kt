package ru.guybydefault.foody.domain

data class Nutrient(
    val nutrientType: NutrientType,
    val value: Double,
    val nutrientsInfo: NutrientsInfo
) : BaseEntity() {
    override fun toString(): String {
        return nutrientType.toString() + ": " + value + " nutrientsInfo ID " + nutrientsInfo.id
    }
}