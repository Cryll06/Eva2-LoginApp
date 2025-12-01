package com.dorian.eva2

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dorian.eva2.databinding.ActivityRegisterBinding
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackRegister.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmailRegister.text.toString().trim().lowercase()
            val password = binding.etPasswordRegister.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailRegister.error = "Ingresa un correo real"
                return@setOnClickListener
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (password.length >= 4) {
                    registrarUsuario(email, password)
                } else {
                    Toast.makeText(this, "La contraseña es muy corta (mínimo 4)", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(email: String, pass: String) {
        db.collection("usuarios").whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val nuevoUsuario = hashMapOf(
                        "email" to email,
                        "password" to pass
                    )

                    db.collection("usuarios").add(nuevoUsuario)
                        .addOnSuccessListener {
                            Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}