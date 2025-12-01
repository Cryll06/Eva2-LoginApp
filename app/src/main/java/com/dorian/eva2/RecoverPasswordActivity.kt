package com.dorian.eva2

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dorian.eva2.databinding.ActivityRecoverPasswordBinding
import com.google.firebase.firestore.FirebaseFirestore

class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecoverPasswordBinding
    private val db = FirebaseFirestore.getInstance()
    private var idDocumentoUsuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBackRecover.setOnClickListener {
            finish()
            }

        binding.layoutNewPassword.visibility = View.GONE

        binding.btnVerifyUser.setOnClickListener {
            val email = binding.etEmailRecover.text.toString().trim().lowercase()
            if (email.isNotEmpty()) {
                buscarYResetear(email)
            } else {
                Toast.makeText(this, "Ingresa el correo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUpdatePassword.text = "Copiar Contraseña y Salir"

        binding.btnUpdatePassword.setOnClickListener {
            val nuevaClave = binding.etNewPassword.text.toString()
            if (nuevaClave.isNotEmpty()) {
                copiarAlPortapapeles(nuevaClave)
            }
        }
    }

    private fun buscarYResetear(email: String) {
        db.collection("usuarios").whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val documento = documents.documents[0]
                    idDocumentoUsuario = documento.id

                    val nuevaPass = generarPasswordAleatoria()

                    actualizarPasswordEnNube(idDocumentoUsuario, nuevaPass)
                } else {
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al buscar: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarPasswordEnNube(docId: String, nuevaPass: String) {
        db.collection("usuarios").document(docId).update("password", nuevaPass)
            .addOnSuccessListener {
                // Éxito
                Toast.makeText(this, "¡Contraseña Generada!", Toast.LENGTH_SHORT).show()

                // Bloqueamos la parte de arriba
                binding.btnVerifyUser.isEnabled = false
                binding.etEmailRecover.isEnabled = false
                binding.layoutNewPassword.visibility = View.VISIBLE
                binding.etNewPassword.setText(nuevaPass)
                binding.etNewPassword.inputType = InputType.TYPE_CLASS_TEXT
                binding.etNewPassword.isEnabled = false
                binding.etConfirmPassword.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error de conexión: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun generarPasswordAleatoria(): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6)
            .map { caracteres.random() }
            .joinToString("")
    }

    private fun copiarAlPortapapeles(texto: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Contraseña Eva2", texto)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "Copiado al portapapeles", Toast.LENGTH_SHORT).show()
        finish()
    }
}