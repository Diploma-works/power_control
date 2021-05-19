package ru.guybydefault.foody.domain

import java.time.Instant

data class DiaryPosition(
    val name : String,
    val date: Instant,
    val nutrientInfo: NutrientsInfo,
    val value: Double,
    val user: User
) : BaseEntity() {
}