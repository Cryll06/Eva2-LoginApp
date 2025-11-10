package com.dorian.eva2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dorian.eva2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, RecoverPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val emailLogin = binding.etEmailLogin.text.toString().trim().lowercase()
            val passwordLogin = binding.etPasswordLogin.text.toString().trim()

            val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val emailSaved = sharedPreferences.getString("email", null)
            val passwordSaved = sharedPreferences.getString("password", null)

            if (emailLogin == emailSaved && passwordLogin == passwordSaved && emailLogin.isNotEmpty()) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}