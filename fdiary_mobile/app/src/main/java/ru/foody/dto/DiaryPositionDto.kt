package ru.foody.dto

import ru.guybydefault.foody.domain.BaseEntity
import ru.guybydefault.foody.domain.NutrientsInfo
import ru.guybydefault.foody.domain.NutrientsInfoDto
import ru.guybydefault.foody.domain.User
import java.time.Instant

data class DiaryPositionDto(
    val id: Int,
    val name: String,
    val date: Instant,
    val nutrientInfo: NutrientsInfoDto,
    val value: Double,
    val user: User
)