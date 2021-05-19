package ru.guybydefault.foody.domain

import ru.foody.model.Recipe

data class RecipeProduct(
    val recipe: Recipe,
    var product: Product,
    var amount: Double
) : BaseEntity() {
}