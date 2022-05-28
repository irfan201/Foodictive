package com.example.foodictive.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodictive.api.apiConfig
import com.example.foodictive.response.Data
import com.example.foodictive.response.Food
import com.example.foodictive.response.FoodResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailModel: ViewModel() {

    private val _foodData = MutableLiveData<Data>()
    val foodData: LiveData<Data> = _foodData

    fun setFood(){
        val client = apiConfig.getApiService().getFood()
        client.enqueue(object : Callback<FoodResponse>{
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _foodData.postValue(responseBody.data)
                    }
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Log.e(TAG.toString(),"onFailure: ${t.message}")
            }
        })
    }

    companion object{
        val TAG = DetailModel::class.java
    }
}