package ru.foody

import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.domain.NutrientsInfo
import ru.guybydefault.foody.domain.User
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.util.*

object DummyObjectsGenerationUtility {

    fun Double.roundForDiaryDisplay(): Double {
        return BigDecimal(this).setScale(1, RoundingMode.HALF_UP).toDouble()
    }

    fun genNutrientsInfo(num: Int): MutableList<NutrientsInfo> {
        val r = Random(0)
        val nutrientsInfoList = mutableListOf<NutrientsInfo>()
        for (i in 0..num) {
            val prot = (r.nextDouble() * 10).roundForDiaryDisplay()
            val fat = (r.nextDouble() * 10).roundForDiaryDisplay()
            val carb = (r.nextDouble() * 40).roundForDiaryDisplay()
            val cals = (prot * 4 + fat * 9 + carb * 4).roundForDiaryDisplay()
            val nutrientsInfo = NutrientsInfo(cals, prot, fat, carb)
            nutrientsInfo.nutrients = mutableListOf()
            nutrientsInfoList.add(nutrientsInfo)
        }
        return nutrientsInfoList
    }

    fun getDiaryPositionList(): MutableList<DiaryPosition> {
        val diaryPositionsList = mutableListOf<DiaryPosition>()
        val r = Random(0)
        val count = 10
        val nutrientsInfoList = genNutrientsInfo(count)
        val user = User("test@test.ru", "password")
        user.id = 4
        for (i in 0..count) {
            val diaryPosition =
                DiaryPosition(
                    "Test $i",
                    Instant.now(),
                    nutrientsInfoList.get(i),
                    r.nextInt(450) + 50.0,
                    user
                )
            diaryPositionsList.add(diaryPosition)
        }
        return diaryPositionsList
    }
}