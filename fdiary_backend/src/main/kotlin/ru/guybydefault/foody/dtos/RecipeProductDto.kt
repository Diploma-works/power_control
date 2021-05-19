package ru.guybydefault.foody.dtos



data class RecipeProductDto(
    val id: Int,
    val recipe: Int,
    var product: ProductDto,
    var amount: Double
) {
}