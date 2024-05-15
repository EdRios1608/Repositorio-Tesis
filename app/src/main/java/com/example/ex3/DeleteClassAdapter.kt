package com.example.ex3

import android.app.AlertDialog
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



class DeleteClassAdapter(private val classList : ArrayList<Classes>, private val context: Context) : RecyclerView.Adapter<DeleteClassAdapter.MyViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteClassAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.delete_class_item, parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val claseBorrar: Classes = classList[position]
        holder.nombreClaseDelete.text = claseBorrar.nombreClase
        holder.nombreProfesorDelete.text = claseBorrar.nombreProfesor

        holder.btnDeleteClass.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("¿Estás seguro de querer eliminar la clase '${claseBorrar.nombreClase}'?")
                .setPositiveButton("Eliminar") { dialog, which ->
                    eliminarClaseYReferencias(claseBorrar.nombreClase, position)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }


    }

    private fun eliminarClaseYReferencias(nombreClase: String, position: Int) {
        try {
            // Iniciar un batch para las operaciones de base de datos
            val batch = db.batch()

            // Paso 1: Eliminar la clase de 'clasesAsignadas' de cada usuario (profesor) que la tiene asignada
            db.collection("usuarios")
                .whereArrayContains("clasesAsignadas", nombreClase)
                .get()
                .addOnSuccessListener { usersSnapshot ->
                    for (userDoc in usersSnapshot) {
                        val userRef = userDoc.reference
                        // Actualizar la lista de clasesAsignadas eliminando la clase
                        batch.update(userRef, "clasesAsignadas", FieldValue.arrayRemove(nombreClase))
                    }

                    // Paso 2: Eliminar todas las referencias de la clase de cualquier colección de 'hijos' que la contenga
                    db.collectionGroup("hijos")
                        .whereArrayContains("clasesInscritas", nombreClase)
                        .get()
                        .addOnSuccessListener { hijosSnapshot ->
                            for (hijoDoc in hijosSnapshot) {
                                batch.update(hijoDoc.reference, "clasesInscritas", FieldValue.arrayRemove(nombreClase))
                            }

                            // Paso 3: Eliminar la clase de la colección de clases
                            val claseRef = db.collection("clases").document(nombreClase)
                            batch.delete(claseRef)

                            // Ejecutar el batch
                            batch.commit().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    classList.removeAt(position)
                                    notifyItemRemoved(position)
                                    Toast.makeText(context, "Clase y referencias eliminadas correctamente.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error al eliminar clase y actualizar referencias: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error al buscar hijos inscritos: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al buscar profesores con clase asignada: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }



    override fun getItemCount(): Int {
        return classList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nombreClaseDelete : TextView = itemView.findViewById(R.id.txtViewNombreClaseDelete)
        val nombreProfesorDelete : TextView = itemView.findViewById(R.id.txtViewNombreProfesorDelete)

        val btnDeleteClass : Button = itemView.findViewById(R.id.btnDeleteClass)
    }
}