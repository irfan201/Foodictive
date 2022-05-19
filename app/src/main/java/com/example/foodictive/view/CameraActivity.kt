package com.example.foodictive.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.foodictive.MainActivity.Companion.CAMERA_RESULT
import com.example.foodictive.R
import com.example.foodictive.createFile
import com.example.foodictive.databinding.ActivityCameraBinding
import com.example.foodictive.uriToFile
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutors: ExecutorService
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutors = Executors.newSingleThreadExecutor()

        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA))
                CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
            binding.captureImage.setOnClickListener { takePhoto() }
        }

    }

    public override fun onResume() {
        super.onResume()
        hideUI()
        startCamera()
    }

    public override fun onDestroy() {
        super.onDestroy()
        cameraExecutors.shutdown()
    }

    private fun hideUI(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        supportActionBar?.hide()
    }

    private fun takePhoto(){
        val capture = imageCapture ?: return

        val photoFile = createFile(application)

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        capture.takePicture(outputOption,ContextCompat.getMainExecutor(this),
        object : ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Toast.makeText(this@CameraActivity,"berhasil mengambil gambar",Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.putExtra("picture",photoFile)
                intent.putExtra("isBackCamera",cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                setResult(CAMERA_RESULT,intent)
                finish()
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@CameraActivity,"gagal mengambil gambar",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startCamera(){
        val cameraProvideFuture = ProcessCameraProvider.getInstance(this)

        cameraProvideFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProvideFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            } catch (e: Exception){
                Toast.makeText(this,"gagal memunculkan camera",Toast.LENGTH_SHORT).show()
            }
        },ContextCompat.getMainExecutor(this))
    }



}