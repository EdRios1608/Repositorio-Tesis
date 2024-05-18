package com.example.ex3

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

//Definicion del adaptador para las clases asignadas del usuario
class AssignedClassesAdapter(private val assignedClasses: ArrayList<Classes>, private val context: Context) : RecyclerView.Adapter<AssignedClassesAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignedClassesAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.assigned_class, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AssignedClassesAdapter.MyViewHolder, position: Int) {
        val claseAsignada : Classes = assignedClasses[position]

        //Conexion con lo visual y lo programatico
        holder.nombreClaseAsignada.text = claseAsignada.nombreClase
        holder.diaClaseAsignada.text = claseAsignada.dia
        holder.lugarClaseAsignada.text = claseAsignada.lugar
        holder.horaInicioClaseAsignada.text = claseAsignada.horaInicio
        holder.horaFinClaseAsignada.text = claseAsignada.horaFin

        holder.btnVerInscritos.setOnClickListener {
            val intent = Intent(context, AssignedSonsActivity::class.java).apply {
                putExtra("nombreClase", claseAsignada.nombreClase)
                putParcelableArrayListExtra("hijosInscritos", ArrayList(claseAsignada.hijosInscritos))
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return assignedClasses.size
    }

    public class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        //Conexion con lo visual
        val nombreClaseAsignada : TextView = itemView.findViewById(R.id.txtViewNombreClaseAsignada)
        val diaClaseAsignada : TextView = itemView.findViewById(R.id.txtViewDiaClaseAsignada)
        val lugarClaseAsignada : TextView = itemView.findViewById(R.id.txtViewLugarClaseAsignada)
        val horaInicioClaseAsignada : TextView = itemView.findViewById(R.id.txtViewHoraInicioClaseAsignada)
        val horaFinClaseAsignada : TextView = itemView.findViewById(R.id.txtViewHoraFinClaseAsignada)

        val btnVerInscritos : Button = itemView.findViewById(R.id.btnVerInscritosClaseAsignada)
    }
}