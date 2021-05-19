package ru.foody.dto

import ru.foody.model.Recipe

data class RecipeTagDto(
    val id: Int,
    val name: String,
    val recipe: Int
) {
}