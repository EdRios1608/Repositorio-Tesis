package com.example.ex3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class ViewInscriptionsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var inscripcionesXHijoRecyclerView: RecyclerView
    private lateinit var inscripcionesXHijoArrayList : ArrayList<Classes>
    private lateinit var myAdapter: ViewInscriptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_inscriptions)

        val userEmail = intent.getStringExtra("userEmail")
        val nombreHijo = intent.getStringExtra("nombreHijo")


        inscripcionesXHijoRecyclerView = findViewById(R.id.inscripcionesXHijoRecyclerView)
        inscripcionesXHijoRecyclerView.layoutManager = LinearLayoutManager(this)
        inscripcionesXHijoRecyclerView.setHasFixedSize(true)

        inscripcionesXHijoArrayList = arrayListOf()

        if(userEmail != null){
            if(nombreHijo != null){
                myAdapter = ViewInscriptionsAdapter(inscripcionesXHijoArrayList,this, userEmail,nombreHijo)
            }

        }
        //myAdapter = ViewInscriptionsAdapter(inscripcionesXHijoArrayList,this, userEmail,nombreHijo)
        inscripcionesXHijoRecyclerView.adapter = myAdapter

        if (userEmail != null) {
            if (nombreHijo != null) {
                getData(userEmail,nombreHijo)
            }
        }

    }


    //Funcion para mostrar las clases en las que los hijos estan inscritos
    private fun getData(userEmail: String, nombreHijo: String) {
        db = FirebaseFirestore.getInstance()

        // Obtener la referencia del documento del hijo específico
        val hijoRef = db.collection("usuarios").document(userEmail).collection("hijos").document(nombreHijo)

        hijoRef.get().addOnSuccessListener { documento ->
            if (documento.exists()) {
                val clasesInscritas = documento["clasesInscritas"] as? List<String>
                if (clasesInscritas.isNullOrEmpty()) {
                    Toast.makeText(this, "No hay clases inscritas para mostrar.", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                // Consulta para obtener todas las clases a la vez si es posible
                db.collection("clases").whereIn(FieldPath.documentId(), clasesInscritas).get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(this, "No se encontraron detalles de las clases inscritas.", Toast.LENGTH_LONG).show()
                            return@addOnSuccessListener
                        }

                        documents.forEach { document ->
                            val clase = document.toObject(Classes::class.java)
                            inscripcionesXHijoArrayList.add(clase)
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        Log.e("ViewInscriptions", "Error al obtener clases inscritas: ${e.message}")
                    }
            } else {
                Toast.makeText(this, "Datos del hijo no encontrados.", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener { e ->
            Log.e("ViewInscriptions", "Error al obtener datos del hijo: ${e.message}")
            Toast.makeText(this, "Error al obtener datos del hijo: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


}