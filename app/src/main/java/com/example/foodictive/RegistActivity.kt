package com.example.foodictive

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodictive.databinding.RegistActivityBinding
import com.google.firebase.auth.FirebaseAuth

class RegistActivity : AppCompatActivity(){
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: RegistActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.imgBackRegister.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            binding.apply {
            val username = editNameRegister.text.toString().trim()
            val email = editEmailRegister.text.toString().trim()
            val password = editPasswordRegister.text.toString().trim()

            when{
                username.isEmpty() ->{
                    editNameRegister.error = "please input username"
                }
                email.isEmpty() ->{
                    editEmailRegister.error = "please input email"
                    editEmailRegister.requestFocus()
                }
            }
                if (email.isEmpty() && password.isEmpty()){
                    AlertDialog.Builder(this@RegistActivity)
                        .setMessage("please insert email and password")
                        .setPositiveButton("Oke"){_,_->

                        }
                        .create().show()
                }else{
                    registUser(email,password)
                }
        }
        }
    }

    private fun registUser(email: String, password:String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                startActivity(Intent(this,LoginActivity::class.java))
            }else{
                Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }
    }



}
