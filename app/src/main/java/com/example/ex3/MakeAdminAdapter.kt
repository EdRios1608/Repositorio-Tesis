package com.example.ex3

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MakeAdminAdapter(private val userList: ArrayList<User>, private val db: FirebaseFirestore) : RecyclerView.Adapter<MakeAdminAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.nombre.text = currentItem.nombre
        holder.apellido.text = currentItem.apellido

        // Desvincular el listener temporalmente para evitar disparos durante la configuración inicial
        holder.switch.setOnCheckedChangeListener(null)
        holder.switch.isChecked = currentItem.rango == 1

        // Vincular de nuevo el listener
        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            // Actualizar el rango basado en el estado del switch
            val nuevoRango = if (isChecked) 1 else 0
            currentItem.rango = nuevoRango

            // Actualizar Firestore
            db.collection("usuarios").document(currentItem.email).update("rango", nuevoRango)
                .addOnSuccessListener {
                    Log.d("UserInfoAdapter", "Rango actualizado con éxito para ${currentItem.email}")
                }
                .addOnFailureListener { e ->
                    Log.e("UserInfoAdapter", "Error al actualizar el rango para ${currentItem.email}", e)
                }
        }
    }




    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nombre : TextView = itemView.findViewById(R.id.txtViewNombre)
        val apellido : TextView = itemView.findViewById(R.id.txtViewApellido)
        val switch : Switch = itemView.findViewById(R.id.switchAdmin) as Switch

    }

}