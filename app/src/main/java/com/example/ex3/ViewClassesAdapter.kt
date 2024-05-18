package com.example.ex3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Adaptador que muestra las clases
class ViewClassesAdapter(private val classList : ArrayList<Classes>) : RecyclerView.Adapter<ViewClassesAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewClassesAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.class_item,
            parent,false)
        return  MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewClassesAdapter.MyViewHolder, position: Int) {
        val currentClass : Classes = classList[position]

        //Para string
        holder.nombreClase.text = currentClass.nombreClase
        holder.nombreProfesor.text = currentClass.nombreProfesor
        holder.lugar.text = currentClass.lugar

        //Para int
        holder.cupos.text = currentClass.cupos.toString()
        holder.cuposRestantes.text = currentClass.cuposRestantes.toString()

        holder.dia.text = currentClass.dia
        holder.horaInicio.text = currentClass.horaInicio
        holder.horaFin.text = currentClass.horaFin

        holder.edadMinima.text = currentClass.edadMinima.toString()
        holder.edadMaxima.text = currentClass.edadMaxima.toString()


    }

    override fun getItemCount(): Int {
        return classList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //Variables
        val nombreClase : TextView = itemView.findViewById(R.id.txtViewNombreClase)
        val nombreProfesor : TextView = itemView.findViewById(R.id.txtViewNombreProfesor)
        val lugar : TextView = itemView.findViewById(R.id.txtViewLugar)
        val cupos : TextView = itemView.findViewById(R.id.txtViewCupos)
        val cuposRestantes : TextView = itemView.findViewById(R.id.txtViewCuposRestantes)
        val dia : TextView = itemView.findViewById(R.id.txtViewDia)
        val horaInicio : TextView = itemView.findViewById(R.id.txtViewHoraInicio)
        val horaFin : TextView = itemView.findViewById(R.id.txtViewHoraFin)
        val edadMinima : TextView = itemView.findViewById(R.id.txtViewEdadMinima)
        val edadMaxima : TextView = itemView.findViewById(R.id.txtViewEdadMaxima)

    }
}