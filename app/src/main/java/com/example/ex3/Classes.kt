package com.example.ex3

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//ir poniendo mas cosas

@Parcelize
data class Classes(var nombreClase: String = "",
                   var nombreProfesor: String = "",
                   var lugar: String = "",
                   var cupos: Int = 0,
                   var cuposRestantes: Int = 0,
                   var dia: String = "",
                   var horaInicio: String = "",
                   var horaFin: String = "",
                   var edadMinima: Int = 0,
                   var edadMaxima: Int = 0,

                   //Lista de hijosInscritos en la clase
                   var hijosInscritos: List<Hijo> = arrayListOf()

) : Parcelable {

    fun toMap(): Map<String, Any> {
        return mapOf(
            //Importante que los nombres sean iguales que en la base de datos
            "nombreClase" to nombreClase,
            "nombreProfesor" to nombreProfesor,
            "lugar" to lugar,
            "cupos" to cupos,
            "cuposRestantes" to cuposRestantes,
            "dia" to dia,
            "horaInicio" to horaInicio,
            "horaFin" to horaFin,
            "edadMinima" to edadMinima,
            "edadMaxima" to edadMaxima,
            "hijosInscritos" to hijosInscritos.map { hijo -> hijo.toMap() }
        )
    }
}
