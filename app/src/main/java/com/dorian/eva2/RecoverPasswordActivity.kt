package com.dorian.eva2

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dorian.eva2.databinding.ActivityRecoverPasswordBinding

class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecoverPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        binding.btnVerifyUser.setOnClickListener {
            val emailToRecover = binding.etEmailRecover.text.toString().trim().lowercase()
            val emailSaved = sharedPreferences.getString("email", null)

            if (emailToRecover.isNotEmpty() && emailToRecover == emailSaved) {
                Toast.makeText(this, "Usuario verificado", Toast.LENGTH_SHORT).show()
                binding.layoutNewPassword.visibility = View.VISIBLE
                binding.btnVerifyUser.isEnabled = false
                binding.etEmailRecover.isEnabled = false
            } else {
                Toast.makeText(this, "El correo no se encuentra registrado", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUpdatePassword.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (newPassword.isNotEmpty() && newPassword == confirmPassword) {
                val editor = sharedPreferences.edit()
                editor.putString("password", newPassword)
                editor.apply()

                Toast.makeText(this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden o están vacías", Toast.LENGTH_SHORT).show()
            }
        }
    }
}