package com.example.foodictive.api

import com.example.foodictive.response.FoodResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("foods")
    fun getFood(
        @Query("name") name: String
    ):Call<FoodResponse>
}