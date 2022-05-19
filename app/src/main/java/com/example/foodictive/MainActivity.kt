package com.example.foodictive

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodictive.databinding.ActivityMainBinding
import com.example.foodictive.view.CameraActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION){
            if (!allPermissionGranted()){
                Toast.makeText(this,"tidak mendapat permission",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext,it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION)
        }

        binding.addFood.setOnClickListener {
            val intent = Intent(this,CameraActivity::class.java)
            intentCamera.launch(intent)
        }
        binding.addGalery.setOnClickListener { startGalery() }
    }

    private val intentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == CAMERA_RESULT){
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera",true) as Boolean

            getFile = myFile
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path),isBackCamera)

            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun startGalery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
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
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    companion object{
        const val CAMERA_RESULT = 200

        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }
}