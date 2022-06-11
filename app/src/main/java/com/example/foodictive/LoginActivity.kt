package com.example.foodictive

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodictive.databinding.LoginActivityBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(){
    private lateinit var binding: LoginActivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnRegisterLogin.setOnClickListener {
            startActivity(Intent(this,RegistActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            binding.apply {
                val email = editEmail.text.toString().trim()
                val password = editPassword.text.toString().trim()

                when{
                    email.isEmpty() ->{
                        editEmail.error = "please input email"
                        editEmail.requestFocus()
                    }
                }
                if (email.isEmpty() && password.isEmpty()){
                    AlertDialog.Builder(this@LoginActivity)
                        .setMessage("please insert email and password")
                        .setPositiveButton("Oke"){_,_->

                        }
                        .create().show()
                }else{
                    loginUser(email, password)
                }
            }
        }
    }

    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
            if (it.isSuccessful){
                Intent(this,MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }else{
                Toast.makeText(this,"${it.exception?.message}",Toast.LENGTH_SHORT).show()
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
