package com.example.foodictive

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodictive.databinding.LoginActivityDummyBinding

class LoginActivityDummy : AppCompatActivity(), View.OnClickListener{
    private var _binding: LoginActivityDummyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LoginActivityDummyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewSet()
    }

    private fun viewSet() {
        with(binding) {
            btnRegisterLogin.setOnClickListener(this@LoginActivityDummy)
            btnLogin.setOnClickListener(this@LoginActivityDummy)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
