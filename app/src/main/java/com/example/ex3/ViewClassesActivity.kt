package com.example.ex3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

//Clase que muestra todas las actividades disponibles
class ViewClassesActivity : AppCompatActivity() {

    //Variables
    private lateinit var db: FirebaseFirestore
    private lateinit var classesRecyclerView: RecyclerView
    private lateinit var classesArrayList : ArrayList<Classes>
    private lateinit var myAdapter: ViewClassesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_classes)

        classesRecyclerView = findViewById(R.id.recyclerViewClasesDisponibles)
        classesRecyclerView.layoutManager = LinearLayoutManager(this)
        classesRecyclerView.setHasFixedSize(true)

        classesArrayList = arrayListOf()

        myAdapter = ViewClassesAdapter(classesArrayList)
        classesRecyclerView.adapter = myAdapter

        getData()

    }

    private fun getData(){
        db = FirebaseFirestore.getInstance()
        db.collection("clases"). 
                addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if(error != null){
                            Log.e("Firestore Error", error.message.toString())
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                classesArrayList.add(dc.document.toObject(Classes::class.java))
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
    }
}