package com.example.levelupgamer.api

import com.example.levelupgamer.models.GamingProduct
import com.example.levelupgamer.models.UserProfile
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("products")
    suspend fun getAllProducts(): Response<List<GamingProduct>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<GamingProduct>

    @POST("products")
    suspend fun createProduct(@Body product: GamingProduct): Response<GamingProduct>

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: GamingProduct
    ): Response<GamingProduct>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>

    @POST("users/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<UserProfile>

    @POST("users/register")
    suspend fun register(@Body user: UserProfile): Response<UserProfile>
}

object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}