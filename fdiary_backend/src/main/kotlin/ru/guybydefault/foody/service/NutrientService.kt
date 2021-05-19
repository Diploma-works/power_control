package ru.guybydefault.foody.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.guybydefault.foody.domain.*
import ru.guybydefault.foody.dtos.NutrientDto
import ru.guybydefault.foody.dtos.NutrientsInfoDto
import ru.guybydefault.foody.repository.NutrientRepository
import ru.guybydefault.foody.repository.NutrientTypeRepository

@Service
class NutrientService : INutrientService {

    @Autowired
    private lateinit var nutrientRepository: NutrientRepository

    @Autowired
    private lateinit var nutrientTypeRepository: NutrientTypeRepository

    private val nutrientTypesById = mutableMapOf<Int, NutrientType>()

    override fun addNutrient(nutrient: Nutrient): Nutrient {
        return nutrientRepository.save(nutrient)
    }

    override fun removeNutrient(nutrientId: Int): Boolean {
        TODO("Not yet implemented")
    }

    private fun initNutrientTypesById() {
        for (nutrientType in nutrientTypeRepository.findAll()) {
            nutrientTypesById.put(nutrientType.id!!, nutrientType)
        }
    }

    fun getNutrientTypesById(): Map<Int, NutrientType> {
        if (nutrientTypesById.isEmpty()) {
            initNutrientTypesById()
        }
        return nutrientTypesById
    }

    fun getNutrientTypeById(id: Int): NutrientType? {
        return getNutrientTypesById().get(id)
    }

    fun convertToNutrient(nutrientsInfoDto: NutrientsInfoDto): NutrientsInfo {
        val nutrientsList = mutableListOf<Nutrient>()
        val nutrientsInfo = NutrientsInfo(
            nutrientsList,
            nutrientsInfoDto.calories,
            nutrientsInfoDto.protein,
            nutrientsInfoDto.fat,
            nutrientsInfoDto.carb
        )
        nutrientsInfoDto.nutrients.map { nutrientDto: NutrientDto ->
            Nutrient(
                getNutrientTypeById(nutrientDto.nutrientType)!!,
                nutrientDto.value,
                nutrientsInfo
            )
        }.toCollection(nutrientsList)
        return nutrientsInfo
    }
}