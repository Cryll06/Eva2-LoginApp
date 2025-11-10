package com.dorian.eva2

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Noticia(
    var id: String = "",
    var titulo: String = "",
    var contenido: String = "",
    @ServerTimestamp
    var fecha: Date? = null
)