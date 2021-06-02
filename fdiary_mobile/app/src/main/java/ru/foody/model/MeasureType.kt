package ru.guybydefault.foody.domain

data class MeasureType(
    val name: String,
    val grams: Double
) : BaseEntity() {
}