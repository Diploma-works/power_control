package ru.guybydefault.foody.dtos

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

public class RecipeDto(
    val id: Int,
    val name: String,
    val tags: List<RecipeTagDto>,
    val products: List<RecipeProductDto>,
    val imageLink: String,
    val rating: Double,
    val description: String,
    var nutrientsInfo: NutrientsInfoDto,
    val minutesPrepared: Int
) {
}