package com.example.foodictive

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
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
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->{
                    editEmailRegister.error = "email is not valid"
                    editEmailRegister.requestFocus()
                }
                password.isEmpty() ->{
                    editPasswordRegister.error = "please input password and must be more than 6 character"
                    editPasswordRegister.requestFocus()
                }
            }
                registUser(email,password)
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

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            Intent(this,MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }


}
