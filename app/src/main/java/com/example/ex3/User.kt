package com.example.ex3


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var nombre: String = "",
    var apellido: String = "",
    var email: String = "",
    var password1: String = "",
    var password2: String = "",
    var telefono: String = "",
    var cedula: String = "",
    var club: String = "",
    var rango: Int = 0, // Por defecto, todos los usuarios son de rango 0

    var hijos: ArrayList<Hijo> = arrayListOf(),
    var clasesAsignadas: ArrayList<String> = arrayListOf()

) : Parcelable {

    companion object {
        fun fromDocument(document: Map<String, Any>):User{
            return User(
                nombre = document["nombre"] as? String ?: "",
                apellido = document["apellido"] as? String ?: "",
                email = document["email"] as? String ?: "",
                password1 = document["password1"] as? String ?: "",
                password2 = document["password2"] as? String ?: "",
                telefono = document["telefono"] as? String ?: "",
                cedula = document["cedula"] as? String ?: "",
                club = document["club"] as? String ?: "",
                rango = (document["rango"] as? Long)?.toInt() ?: 0
            )
        }
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "apellido" to apellido,
            "email" to email,
            "password1" to password1,
            "password2" to password2,
            "telefono" to telefono,
            "cedula" to cedula,
            "club" to club,
            "rango" to rango,
            "hijos" to hijos.map { hijo -> hijo.toMap() },
            "clasesAsignadas" to clasesAsignadas
        )
    }
}