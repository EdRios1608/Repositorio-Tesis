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

//Activity que permite hacer nuevos administradores
class MakeAdminActivity : AppCompatActivity() {

    //Variables
    private lateinit var db: FirebaseFirestore
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList : ArrayList<User>
    private lateinit var myAdapter: MakeAdminAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_admin)

        userRecyclerView = findViewById(R.id.userList)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()
        myAdapter = MakeAdminAdapter(userArrayList, FirebaseFirestore.getInstance())
        userRecyclerView.adapter = myAdapter
        getUserData()


    }

    //Funcion que obtiene la informacion de los usuarios de la base de datos
    private fun getUserData(){
        db = FirebaseFirestore.getInstance()
        db.collection("usuarios").
        addSnapshotListener(object :EventListener<QuerySnapshot>{
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if(error!=null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for(dc:DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.document.toObject(User::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }

        })
    }
}