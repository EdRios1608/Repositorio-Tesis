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

//Adaptador que muestra la informacion de las clases disponibles para los hijos.
class ViewInscriptionsAdapter(private val clasesInscritasXHijo: ArrayList<Classes>, private val context: Context, private val userEmail: String, private val nombreHijo: String): RecyclerView.Adapter<ViewInscriptionsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewInscriptionsAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.son_classes, parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewInscriptionsAdapter.MyViewHolder, position: Int) {
        val claseInscrita = clasesInscritasXHijo[position]

        holder.nombreClase.text = claseInscrita.nombreClase
        holder.nombreProfesor.text = claseInscrita.nombreProfesor
        holder.diaClase.text = claseInscrita.dia
        holder.horaInicio.text = claseInscrita.horaInicio
        holder.horaFin.text = claseInscrita.horaFin

        holder.btnEliminarInscripcionHijo.setOnClickListener {
            eliminarInscripcion(position, userEmail, nombreHijo)
        }

    }

    override fun getItemCount(): Int {
        return clasesInscritasXHijo.size
    }

    public class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val nombreClase : TextView = itemView.findViewById(R.id.txtViewNombreClaseHijoInscrito)
        val nombreProfesor : TextView = itemView.findViewById(R.id.txtViewNombreProfesorClaseHijoInscrito)
        val diaClase : TextView = itemView.findViewById(R.id.txtViewDiaClaseHijoInscrito)
        val horaInicio : TextView = itemView.findViewById(R.id.txtViewHoraInicioClaseHijoInscrito)
        val horaFin : TextView = itemView.findViewById(R.id.txtViewHoraFinClaseHijoInscrito)

        val btnEliminarInscripcionHijo: Button = itemView.findViewById(R.id.btnEliminarInscripcionClase)

    }

    fun eliminarInscripcion(position: Int, userEmail: String, nombreHijo: String) {
        val clase = clasesInscritasXHijo[position]
        val db = FirebaseFirestore.getInstance()

        val hijoDocRef = db.collection("usuarios").document(userEmail).collection("hijos").document(nombreHijo)
        val claseDocRef = db.collection("clases").document(clase.nombreClase)

        AlertDialog.Builder(context).apply {
            setTitle("Confirmar eliminación")
            setMessage("¿Estás seguro de querer eliminar la inscripción en la clase '${clase.nombreClase}' de ${nombreHijo}?")
            setPositiveButton(android.R.string.yes) { dialog, which ->
                db.runTransaction { transaction ->
                    val hijoSnapshot = transaction.get(hijoDocRef)
                    val claseSnapshot = transaction.get(claseDocRef)

                    if (hijoSnapshot.exists() && claseSnapshot.exists()) {
                        // Actualizar la lista de clases inscritas del hijo
                        val nuevasClasesInscritas = (hijoSnapshot["clasesInscritas"] as List<String>).filter { it != clase.nombreClase }
                        transaction.update(hijoDocRef, "clasesInscritas", nuevasClasesInscritas)

                        // Actualizar la lista de hijos inscritos en la clase
                        val nuevosHijosInscritos = (claseSnapshot["hijosInscritos"] as List<Map<String, String>>).filterNot {
                            it["nombre"] == nombreHijo && it["apellido"] == hijoSnapshot.getString("apellido")
                        }
                        transaction.update(claseDocRef, "hijosInscritos", nuevosHijosInscritos)

                        // Incrementar los cupos restantes de la clase
                        val cuposRestantesActuales = (claseSnapshot.getLong("cuposRestantes") ?: 0L) + 1
                        transaction.update(claseDocRef, "cuposRestantes", cuposRestantesActuales)
                    }
                }.addOnSuccessListener {
                    clasesInscritasXHijo.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, clasesInscritasXHijo.size) // Para actualizar correctamente las posiciones
                    Toast.makeText(context, "Inscripción eliminada correctamente", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Toast.makeText(context, "Error al eliminar la inscripción: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            setNegativeButton(android.R.string.no, null)
            show()
        }
    }


}