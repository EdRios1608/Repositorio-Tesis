package com.example.ex3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AssignedSonsActivity : AppCompatActivity() {

    //Definicion de variables
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerViewHijosAsignados: RecyclerView
    private lateinit var arrayListHijosAsignados: ArrayList<Hijo>
    private lateinit var myAdapter: AssignedSonsAdapter
    private lateinit var nombreClase: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assigned_sons)

        db = FirebaseFirestore.getInstance()
        nombreClase = intent.getStringExtra("nombreClase") ?: ""



        recyclerViewHijosAsignados = findViewById(R.id.recyclerViewHijosAsignados)
        recyclerViewHijosAsignados.layoutManager = LinearLayoutManager(this)
        recyclerViewHijosAsignados.setHasFixedSize(true)

        arrayListHijosAsignados = intent.getParcelableArrayListExtra<Hijo>("hijosInscritos") ?: arrayListOf()

        myAdapter = AssignedSonsAdapter(arrayListHijosAsignados)
        recyclerViewHijosAsignados.adapter = myAdapter

        if (nombreClase.isNotEmpty()) {
            loadHijosInscritos()
        } else {
            Toast.makeText(this, "No se proporcionó el nombre de la clase.", Toast.LENGTH_LONG).show()
        }

    }


    //Funcion para obtener la informacion de los hijos por medio de la base de datos.
    private fun loadHijosInscritos() {
        db.collection("clases").document(nombreClase).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val clase = documentSnapshot.toObject(Classes::class.java)
                    if (clase != null && clase.hijosInscritos.isNotEmpty()) {
                        arrayListHijosAsignados.clear()  // Limpia la lista antes de agregar nuevos elementos
                        arrayListHijosAsignados.addAll(clase.hijosInscritos)
                        myAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, "No hay hijos inscritos en esta clase.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Clase no encontrada.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar los detalles de la clase: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

}
