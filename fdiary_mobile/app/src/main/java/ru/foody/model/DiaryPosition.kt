package ru.guybydefault.foody.domain

import java.time.Instant

data class DiaryPosition(
    val name: String,
    val date: Instant,
    val nutrientInfo: NutrientsInfo,
    val value: Double,
    val user: User
) : BaseEntity() {
    val totalNutrientsInfo: NutrientsInfo

    init {
        totalNutrientsInfo = getActualNutrientInfoValues(nutrientInfo, value)
    }

    fun getActualNutrientInfoValues(nutrientInfo: NutrientsInfo, value: Double): NutrientsInfo {
        val scale = (value / 100)
        val calories = nutrientInfo.calories * scale
        val protein = nutrientInfo.protein * scale
        val fat = nutrientInfo.fat * scale
        val carbs = nutrientInfo.carb * scale

        val totalNutrientsInfo = NutrientsInfo(calories, protein, fat, carbs)
        totalNutrientsInfo.nutrients.addAll(nutrientInfo.nutrients.map { nutrient: Nutrient ->
            Nutrient(
                nutrient.nutrientType,
                nutrient.value * scale,
                totalNutrientsInfo
            )
        })
        return totalNutrientsInfo
    }

}