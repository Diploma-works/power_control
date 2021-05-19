package ru.guybydefault.foody.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import ru.guybydefault.foody.domain.Recipe
import ru.guybydefault.foody.domain.RecipeProduct
import ru.guybydefault.foody.domain.RecipeTag
import ru.guybydefault.foody.domain.User
import ru.guybydefault.foody.dtos.RecipeDto
import ru.guybydefault.foody.dtos.RecipeTagDto
import ru.guybydefault.foody.repository.RecipeRepository
import java.util.*
import javax.persistence.criteria.JoinType
import kotlin.collections.HashSet

@Service
class RecipeService @Autowired constructor(
    private val recipeRepository: RecipeRepository,
    private val productService: ProductService,
    private val nutrientService: NutrientService
) {
    final var cuisines: List<String?>? = null
        get() {
            if (field == null) {
                val cuisinesSet: MutableSet<String?> = HashSet()
                for ((_, tags) in recipeRepository.findAll()) {
                    for ((name) in tags) {
                        if (name.endsWith("кухня")) {
                            cuisinesSet.add(name)
                        }
                    }
                }
                field = ArrayList<String?>(cuisinesSet)
            }
            return field
        }
        private set
    final var categories: List<String?>? = null
        get() {
            if (field == null) {
                val categoriesSet: MutableSet<String?> = HashSet()
                for ((_, tags) in recipeRepository.findAll()) {
                    for ((name) in tags) {
                        categoriesSet.add(name)
                    }
                }
                for (cuisineTag in cuisines!!) {
                    categoriesSet.remove(cuisineTag)
                }
                field = ArrayList<String?>(categoriesSet)
            }
            return field
        }
        private set

    fun saveRecipe(recipeDto: RecipeDto, user: User?): Recipe {
        val recipe = convertToRecipe(recipeDto, user)
        return recipeRepository.save(recipe)
    }

    fun convertToRecipe(recipeDto: RecipeDto, user: User?): Recipe {
        val recipe = Recipe(
            recipeDto.name,
            mutableListOf(),
            mutableListOf(),
            nutrientService.convertToNutrient(recipeDto.nutrientsInfo),
            recipeDto.imageLink,
            recipeDto.minutesPrepared,
            recipeDto.description,
            user
        )
        recipe.tags =
            recipeDto.tags.map { recipeTagDto: RecipeTagDto -> RecipeTag(recipeTagDto.name, recipe) }
        recipe.products = recipeDto.products.map { recipeProductDto ->
            RecipeProduct(
                recipe,
                productService.convertToProduct(recipeProductDto.product),
                recipeProductDto.amount
            )
        }.toCollection(
            mutableListOf()
        )
        return recipe
    }

    companion object Specs {
        fun hasTagSpecification(tag: String): Specification<Recipe> {
            return Specification { recipe, criteriaQuery, criteriaBuilder ->
                criteriaBuilder.equal(
                    recipe.join<Recipe, RecipeTag>("tags", JoinType.INNER).get<String>("name"),
                    tag
                )
            }
        }

        fun caloriesMaxSpec(caloriesMax: Int): Specification<Recipe> {
            return Specification() { recipe, criteriaQuery, criteriaBuilder ->
                criteriaBuilder.lessThanOrEqualTo(
                    recipe.get<Any>("nutrientsInfo").get("calories"), caloriesMax
                )
            }
        }

        fun caloriesMinSpec(caloriesMin: Int): Specification<Recipe> {
            return Specification() { recipe, criteriaQuery, criteriaBuilder ->
                criteriaBuilder.greaterThanOrEqualTo(
                    recipe.get<Any>("nutrientsInfo").get("calories"), caloriesMin
                )
            }
        }

        fun maxTimePreparedSpec(maxTimePrepared: Int): Specification<Recipe> {
            return Specification() { recipe, criteriaQuery, criteriaBuilder ->
                criteriaBuilder.lessThanOrEqualTo(recipe.get("minutesPrepared"), maxTimePrepared)
            }
        }

        fun imageLinkPresentSpec(): Specification<Recipe> {
            return return Specification() { recipe, criteriaQuery, criteriaBuilder ->
                criteriaBuilder.isNotNull(recipe.get<Any>("imageLink")) // instead of true            }
            }
        }
    }

    fun findAll(
        caloriesMin: Int?,
        caloriesMax: Int?,
        cuisine: String?,
        category: String?,
        rating: Int?,
        maxTimePrepared: Int?
    ): Iterable<Recipe> {

        var spec = imageLinkPresentSpec()
        if (caloriesMin != null) {
            spec = spec.and(caloriesMinSpec(caloriesMin))
        }
        if (caloriesMax != null) {
            spec = spec.and(caloriesMaxSpec(caloriesMax))
        }
        if (cuisine != null) {
            spec = spec.and(hasTagSpecification(cuisine))
        }
        if (category != null) {
            spec = spec.and(hasTagSpecification(category))
        }
        if (maxTimePrepared != null) {
            spec = spec.and(maxTimePreparedSpec(maxTimePrepared))
        }
        return recipeRepository.findAll(spec)
    }
}