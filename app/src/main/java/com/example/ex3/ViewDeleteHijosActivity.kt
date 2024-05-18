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

//Actividad que elimina la informacion de los hijos y sus referencias
class ViewDeleteHijosActivity : AppCompatActivity() {

    //Variables
    private lateinit var db: FirebaseFirestore
    private lateinit var hijosRecyclerView: RecyclerView
    private lateinit var hijosArrayList : ArrayList<Hijo>
    private lateinit var myAdapter: ViewDeleteHijosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_delete_hijos)

        val userEmail = intent.getStringExtra("userEmail")

        hijosRecyclerView = findViewById(R.id.recyclerViewVerEliminarHijos)
        hijosRecyclerView.layoutManager = LinearLayoutManager(this)
        hijosRecyclerView.setHasFixedSize(true)

        hijosArrayList = arrayListOf()

        if (userEmail != null) {
            myAdapter = ViewDeleteHijosAdapter(hijosArrayList, this, userEmail)
        }

        hijosRecyclerView.adapter = myAdapter

        if (userEmail != null) {
            getData(userEmail)
        }

    }

    private fun getData(userEmail: String) {
        db = FirebaseFirestore.getInstance()

        val userDocument = db.collection("usuarios").document(userEmail)
        userDocument.collection("hijos")
            .addSnapshotListener(object :EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Firestore error fatal", error.message.toString())
                        return
                    }
                    hijosArrayList.clear()
                    for(doc in value!!.documents){
                        val hijo = doc.toObject(Hijo::class.java)
                        hijo?.let{ hijosArrayList.add(it)}

                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }

}