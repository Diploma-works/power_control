package ru.foody.model

import ru.guybydefault.foody.domain.NutrientsInfo
import ru.guybydefault.foody.domain.RecipeProduct

class Recipe(
    val name: String,
    val tags: List<String>,
    val products: List<RecipeProduct>,
    val imageLink: String,
    val rating: Double,
    val recipeSteps: MutableList<String>,
//    val ingredients : MutableList<IngredientMeasure>,
    var nutrientsInfo: NutrientsInfo,
    val minutesPrepared: String
) {
}