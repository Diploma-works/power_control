package ru.guybydefault.foody.domain

data class NutrientsInfo(
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carb: Double
) : BaseEntity() {
    val nutrients: MutableList<Nutrient> = mutableListOf()

    operator fun plus(nutrientsInfo: NutrientsInfo): NutrientsInfo {
        val sum = NutrientsInfo(
            this.calories + nutrientsInfo.calories,
            this.protein + nutrientsInfo.protein,
            this.fat + nutrientsInfo.fat,
            this.carb + nutrientsInfo.carb
        )

        for (nutrient in nutrientsInfo.nutrients) {
            sum.addNutrient(nutrient)
        }
        for (nutrient in nutrients) {
            sum.addNutrient(nutrient)
        }

        return sum
    }

    fun addNutrient(nutrientType: NutrientType, value: Double) {
        nutrients.find { nutrient: Nutrient -> nutrientType == nutrient.nutrientType }?.value?.plus(
            value
        ) ?: nutrients.add(Nutrient(nutrientType, value, this))
    }

    fun addNutrient(nutrient: Nutrient) {
        addNutrient(nutrient.nutrientType, nutrient.value)
    }

    fun getNutrientByType(nutrientType: NutrientType) : Nutrient {
        return nutrients.find { nutrient -> nutrient.nutrientType == nutrientType } ?: Nutrient(nutrientType, 0.0, this)
    }
}