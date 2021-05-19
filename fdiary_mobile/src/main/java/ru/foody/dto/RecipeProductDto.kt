package ru.foody.dto

import ru.foody.model.Recipe
import ru.guybydefault.foody.domain.ProductDto

data class RecipeProductDto(
    val id: Int,
    val recipe: Int,
    var product: ProductDto,
    var amount: Double
) {
}