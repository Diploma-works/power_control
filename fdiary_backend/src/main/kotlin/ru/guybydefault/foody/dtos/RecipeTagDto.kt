package ru.guybydefault.foody.dtos


data class RecipeTagDto(
    val id: Int,
    val name: String,
    val recipe: Int
) {
}