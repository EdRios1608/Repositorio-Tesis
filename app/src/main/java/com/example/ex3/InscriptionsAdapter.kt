package com.example.ex3

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//Adaptador para las inscripciones de los hijos
class InscriptionsAdapter(private var hijoListXClases: ArrayList<Hijo>, private val context: Context, private val userEmail: String): RecyclerView.Adapter<InscriptionsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InscriptionsAdapter.MyViewHolder {


        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.incriptions_hijo, parent, false)

        return  MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: InscriptionsAdapter.MyViewHolder, position: Int) {

        val hijoClase : Hijo = hijoListXClases[position]

        holder.nombreHijo.text = hijoClase.nombre
        holder.apellidoHijo.text = hijoClase.apellido
        holder.edadHijo.text = hijoClase.edad.toString()

        holder.btnVerClasesDisponibles.setOnClickListener {
            val intent = Intent(context, AvailableClassesActivity::class.java)
            intent.putExtra("userEmail", userEmail)
            intent.putExtra("nombreHijo", hijoClase.nombre)
            intent.putExtra("edadHijo", hijoClase.edad)
            intent.putExtra("Hijo", hijoClase)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return hijoListXClases.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nombreHijo: TextView = itemView.findViewById(R.id.txtViewNombreHijoClases)
        val apellidoHijo: TextView = itemView.findViewById(R.id.txtViewApellidoHijoClases)
        val edadHijo: TextView = itemView.findViewById(R.id.txtViewEdadHijoClases)

        val btnVerClasesDisponibles: Button = itemView.findViewById(R.id.btnVerClasesDisponibles)

    }

}