package ru.foody.api

import retrofit2.Call
import retrofit2.http.*
import ru.foody.model.Recipe
import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.domain.Product
import ru.guybydefault.foody.domain.ProductDto

interface ProductApi {
    @GET("/products")
    fun getProducts(
        @Query("nameLike") likeStr: String,
        @Query("user") user: Int
    ): Call<Array<ProductDto>>
}