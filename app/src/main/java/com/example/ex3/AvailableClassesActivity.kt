package com.example.ex3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


// Clase para la actividad de ver clases disponibles para los hijos
class AvailableClassesActivity : AppCompatActivity() {

    //Definicion de variables
    private lateinit var db: FirebaseFirestore
    private lateinit var availableClasesRecyclerView: RecyclerView
    private lateinit var availableClasesArrayList : ArrayList<Classes>
    private lateinit var myAdapter: AvailableClassesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_classes)

        val edadHijo = intent.getIntExtra("edadHijo",0)
        val userEmail = intent.getStringExtra("userEmail")
        val nombreHijo = intent.getStringExtra("nombreHijo")

        val hijo: Hijo? = intent.getParcelableExtra("Hijo")

        if (userEmail == null || nombreHijo == null || hijo == null) {
            Toast.makeText(this, "Falta información del usuario o del hijo.", Toast.LENGTH_LONG).show()
            //finish() // Cierra esta Activity si no hay datos esenciales
            return
        }

        Toast.makeText(baseContext,"Edad hijo: $edadHijo", Toast.LENGTH_SHORT).show()

        availableClasesRecyclerView = findViewById(R.id.recyclerViewClasesDisponiblesXHijo)
        availableClasesRecyclerView.layoutManager = LinearLayoutManager(this)
        availableClasesRecyclerView.setHasFixedSize(true)

        availableClasesArrayList = arrayListOf()

        myAdapter = AvailableClassesAdapter(availableClasesArrayList,this,userEmail, hijo)

        availableClasesRecyclerView.adapter = myAdapter

        getData(edadHijo)


    }


    //Funcion para obtener las clases disponibles para los hijos en base a su edad y se filtran las clases
    private fun getData(edadHijo: Int) {
        db = FirebaseFirestore.getInstance()
        db.collection("clases")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("Firestore Error", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val filteredClasesList = ArrayList<Classes>()
                for (doc in snapshots!!.documents) {
                    val clase = doc.toObject(Classes::class.java)
                    if (clase != null && clase.edadMinima <= edadHijo && clase.edadMaxima >= edadHijo) {
                        filteredClasesList.add(clase)
                    }
                }

                // Actualiza el ArrayList usado por tu adaptador y notifica los cambios
                availableClasesArrayList.clear()
                availableClasesArrayList.addAll(filteredClasesList)
                myAdapter.notifyDataSetChanged()
            }
    }



}