package ru.foody.dto

import ru.guybydefault.foody.domain.NutrientsInfoDto

class RecipeDto(
    val id: Int,
    val name: String,
    val tags: List<RecipeTagDto>,
    val products: List<RecipeProductDto>,
    val imageLink: String,
    val rating: Double,
    val recipeSteps: MutableList<String>,
//    val ingredients : MutableList<IngredientMeasure>,
    var nutrientsInfo: NutrientsInfoDto,
    val minutesPrepared: String
) {
}