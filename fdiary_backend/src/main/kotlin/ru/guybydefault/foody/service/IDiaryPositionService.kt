package ru.guybydefault.foody.service

import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.domain.NutrientsInfo
import java.time.Instant

interface IDiaryPositionService {

    fun getNutrientsAverageStats(start: Instant, end: Instant): NutrientsInfo

    fun getDiaryPositions(start: Instant, end: Instant): List<DiaryPosition>

    fun removeDiaryPosition(id: Int): Boolean
}