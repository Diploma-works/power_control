package ru.guybydefault.foody.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.repository.DiaryPositionRepository
import java.time.Instant

@Service
class DiaryService {

    @Autowired
    private lateinit var diaryPositionRepository: DiaryPositionRepository

    fun getDiaryPositions(userId: Int, start: Instant, end: Instant): List<DiaryPosition> {
        return diaryPositionRepository.getDiaryPositionByUserIdAndDateIsBetween(userId, start, end)
    }

    fun saveDiaryPosition(diaryPosition: DiaryPosition): DiaryPosition {
        return diaryPositionRepository.save(diaryPosition)
    }

    fun deleteDiaryPosition(id: Int) {
        diaryPositionRepository.deleteById(id)
    }

    fun getDiaryPosition(id: Int): DiaryPosition? {
        return diaryPositionRepository.findById(id).orElse(null)
    }

}