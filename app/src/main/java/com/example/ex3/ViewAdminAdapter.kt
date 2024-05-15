package com.example.ex3

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class ViewAdminAdapter(private val adminList: ArrayList<User>, private val context: Context) : RecyclerView.Adapter<ViewAdminAdapter.MyViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdminAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewAdminAdapter.MyViewHolder, position: Int) {
        val admin : User = adminList[position]

        holder.nombreAdmin.text = admin.nombre
        holder.apellidoAdmin.text = admin.apellido
        holder.telefonoAdmin.text = admin.telefono

        holder.btnAgregarInformacion.setOnClickListener{
            val intent = Intent(context, AdminDataActivity::class.java)
            intent.putExtra("adminEmail", admin.email)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreAdmin : TextView = itemView.findViewById(R.id.txtViewNombreAdmin)
        val apellidoAdmin : TextView = itemView.findViewById(R.id.txtViewApellidoAdmin)
        val telefonoAdmin : TextView = itemView.findViewById(R.id.txtViewTelefonoAdmin)

        val btnAgregarInformacion : TextView = itemView.findViewById(R.id.btnAgregarInformacion)
    }
}