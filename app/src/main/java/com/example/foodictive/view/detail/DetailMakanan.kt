package com.example.foodictive.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.foodictive.R
import com.example.foodictive.databinding.ActivityDetailMakananBinding
import com.example.foodictive.response.Food

class DetailMakanan : AppCompatActivity() {
    private lateinit var detailModel: DetailModel
    private lateinit var binding: ActivityDetailMakananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.identifikasi.setOnClickListener {
            setupViewModel()
        }



    }

    private fun setupViewModel(){
        detailModel = ViewModelProvider(this).get(DetailModel::class.java)
        val data = intent.getParcelableExtra<Food>("food") as Food
        detailModel.setFood()
        detailModel.foodData.observe(this,{
            setFoodData(it.food!!)
        })
    }

    private fun setFoodData(data: Food){
        binding.apply {
            namaMakanan.text = data.name
                descMakanan.text = data.description
                bahanMakanan.text = data.ingredients
                caraMasak.text = data.howtocook
        }
    }



}