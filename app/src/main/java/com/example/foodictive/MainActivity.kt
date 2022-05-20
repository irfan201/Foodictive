package com.example.foodictive

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodictive.databinding.ActivityMainBinding
import com.example.foodictive.view.CameraActivity
import java.io.File
import kotlin.math.abs

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var binding: ActivityMainBinding
    private var getFile: File? = null
    private lateinit var gestureDetector: GestureDetector
    private var x1 = 0.0f
    private var x2 = 0.0f
    private var y1 = 0.0f
    private var y2 = 0.0f

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

        gestureDetector = GestureDetector(this,this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)

        when(event?.action){
            0->{
                x1 = event.x
                y1 = event.y
            }
            1->{
                x2 = event.x
                y2 = event.y

                val valueX = x2-x1
                val valueY = y2-y1

                if (abs(valueX) > MIN_DISTANCE){
                    if (x2 > x1){
                        val intent = Intent(this,CameraActivity::class.java)
                        intentCamera.launch(intent)
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                    }
                }
            }
        }
        return super.onTouchEvent(event)
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

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    companion object{
        const val CAMERA_RESULT = 200
        const val MIN_DISTANCE = 150
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }
}