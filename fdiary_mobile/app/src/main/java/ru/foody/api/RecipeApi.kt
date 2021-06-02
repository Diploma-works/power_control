package ru.foody.api

import retrofit2.Call
import retrofit2.http.*
import ru.foody.dto.RecipeDto
import ru.foody.model.Recipe

interface RecipeApi {
    @GET("/recipes/cuisines")
    fun getAllCuisines(): Call<Array<String>>

    @GET("/recipes")
    fun getRecipes(
        @Query("maxTimePrepared") maxTimePrepared: Int?,
        @Query("minRating") minRating: Int?,
        @Query("caloriesMin") minCalories: Int,
        @Query("caloriesMax") maxColories: Int,
        @Query("cuisine") cuisine: String?,
        @Query("category") category: String?
    )
            : Call<Array<RecipeDto>>

    @GET("/recipes/categories")
    fun getAllCategories(): Call<Array<String>>
}