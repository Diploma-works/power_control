package ru.foody.service

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.foody.api.DAO
import ru.foody.dto.DiaryPositionDto
import ru.foody.dto.RecipeDto
import ru.foody.dto.RecipeProductDto
import ru.foody.dto.RecipeTagDto
import ru.foody.model.Recipe
import ru.guybydefault.foody.domain.*
import java.lang.IllegalStateException
import java.time.*

class DiaryService {


    private val user = User("test@test.ru", "password")

    init {
        user.id = 4
    }

    private val nutrientTypeById = mutableMapOf<Int, NutrientType>()
    private val nutrientsInfoById = mutableMapOf<Int, NutrientsInfo>()
    private val recipesById = mutableMapOf<Int, Recipe>()

    fun addDiaryPosition(name: String, date: Instant, product: Product, amount: Double) {
        DAO.diaryApi.createDiaryPositionFromProduct(name, product.id!!, date, user.id!!, amount)
            .enqueue(object : Callback<DiaryPositionDto> {
                override fun onResponse(
                    call: Call<DiaryPositionDto>,
                    response: Response<DiaryPositionDto>
                ) {
//                TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<DiaryPositionDto>, t: Throwable) {
//                TODO("Not yet implemented")
                }
            })
    }

    fun getDiaryPositionsForCurrentDay(listener: (MutableList<DiaryPosition>) -> Unit) {
        val start = LocalDate.now().atStartOfDay()
            .toInstant(
                ZoneOffset.ofTotalSeconds(OffsetDateTime.now().getOffset().totalSeconds)
            )
        val end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
            .toInstant(ZoneOffset.ofTotalSeconds(OffsetDateTime.now().getOffset().totalSeconds))

        DAO.diaryApi.getDiaryPositions(start, end, user.id!!)
            .enqueue(object : Callback<Array<DiaryPositionDto>> {
                override fun onResponse(
                    call: Call<Array<DiaryPositionDto>>,
                    response: Response<Array<DiaryPositionDto>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val diaryPositions = response.body()!!
                            .map { diaryPositionDto -> diaryPositionDto.toDiaryPosition() }
                            .toMutableList()
                        listener(diaryPositions)
                    }
                }

                override fun onFailure(call: Call<Array<DiaryPositionDto>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun removeDiaryPosition(diaryPosition: DiaryPosition, listener: (Boolean) -> Unit) {
        DAO.diaryApi.deleteDiaryPosition(diaryPosition.id!!).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    listener(true)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun NutrientDto.toNutrient(): Nutrient {
        // TODO handle not existing nutrient types and not existing nutrientsInfos
        val nutrient = Nutrient(
            nutrientTypeById.get(this.nutrientType)!!,
            this.value,
            nutrientsInfoById.get(this.nutrientsInfo)!!
        )
        nutrient.id = this.id
        return nutrient
    }

    private fun List<DiaryPositionDto>.toDiaryPositionList(): List<DiaryPosition> {
        return this.map { diaryPositionDto -> diaryPositionDto.toDiaryPosition() }.toMutableList()
    }

    fun DiaryPositionDto.toDiaryPosition(): DiaryPosition {
        val diaryPosition = DiaryPosition(
            this.name,
            this.date,
            this.nutrientInfo.toNutrientsInfo(),
            this.value,
            this.user
        )
        diaryPosition.id = this.id
        return diaryPosition
    }

    fun RecipeDto.toRecipe(): Recipe {
        val recipeTags = mutableListOf<String>()
        val recipeProducts = mutableListOf<RecipeProduct>()
        val recipe = Recipe(
            this.name,
            recipeTags,
            recipeProducts,
            this.imageLink,
            this.rating,
            mutableListOf(),
            this.nutrientsInfo.toNutrientsInfo(),
            this.minutesPrepared
        )
        recipesById.put(this.id, recipe)
        this.products.forEach { recipeProductDto ->
            run {
                recipeProducts.add(recipeProductDto.toRecipeProduct())
            }
        }
        this.tags.forEach { recipeTagDto ->
            run {
                recipeTags.add(recipeTagDto.name)
            }
        }
        return recipe
    }


    fun RecipeProductDto.toRecipeProduct(): RecipeProduct {
        return RecipeProduct(recipesById.get(this.recipe)!!, this.product.toProduct(), this.amount)
    }

    private fun List<NutrientDto>.toNutrientList(): List<Nutrient> {
        return this.map { nutrientDto -> nutrientDto.toNutrient() }.toMutableList()
    }

    private fun NutrientsInfoDto.toNutrientsInfo(): NutrientsInfo {
        val nutrientsInfo = NutrientsInfo(
            this.calories,
            this.protein,
            this.fat,
            this.carb
        )
        nutrientsInfo.id = this.id
        nutrientsInfoById.put(this.id, nutrientsInfo)
        nutrientsInfo.nutrients = this.nutrients.toNutrientList()
        return nutrientsInfo
    }

    private fun ProductDto.toProduct(): Product {
        if (this.nutrientsInfo == null) {
            throw IllegalStateException()
        }
        val product = Product(
            this.name,
            this.nutrientsInfo.toNutrientsInfo(),
            this.dateCreated,
            this.dateUpdated,
            this.user
        )
        product.id = this.id
        return product
    }

    fun initNutrientTypesAndRequestProducts(
        likeStr: String,
        listener: (MutableList<Product>) -> Unit
    ) {
        DAO.nutrientApi.getNutrientTypes().enqueue(object : Callback<Array<NutrientType>> {
            override fun onResponse(
                call: Call<Array<NutrientType>>,
                response: Response<Array<NutrientType>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!
                        .forEach { nutrientTypeById.put(it.id!!, it) }
                    requestProducts(likeStr, listener)
                }
            }

            override fun onFailure(call: Call<Array<NutrientType>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun searchNutrientTypes(
        str: String,
        nutrientTypeList: MutableList<NutrientType>
    ): MutableList<NutrientType> {
        return nutrientTypeList.filter { nutrientType ->
            nutrientType.name.contains(
                str,
                ignoreCase = true
            )
        }.toMutableList()
    }

    fun searchNutrientTypes(
        str: String,
        listener: (full: MutableList<NutrientType>, searched: MutableList<NutrientType>) -> Unit
    ) {
        if (nutrientTypeById.isEmpty()) {
            DAO.nutrientApi.getNutrientTypes().enqueue(object : Callback<Array<NutrientType>> {
                override fun onResponse(
                    call: Call<Array<NutrientType>>,
                    response: Response<Array<NutrientType>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val full = response.body()!!.toMutableList()
                        listener(full, searchNutrientTypes(str, full))
                    } else {
                        TODO("Not yet implemented")
                    }
                }

                override fun onFailure(call: Call<Array<NutrientType>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            val full = nutrientTypeById.values.toMutableList()
            listener(full, searchNutrientTypes(str, full))
        }
    }

    private fun requestProducts(likeStr: String, listener: (MutableList<Product>) -> Unit) {
        DAO.productApi.getProducts(likeStr, user.id!!).enqueue(
            object : retrofit2.Callback<Array<ProductDto>> {
                override fun onResponse(
                    call: Call<Array<ProductDto>>,
                    response: Response<Array<ProductDto>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val productDtoArray = response.body()!!
                        listener(productDtoArray.map { it.toProduct() }.toMutableList())
                    } else {
                        // TODO err
//                        val toast = Toast.makeText(
//                            activity!!.applicationContext,
//                            "Error loading data: " + t.message!!,
//                            Toast.LENGTH_SHORT
//                        )
//                        toast.show()
                    }
                }

                override fun onFailure(call: Call<Array<ProductDto>>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }

    fun getProducts(likeStr: String, listener: (MutableList<Product>) -> Unit) {
        if (nutrientTypeById.isEmpty()) {
            initNutrientTypesAndRequestProducts(likeStr, listener)
        } else {
            requestProducts(likeStr, listener)
        }
    }

    fun getRecipes(
        maxTimePrepared: Int?,
        minRating: Int?,
        caloriesMin: Int,
        caloriesMax: Int,
        cuisine: String?,
        category: String?,
        listener: (recipes: MutableList<Recipe>) -> Unit
    ) {
        DAO.recipeApi.getRecipes(
            maxTimePrepared, minRating, caloriesMin, caloriesMax, cuisine, category
        ).enqueue(object : Callback<Array<RecipeDto>> {
            override fun onResponse(
                call: Call<Array<RecipeDto>>,
                response: Response<Array<RecipeDto>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val recipes = mutableListOf<Recipe>()
                    response.body()!!.forEach {
                        recipes.add(it.toRecipe())
                    }
                    listener(recipes)
                } else {
                    TODO("Not yet implemented")
                }
            }

            override fun onFailure(call: Call<Array<RecipeDto>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


}


