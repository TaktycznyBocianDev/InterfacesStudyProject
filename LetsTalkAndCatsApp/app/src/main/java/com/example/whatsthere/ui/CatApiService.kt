package com.example.whatsthere.ui

import com.example.whatsthere.data.CatImage
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



interface CatApiService {
    @GET("images/search")
    suspend fun getCatImages(@Query("limit") limit: Int): List<CatImage>
}

object CatApiClient {
    private const val BASE_URL = "https://api.thecatapi.com/v1/"

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val catApiService = retrofit.create(CatApiService::class.java)
}