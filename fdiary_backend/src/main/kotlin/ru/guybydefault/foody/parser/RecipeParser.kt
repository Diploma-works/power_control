package ru.guybydefault.foody.parser

import com.mongodb.MongoClient
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import ru.guybydefault.foody.domain.NutrientsInfo
import ru.guybydefault.foody.domain.Recipe
import ru.guybydefault.foody.domain.RecipeTag
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional


@Component
@Transactional
class RecipeParser {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun Document.getAmountOfNutrition(): Double {
        return (this.get("measure") as Document).getDouble("amount")
    }

    private fun parseTime(strTime: String): Int? {
        var hours = 0
        var minutes = 0
        var splitTime = strTime.split(' ')
        if ("час" in strTime) {
            hours = splitTime[0].toInt()
            if ("минут" in strTime) {
                minutes = strTime.split(' ')[2].toInt()
            }
        } else if ("минут" in strTime && splitTime.size == 2) {
            minutes = strTime.split(" ")[0].toInt()
        } else {
            println("Unpredictable strTime value: $strTime")
            return null
        }
        return hours * 60 + minutes
    }

    fun parse() {
        val client = MongoClient("localhost", 27017)
        val db = client.getDatabase("foody")
        val collection = db.getCollection("recipes")
        var i = 0
        collection.find().forEach { document: Document? ->
            run {
                if (i < 100) {
                    val recipeDocument = document!!
                    val timePrepared = parseTime(recipeDocument.getString("time"))
                    if (timePrepared != null) {
                        val name = recipeDocument.getString("title")
                        val imageLink = recipeDocument.getString("image_link")
                        if (imageLink != null) {
                            val nutrition: ArrayList<Document> = recipeDocument.get("nutritions") as ArrayList<Document>
                            val calories = nutrition.get(0).getAmountOfNutrition()
                            val protein = nutrition.get(1).getAmountOfNutrition()
                            val fats = nutrition.get(2).getAmountOfNutrition()
                            val carbs = nutrition.get(3).getAmountOfNutrition()
                            val tagsStr: ArrayList<String> = (recipeDocument.get("tags") as ArrayList<String>)
                            val tags = mutableListOf<RecipeTag>()
                            val description =
                                (recipeDocument.get("recipe_steps") as ArrayList<String>).joinToString("\n")

                            val nutrientsInfo = NutrientsInfo(mutableListOf(), calories, protein, fats, carbs)
                            val recipe =
                                Recipe(
                                    name,
                                    mutableListOf(),
                                    mutableListOf(),
                                    nutrientsInfo,
                                    imageLink,
                                    timePrepared,
                                    description,
                                    null
                                )

                            tagsStr.map { tagStr -> RecipeTag(tagStr, recipe) }.toCollection(tags)
                            recipe.tags = tags

                            entityManager.persist(recipe)
                            i++
                        }
                    }
                } else {
                    return
                }
            }
        }
    }
}

@Component
class RecipeLoader : ApplicationListener<ApplicationReadyEvent> {

    @Value(value = "#{new Boolean('\${initdb}'.trim())}")
    private var init: Boolean = false

    @Autowired
    private lateinit var recipeParser: RecipeParser

    override fun onApplicationEvent(applicationReadyEvent: ApplicationReadyEvent) {
        if (init) {
            recipeParser.parse()
        }
    }

}

fun main(arg: Array<String>) {
    val recipeParser = RecipeParser()
    recipeParser.parse()
}

