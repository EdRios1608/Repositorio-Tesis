package com.example.ex3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AssignedClassesActivity : AppCompatActivity() {

    //Declaracion de variables.
    private lateinit var db: FirebaseFirestore
    private lateinit var assignedClassesRecyclerView: RecyclerView
    private lateinit var assignedClassesArrayList: ArrayList<Classes>
    private lateinit var assignedClassNames: ArrayList<String>
    private lateinit var myAdapter: AssignedClassesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assigned_classses)

        db = FirebaseFirestore.getInstance()

        // Muestras las clases asignadas
        assignedClassNames = intent.getStringArrayListExtra("clasesAsignadas") ?: arrayListOf()

        assignedClassesRecyclerView = findViewById(R.id.assignedClassesRecyclerView)
        assignedClassesRecyclerView.layoutManager = LinearLayoutManager(this)
        assignedClassesRecyclerView.setHasFixedSize(true)

        assignedClassesArrayList = arrayListOf()
        myAdapter = AssignedClassesAdapter(assignedClassesArrayList,this)
        assignedClassesRecyclerView.adapter = myAdapter

        // Funcion para cargar las clases asignadas.
        loadAssignedClasses()
    }

    private fun loadAssignedClasses() {
        if (assignedClassNames.isEmpty()) {
            Toast.makeText(this, "No assigned classes to load", Toast.LENGTH_LONG).show()
            return
        }

        // A local list to collect class objects before updating the adapter
        val tempClassList = ArrayList<Classes>()

        assignedClassNames.forEach { className ->
            db.collection("clases").document(className).get()
                .addOnSuccessListener { documentSnapshot ->
                    val clase = documentSnapshot.toObject(Classes::class.java)
                    if (clase != null) {
                        tempClassList.add(clase)
                        if (tempClassList.size == assignedClassNames.size) {
                            // Once all classes are loaded, update the RecyclerView
                            assignedClassesArrayList.clear()
                            assignedClassesArrayList.addAll(tempClassList)
                            myAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(this, "Failed to parse class data for: $className", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error loading class details: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
