package com.example.ex3

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ViewDeleteHijosAdapter(private val hijoList: ArrayList<Hijo>, private val context: Context, private val userEmail: String): RecyclerView.Adapter<ViewDeleteHijosAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewDeleteHijosAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_delete_hijo, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewDeleteHijosAdapter.MyViewHolder, position: Int) {
        val hijo: Hijo = hijoList[position]
        holder.nombreHijo.text = hijo.nombre
        holder.apellidoHijo.text = hijo.apellido
        holder.edadHijo.text = hijo.edad.toString()

        holder.btnEliminarHijo.setOnClickListener {
            eliminarHijo(userEmail, hijo, position)
        }
    }

    override fun getItemCount(): Int {
        return hijoList.size
    }


    private fun eliminarHijo(userEmail: String, hijo: Hijo, position: Int) {
        val db = FirebaseFirestore.getInstance()
        val userHijosRef = db.collection("usuarios").document(userEmail).collection("hijos").document(hijo.nombre)

        AlertDialog.Builder(context).apply {
            setTitle("Eliminar Hijo")
            setMessage("¿Estás seguro de querer eliminar a ${hijo.nombre}?")
            setPositiveButton(android.R.string.yes) { dialog, which ->
                // Inicia una operación batch
                val batch = db.batch()

                // Itera sobre las clases inscritas del hijo y elimina su referencia de cada clase
                hijo.clasesInscritas.forEach { nombreClase ->
                    val claseRef = db.collection("clases").document(nombreClase)
                    // Actualizar los hijos inscritos y cupos restantes
                    batch.update(claseRef, "hijosInscritos", FieldValue.arrayRemove(mapOf("nombre" to hijo.nombre, "apellido" to hijo.apellido)))
                    batch.update(claseRef, "cuposRestantes", FieldValue.increment(1))
                }

                // Eliminar el hijo de la subcolección 'hijos'
                batch.delete(userHijosRef)

                // Ejecutar la operación batch
                batch.commit().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hijoList.removeAt(position)
                        Toast.makeText(context, "${hijo.nombre} eliminado correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al eliminar hijo: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            setNegativeButton(android.R.string.no, null)
            show()
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nombreHijo: TextView = itemView.findViewById(R.id.txtViewNombreHijo)
        val apellidoHijo: TextView = itemView.findViewById(R.id.txtViewApellidoHijo)
        val edadHijo: TextView = itemView.findViewById(R.id.txtViewEdadHijo)
        val btnEliminarHijo: Button = itemView.findViewById(R.id.btnEliminarHijo)
    }
}
