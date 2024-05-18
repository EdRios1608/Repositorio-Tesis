package com.example.ex3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//Adaptador para mostrar la informacion de los hijos en las clases
class AssignedSonsAdapter(private val sonsList: List<Hijo>) : RecyclerView.Adapter<AssignedSonsAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignedSonsAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.assigned_son, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AssignedSonsAdapter.MyViewHolder, position: Int) {
        val hijoAsignado : Hijo = sonsList[position]
        holder.nombreHijoAsignado.text = hijoAsignado.nombre
        holder.apellidoHijoAsignado.text = hijoAsignado.apellido

    }

    override fun getItemCount(): Int {
        return sonsList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreHijoAsignado : TextView = itemView.findViewById(R.id.txtViewNombreHijoAsignado)
        val apellidoHijoAsignado : TextView = itemView.findViewById(R.id.txtViewApellidoHijoAsignado)
    }
}