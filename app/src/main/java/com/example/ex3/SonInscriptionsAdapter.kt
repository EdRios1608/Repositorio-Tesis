package com.example.ex3

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SonInscriptionsAdapter(private val inscriptionsXHijo: ArrayList<Hijo>, private val context: Context, private val userEmail: String): RecyclerView.Adapter<SonInscriptionsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonInscriptionsAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_inscriptions, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SonInscriptionsAdapter.MyViewHolder, position: Int) {
        val hijoInscripciones: Hijo = inscriptionsXHijo[position]

        holder.nombreHijo.text = hijoInscripciones.nombre
        holder.apellidoHijo.text = hijoInscripciones.apellido
        holder.edadHijo.text = hijoInscripciones.edad.toString()


        holder.btnVerEliminarInscripciones.setOnClickListener {
            // Prepara el intent y agrega los extras necesarios
            val intent = Intent(context, ViewInscriptionsActivity::class.java).apply {
                putExtra("userEmail", userEmail)
                putExtra("nombreHijo", hijoInscripciones.nombre)
                putExtra("edadHijo", hijoInscripciones.edad)
                putExtra("Hijo", hijoInscripciones) // Asegúrate de que Hijo implemente Parcelable
            }
            // Usa el contexto para iniciar la actividad
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return inscriptionsXHijo.size
    }

    public class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val nombreHijo: TextView = itemView.findViewById(R.id.txtViewNombreHijoInscripciones)
        val apellidoHijo: TextView = itemView.findViewById(R.id.txtViewApellidoHijoInscripciones)
        val edadHijo: TextView = itemView.findViewById(R.id.txtViewEdadHijoInscripciones)

        val btnVerEliminarInscripciones: Button = itemView.findViewById(R.id.btnVerInscripcionesXHijo)

    }
}