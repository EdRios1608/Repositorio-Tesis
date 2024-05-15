package com.example.ex3
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hijo(
    var nombre: String= "",
    var apellido: String= "",
    var fechaNacimiento: String= "",
    var edad: Int = 0,

    //Lista de strings clasesInscritas
    var clasesInscritas: List<String> = arrayListOf()

) : Parcelable {
    fun toMap(): Map<String, Any> = mapOf(
        "nombre" to nombre,
        "apellido" to apellido,
        "fechaNacimiento" to fechaNacimiento,
        "edad" to edad,
        "clasesInscritas" to clasesInscritas
    )
}
