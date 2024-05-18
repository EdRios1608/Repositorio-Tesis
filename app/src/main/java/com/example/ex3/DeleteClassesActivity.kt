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

//Clase que elimina la informacion de las clases y todas sus referencias
class DeleteClassesActivity : AppCompatActivity() {

    //Variables
    private lateinit var db: FirebaseFirestore
    private lateinit var classesDeleteRecyclerView: RecyclerView
    private lateinit var classesDeleteArrayList : ArrayList<Classes>
    private lateinit var myAdapter: DeleteClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_classes)

        classesDeleteRecyclerView = findViewById(R.id.recyclerViewEliminarClases)
        classesDeleteRecyclerView.layoutManager = LinearLayoutManager(this)
        classesDeleteRecyclerView.setHasFixedSize(true)

        classesDeleteArrayList = arrayListOf()
        myAdapter = DeleteClassAdapter(classesDeleteArrayList,this)

        classesDeleteRecyclerView.adapter = myAdapter

        getData()
    }

    //Muestra la informacion de las clases obteniendo de la base de datos.
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
                                classesDeleteArrayList.add(dc.document.toObject(Classes::class.java))
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
    }
}