package com.example.ex3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class InscriptionsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var hijosXClasesRecyclerView: RecyclerView
    private lateinit var hijosXClasesArrayList : ArrayList<Hijo>
    private lateinit var myAdapter: InscriptionsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscriptions)

        val userEmail = intent.getStringExtra("userEmail")

        //Manejo del recyclerview
        hijosXClasesRecyclerView = findViewById(R.id.recyclerViewHijosClases)
        hijosXClasesRecyclerView.layoutManager = LinearLayoutManager(this)
        hijosXClasesRecyclerView.setHasFixedSize(true)

        hijosXClasesArrayList = arrayListOf()

        if(userEmail !=null){
            myAdapter = InscriptionsAdapter(hijosXClasesArrayList,this,userEmail)
        }

        hijosXClasesRecyclerView.adapter = myAdapter


        if (userEmail != null) {
            getData(userEmail)
        }

    }

    //Muestra la informacion del hijo y boton de ver clases disponibles
    private fun getData(userEmail: String) {
        db = FirebaseFirestore.getInstance()

        val userDocument = db.collection("usuarios").document(userEmail)
        userDocument.collection("hijos")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Firestore error fatal", error.message.toString())
                        return
                    }
                    hijosXClasesArrayList.clear()
                    for(doc in value!!.documents){
                        val hijo = doc.toObject(Hijo::class.java)
                        hijo?.let{ hijosXClasesArrayList.add(it)}

                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }

}