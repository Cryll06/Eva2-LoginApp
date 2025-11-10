package com.dorian.eva2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class NoticiasAdapter(private val noticias: MutableList<Noticia>) :
    RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder>() {

    class NoticiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvNoticiaTitulo)
        val tvContenido: TextView = itemView.findViewById(R.id.tvNoticiaContenido)
        val tvFecha: TextView = itemView.findViewById(R.id.tvNoticiaFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_noticia, parent, false)
        return NoticiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        val noticia = noticias[position]
        holder.tvTitulo.text = noticia.titulo
        holder.tvContenido.text = noticia.contenido

        noticia.fecha?.let {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL"))
            holder.tvFecha.text = "Fecha: ${dateFormat.format(it)}"
        } ?: run {
            holder.tvFecha.text = "Fecha: Desconocida"
        }
    }

    override fun getItemCount(): Int = noticias.size

    fun updateNoticias(newNoticias: List<Noticia>) {
        noticias.clear()
        noticias.addAll(newNoticias)
        notifyDataSetChanged()
    }
}