package com.example.ex3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class ViewAdminActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adminRecyclerView: RecyclerView
    private lateinit var adminArrayList : ArrayList<User>
    private lateinit var myAdapter: ViewAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_admin)

        db = FirebaseFirestore.getInstance()

        adminRecyclerView = findViewById(R.id.recyclerViewAdministradores)
        adminRecyclerView.layoutManager = LinearLayoutManager(this)
        adminRecyclerView.setHasFixedSize(true)

        adminArrayList = arrayListOf()

        myAdapter = ViewAdminAdapter(adminArrayList, this)
        adminRecyclerView.adapter = myAdapter

        getAdminData()

    }




    private fun getAdminData() {
        db.collection("usuarios")
            .whereEqualTo("rango", 1)  // 'rango' es el campo que determina el nivel de acceso y que 1 es para administradores.
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val admin = dc.document.toObject(User::class.java)
                            adminArrayList.add(admin)
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }

}