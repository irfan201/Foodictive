package com.example.foodictive.api

import com.example.foodictive.response.FoodResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("ayam_bakar")
    fun getFood():Call<FoodResponse>
}