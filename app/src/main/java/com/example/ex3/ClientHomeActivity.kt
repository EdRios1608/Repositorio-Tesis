package com.example.ex3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Suppress("DEPRECATION")
class ClientHomeActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)
        firebaseAuth = Firebase.auth

        val btnVerClasesCliente = findViewById<Button>(R.id.btnVerClasesCliente)
        val btnAgregarHijos = findViewById<Button>(R.id.btnAgregarHijos)
        val btnVerEliminarHijos = findViewById<Button>(R.id.btnVerEliminarHijos)
        val btnInscripciones = findViewById<Button>(R.id.btnInscripciones)
        val btnVerEliminarInscripciones = findViewById<Button>(R.id.btnVerEliminarInscripciones)
        val btnContacto = findViewById<Button>(R.id.btnContacto)


        // Obteniendo la instancia de User pasada desde LoginActivity
        val user = intent.getParcelableExtra<User>("userDetails")

        findViewById<TextView>(R.id.txtViewBienvenida).text = "Bienvenido de vuelta ${user?.nombre}"

        btnVerClasesCliente.setOnClickListener(){
            val intent = Intent(this, ViewClassesActivity::class.java)
            startActivity(intent)
        }

        btnAgregarHijos.setOnClickListener(){
            val intent = Intent(this, AddClientSonActivity::class.java)
            startActivity(intent)
        }

        btnVerEliminarHijos.setOnClickListener(){
            val intent = Intent(this, ViewDeleteHijosActivity::class.java).apply {
                putExtra("userEmail", user?.email)
            }
            startActivity(intent)
        }

        btnInscripciones.setOnClickListener(){
            val intent = Intent(this, InscriptionsActivity::class.java).apply {
                putExtra("userEmail", user?.email)
            }
            startActivity(intent)
        }

        btnVerEliminarInscripciones.setOnClickListener(){
            val intent = Intent(this, SonInscriptionsActivity::class.java).apply {
                putExtra("userEmail", user?.email)
            }
            startActivity(intent)
        }

        btnContacto.setOnClickListener(){
            val intent = Intent(this, ContactoActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navtop_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_salir -> {
                signOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signOut(){
        firebaseAuth.signOut()
        Toast.makeText(baseContext,"Sesion Cerrada.",Toast.LENGTH_SHORT).show()
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
    }
}