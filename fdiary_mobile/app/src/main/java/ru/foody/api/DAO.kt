package ru.foody.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.time.Instant


object DAO {
    const val AUTH_HEADER_NAME = "X-AUTH"
    const val BASE_URL = "http://192.168.1.101";

    lateinit var userRepository: UserRepository

    val retrofit: Retrofit
    val recipeApi: RecipeApi
    val productApi: ProductApi
    val nutrientApi: NutrientApi
    val diaryApi: DiaryApi
    val loginApi: LoginApi

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantAdapter())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

        val client = OkHttpClient.Builder().addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response? {
                if (userRepository.authToken == null) {
                    return chain.proceed(chain.request())
                } else {
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader(AUTH_HEADER_NAME, userRepository.authToken!!)
                        .build()
                    return chain.proceed(newRequest)
                }
            }
        }).build()
        client.interceptors()

        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        recipeApi = retrofit.create(RecipeApi::class.java)
        productApi = retrofit.create(ProductApi::class.java)
        nutrientApi = retrofit.create(NutrientApi::class.java)
        diaryApi = retrofit.create(DiaryApi::class.java)
        loginApi = retrofit.create(LoginApi::class.java)
    }
}

class InstantAdapter : JsonDeserializer<Instant?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instant? {
        return Instant.parse(json!!.asJsonPrimitive.asString)
    }
}