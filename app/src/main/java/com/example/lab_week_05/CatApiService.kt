package com.example.lab_week_05.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.lab_week_05.model.ImageData

interface CatApiService {
    @GET("images/search")
    fun searchImages(
        @Query("limit") limit: Int
    ): Call<List<ImageData>>
}