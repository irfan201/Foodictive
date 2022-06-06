package com.example.foodictive

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodictive.databinding.ActivityMainBinding
import com.example.foodictive.response.FoodsItem
import com.example.foodictive.view.detail.DetailMakanan
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var food: FoodsItem

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

        food = FoodsItem()
        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION)
            firebaseAuth = FirebaseAuth.getInstance()
            chckUser()
        }
        binding.halamanDetail.setOnClickListener { intentDetail(food) }

    }

    private fun intentDetail(food: FoodsItem){
            val intent = Intent(this,DetailMakanan::class.java)
            intent.putExtra("food",food)
            startActivity(intent)
    }

    private fun chckUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else {
            val user = firebaseUser.email
            binding.userTv.text = user
        }
    }

    companion object{
        const val CAMERA_RESULT = 200
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }
}