package com.example.foodictive.view.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.foodictive.databinding.ActivityDetailMakananBinding
import com.example.foodictive.ml.ModelFp16
import com.example.foodictive.response.FoodsItem
import com.example.foodictive.uriToFile
import org.tensorflow.lite.support.image.TensorImage
import java.io.File

class DetailMakanan : AppCompatActivity() {
    private lateinit var detailModel: DetailModel
    private lateinit var binding: ActivityDetailMakananBinding
    private var getFile: File? = null
    private lateinit var bitmap:Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.identifikasi.setOnClickListener { setupViewModel() }
        binding.addGalery.setOnClickListener { startGalery() }

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
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImg)
        }
    }



    private fun setupViewModel(){
        val model = ModelFp16.newInstance(this)

        val image = TensorImage.fromBitmap(bitmap)

        val outputs = model.process(image).probabilityAsCategoryList.apply {
            sortByDescending { it.score }
        }
        val probability = outputs[0]
        detailModel = ViewModelProvider(this).get(DetailModel::class.java)
        detailModel.setFood(probability.label)

        model.close()
        detailModel.foodData.observe(this,{
            setFoodData(it.foods)
        })

    }

    private fun setFoodData(data: List<FoodsItem?>?){
        binding.apply {
            if (data != null) {
                for (item in data){
                    namaMakanan.text = item?.name
                    descMakanan.text = item?.description
                    bahanMakanan.text = item?.ingredients
                    caraMasak.text = item?.howtocook

                }
            }
        }
    }



}