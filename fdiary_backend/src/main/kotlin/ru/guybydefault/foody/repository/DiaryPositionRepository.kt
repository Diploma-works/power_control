package ru.guybydefault.foody.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.guybydefault.foody.domain.DiaryPosition
import java.time.Instant

@Repository
interface DiaryPositionRepository : CrudRepository<DiaryPosition, Int> {

    fun getDiaryPositionByUserIdAndDateIsBetween(userId: Int, start: Instant, end: Instant): List<DiaryPosition>

}