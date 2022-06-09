package com.example.foodictive

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodictive.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener{
    private var _binding: LoginActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewSet()
    }

    private fun viewSet() {
        with(binding) {
            btnRegisterLogin.setOnClickListener(this@LoginActivity)
            btnLogin.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> startActivity(Intent(this, MainActivity::class.java))
            binding.btnRegisterLogin -> startActivity(Intent(this, RegistActivity::class.java))
        }
    }
}
