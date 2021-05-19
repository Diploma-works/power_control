package ru.guybydefault.foody.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import ru.guybydefault.foody.domain.*
import ru.guybydefault.foody.repository.MeasureTypeRepository
import ru.guybydefault.foody.repository.NutrientTypeRepository
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.time.Instant
import javax.annotation.PostConstruct
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.PersistenceContextType
import javax.transaction.Transactional


@Component
class HealthDietParser {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var measureTypeRepository: MeasureTypeRepository

    @Autowired
    private lateinit var nutrientTypeRepository: NutrientTypeRepository

    private val measureTypeMap: MutableMap<String, MeasureType> = HashMap()
    private val nutrientList: MutableList<NutrientType> = mutableListOf()
    private val nutrientJsonIdByNameMap: MutableMap<String, Int> = HashMap()

    val nutrientTypes = mutableListOf<NutrientType>()

    val objectMapper = ObjectMapper()

    @PostConstruct
    private fun init() {
        for (measureType in measureTypeRepository.findAll()) {
            measureTypeMap[measureType.name] = measureType
        }
    }


    @Transactional
    fun parseData() {
        nutrientTypeRepository.findAll().forEach { nutrientType -> nutrientTypes.add(nutrientType) }

        var time = System.currentTimeMillis();
        val dataDir = File("data")
        var n = 0

        for (file in dataDir.listFiles()) {
            if (n > 10000) {
                break
            }
            val productJsonNode = objectMapper.readTree(file);
            val nutrientsJsonNode = productJsonNode.get("nutrients")
            val nutrientsInfo = parseNutrientsInfo(nutrientsJsonNode)

            if (nutrientsInfo != null) {
                val product =
                    Product(productJsonNode.get("name").textValue(), nutrientsInfo, Instant.now(), Instant.now(), null)
                entityManager!!.persist(product)
                entityManager.flush()
            } else {
                throw IllegalStateException()
            }
            n++;
        }
    }


    @Transactional
    fun parseNutrientsInfo(nutrientsJsonNode: JsonNode): NutrientsInfo {
        val nutrients = mutableListOf<Pair<NutrientType, Double>>()

        for (nutrient in nutrientTypes) {
            val nutrientId = nutrientJsonIdByNameMap.get(nutrient.name)
            val value = nutrientsJsonNode.get(nutrientId.toString())?.doubleValue()
            if (value == null) {
                continue
            }
            nutrients.add(Pair(nutrient, value))
        }

        var cals = 0.0;
        var protein = 0.0;
        var fats = 0.0;
        var carb = 0.0;
        for (nutrient in mutableListOf<String>("белки", "жиры", "углеводы", "калорийность")) {
            val nutrientId = nutrientJsonIdByNameMap.get(nutrient)
            val value = nutrientsJsonNode.get(nutrientId.toString())?.doubleValue()
            if (value == null) {
                continue;
            }
            when (nutrient) {
                "белки" -> protein = value;
                "жиры" -> fats = value;
                "углеводы" -> carb = value;
                "калорийность" -> cals = value;
            }
        }


        val nutrientList = mutableListOf<Nutrient>()
        val nutrientsInfo = NutrientsInfo(nutrientList, cals, protein, fats, carb)
        nutrients.forEach { pair: Pair<NutrientType, Double> ->
            nutrientList.add(
                Nutrient(
                    pair.first,
                    pair.second,
                    nutrientsInfo
                )
            )
        }
        return nutrientsInfo
    }

    @Transactional
    fun parseNutrientTypesAndMeta() {
        val fileMeta = File("nutrient_types_health_diet")
        val br = BufferedReader(FileReader(fileMeta))
        br.use {
            var line = br.readLine()
            while (!line.isNullOrBlank()) {
                val splitted = line.split(",").toTypedArray()
                val id = splitted[0]
                val name = splitted[1]
                val measureTypeStr = splitted[2]
                val measureType = measureTypeMap[measureTypeStr]!!

                nutrientJsonIdByNameMap.put(name, id.toInt())

                if (!(name in mutableListOf("калорийность", "белки", "жиры", "углеводы"))) {
                    try {
                        entityManager.persist(NutrientType(name, measureType))
                    } catch (e: DataIntegrityViolationException) {
                        e.printStackTrace()
                    }
                }
                line = br.readLine()
            }
        }
    }

}
