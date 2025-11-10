package com.dorian.eva2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dorian.eva2.databinding.ActivityNoticiasBinding
import com.google.firebase.firestore.FirebaseFirestore

class NoticiasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticiasBinding
    private lateinit var noticiasAdapter: NoticiasAdapter
    private val firestoreDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticiasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchNoticias()
    }

    private fun setupRecyclerView() {
        val noticiasList = mutableListOf<Noticia>()
        noticiasAdapter = NoticiasAdapter(noticiasList)
        binding.rvNoticias.apply {
            layoutManager = LinearLayoutManager(this@NoticiasActivity)
            adapter = noticiasAdapter
        }
    }

    private fun fetchNoticias() {
        firestoreDb.collection("noticias_chile")
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val fetchedNoticias = mutableListOf<Noticia>()
                for (document in result) {
                    val noticia = document.toObject(Noticia::class.java)
                    noticia.id = document.id
                    fetchedNoticias.add(noticia)
                }
                noticiasAdapter.updateNoticias(fetchedNoticias)
                if (fetchedNoticias.isEmpty()) {
                    Toast.makeText(this, "No hay noticias disponibles", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("NoticiasActivity", "Error fetching noticias", exception)
                Toast.makeText(this, "Error al cargar noticias: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}