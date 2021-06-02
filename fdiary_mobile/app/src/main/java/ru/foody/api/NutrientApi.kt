package ru.foody.api

import retrofit2.Call
import retrofit2.http.*
import ru.foody.model.Recipe
import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.domain.NutrientType
import ru.guybydefault.foody.domain.Product

interface NutrientApi {
    @GET("/nutrients/types")
    fun getNutrientTypes(
    ): Call<Array<NutrientType>>

}