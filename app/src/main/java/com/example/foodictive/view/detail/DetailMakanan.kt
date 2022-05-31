package com.example.foodictive.view.detail

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.foodictive.MainActivity.Companion.CAMERA_RESULT
import com.example.foodictive.R
import com.example.foodictive.databinding.ActivityDetailMakananBinding
import com.example.foodictive.response.Food
import com.example.foodictive.uriToFile
import com.example.foodictive.view.CameraActivity
import java.io.File

class DetailMakanan : AppCompatActivity() {
    private lateinit var detailModel: DetailModel
    private lateinit var binding: ActivityDetailMakananBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.identifikasi.setOnClickListener { setupViewModel() }
        binding.addGalery.setOnClickListener { startGalery() }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == CAMERA_RESULT){
            val myFile = data?.getSerializableExtra("picture") as File

            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.imageDetail.setImageBitmap(result)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startGalery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent,"Choose a picture")
        lancuhIntentGalery.launch(chooser)
    }

    private val lancuhIntentGalery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg,this)
            getFile = myFile
            binding.imageDetail.setImageURI(selectedImg)
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