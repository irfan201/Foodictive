package com.example.foodictive

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodictive.databinding.RegistActivityBinding

class RegistActivity : AppCompatActivity(), View.OnClickListener{
    private var _binding: RegistActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = RegistActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewSet()
    }

    private fun viewSet() {
        with(binding) {
            btnRegister.setOnClickListener(this@RegistActivity)
            imgBackRegister.setOnClickListener(this@RegistActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnRegister -> startActivity(Intent(this, MainActivity::class.java))
            binding.imgBackRegister -> startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
