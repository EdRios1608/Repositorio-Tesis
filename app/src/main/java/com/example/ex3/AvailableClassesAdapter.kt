package com.example.ex3

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

// Adaptador para mostrar la informacion de las clases en base a la edad del hijo
class AvailableClassesAdapter(
    private val clasesDisponiblesList: ArrayList<Classes>,
    private val context: Context,
    private val userEmail: String,
    private val hijo: Hijo
) : RecyclerView.Adapter<AvailableClassesAdapter.MyViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inscripciones_disponibles_hijo, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val clase = clasesDisponiblesList[position]

        holder.nombreClaseDisponible.text = clase.nombreClase
        holder.diaClaseDisponible.text = clase.dia
        holder.horaInicioClaseDisponible.text = clase.horaInicio
        holder.horaFinClaseDisponible.text = clase.horaFin

        holder.cuposClasesDisponibles.text = clase.cupos.toString()
        holder.cuposRestantesClasesDisponibles.text = clase.cuposRestantes.toString()

        val clasesInscritas = hijo.clasesInscritas.toMutableList()


        //funcion para mostrar las clases, en el caso de que este inscrito, no podra aplastar el boton y viceversa.
        if (clasesInscritas.contains(clase.nombreClase) || clase.cuposRestantes <= 0) {
            holder.btnInscribirHijo.text = "Completo/Inscrito"
            holder.btnInscribirHijo.isEnabled = false
        } else {
            holder.btnInscribirHijo.text = "Inscribir Hijo"
            holder.btnInscribirHijo.isEnabled = true
            holder.btnInscribirHijo.setOnClickListener {
                if (clase.cuposRestantes > 0 && !clasesInscritas.contains(clase.nombreClase)) {
                    agregarClaseAHijo(userEmail, hijo.nombre, clase.nombreClase)
                    agregarHijoAClase(clase, hijo, holder)
                    clasesInscritas.add(clase.nombreClase)
                    hijo.clasesInscritas = clasesInscritas
                    notifyItemChanged(position)
                }
            }
        }


    }

    override fun getItemCount() = clasesDisponiblesList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreClaseDisponible: TextView = itemView.findViewById(R.id.txtViewNombreClaseDisponible)
        val diaClaseDisponible: TextView = itemView.findViewById(R.id.txtViewDiaClaseDisponible)
        val horaInicioClaseDisponible: TextView = itemView.findViewById(R.id.txtViewHoraInicioClaseDisponible)
        val horaFinClaseDisponible: TextView = itemView.findViewById(R.id.txtViewHoraFinClaseDisponible)
        val cuposClasesDisponibles: TextView = itemView.findViewById(R.id.txtViewCuposClasesDisponibles)
        val cuposRestantesClasesDisponibles: TextView = itemView.findViewById(R.id.txtViewCuposRestantesClasesDisponibles)

        val btnInscribirHijo: Button = itemView.findViewById(R.id.btnInscribirHijo)
    }

    //Funciones importates para agregar todos los datos necesarios a distintas partes.
    //Estas referencias seran usadas para poder actualizar los datos correctamente.

    private fun agregarClaseAHijo(userEmail: String, nombreHijo: String, nombreClase: String) {
        db.collection("usuarios").document(userEmail).collection("hijos").document(nombreHijo)
            .update("clasesInscritas", FieldValue.arrayUnion(nombreClase))
            .addOnSuccessListener {
                Toast.makeText(context, "Inscripción realizada con éxito.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al realizar la inscripción.", Toast.LENGTH_SHORT).show()
            }
    }


    private fun agregarHijoAClase(clase: Classes, hijo: Hijo, holder: MyViewHolder) {
        val claseRef = db.collection("clases").document(clase.nombreClase)
        val hijoMap = mapOf("nombre" to hijo.nombre, "apellido" to hijo.apellido)

        claseRef.update("hijosInscritos", FieldValue.arrayUnion(hijoMap), "cuposRestantes", FieldValue.increment(-1))
            .addOnSuccessListener {
                Toast.makeText(context, "Hijo inscrito en la clase con éxito.", Toast.LENGTH_SHORT).show()
                clase.cuposRestantes -= 1
                holder.cuposRestantesClasesDisponibles.text = clase.cuposRestantes.toString()
                holder.btnInscribirHijo.text = "Hijo Inscrito"
                holder.btnInscribirHijo.isEnabled = false
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al inscribir al hijo en la clase: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
