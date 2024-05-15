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

class SonInscriptionsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var inscripcionesXHijoRecyclerView: RecyclerView
    private lateinit var inscripcionesXHijoArrayList : ArrayList<Hijo>
    private lateinit var myAdapter: SonInscriptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_son_inscriptions)

        val userEmail = intent.getStringExtra("userEmail")

        //Manejo del recyclerview
        inscripcionesXHijoRecyclerView = findViewById(R.id.recyclerViewInscripcionesXHijo)
        inscripcionesXHijoRecyclerView.layoutManager = LinearLayoutManager(this)
        inscripcionesXHijoRecyclerView.setHasFixedSize(true)

        inscripcionesXHijoArrayList = arrayListOf()

        if(userEmail !=null){
            myAdapter = SonInscriptionsAdapter(inscripcionesXHijoArrayList,this,userEmail)
        }

        inscripcionesXHijoRecyclerView.adapter = myAdapter


        if (userEmail != null) {
            getData(userEmail)
        }

    }

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
                    inscripcionesXHijoArrayList.clear()
                    for(doc in value!!.documents){
                        val hijo = doc.toObject(Hijo::class.java)
                        hijo?.let{ inscripcionesXHijoArrayList.add(it)}

                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }


}