package ru.foody.api

import retrofit2.Call
import retrofit2.http.*
import ru.foody.dto.DiaryPositionDto
import ru.foody.model.Recipe
import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.domain.NutrientType
import ru.guybydefault.foody.domain.Product
import java.time.Instant

interface DiaryApi {
    @GET("/diaryPositions")
    fun getDiaryPositions(
        @Query("start") start: Instant,
        @Query("end") end: Instant,
        @Query("userId") userId: Int
    ): Call<Array<DiaryPositionDto>>

    @POST("/diaryPositions/fromProduct")
    fun createDiaryPositionFromProduct(
        @Query("name") name: String,
        @Query("productId") productId: Int,
        @Query("date") date: Instant,
        @Query("userId") userId: Int,
        @Query("value") value: Double
    ): Call<DiaryPositionDto>

    @DELETE("/diaryPositions/{id}")
    fun deleteDiaryPosition(
        @Path("id") id: Int
    ): Call<Void>
}
