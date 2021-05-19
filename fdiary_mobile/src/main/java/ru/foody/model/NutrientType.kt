package ru.guybydefault.foody.domain

data class NutrientType(
    val name: String,
    val defaultMeasureType: MeasureType
) : BaseEntity(){
}