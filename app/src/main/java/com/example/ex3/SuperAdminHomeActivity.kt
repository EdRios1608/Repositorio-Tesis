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
class SuperAdminHomeActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_admin_home)
        firebaseAuth = Firebase.auth

        val btnHacerAdmin = findViewById<Button>(R.id.btnHacerAdmin)
        val btnAdministrarActividades = findViewById<Button>(R.id.btnActividades)
        val btnAdministradores = findViewById<Button>(R.id.btnAdministradores)
        val btnImagenes = findViewById<Button>(R.id.btnImagenes)

        // Obteniendo la instancia de User pasada desde LoginActivity
        val user = intent.getParcelableExtra<User>("userDetails")

        findViewById<TextView>(R.id.txtviewAdmin).text = "SuperAdmin, ${user?.nombre}, ${user?.apellido}"


        btnHacerAdmin.setOnClickListener(){
            val intent = Intent(this, MakeAdminActivity::class.java)
            startActivity(intent)
        }

        btnAdministrarActividades.setOnClickListener(){
            val intent = Intent(this, ClasesAdministrationActivity::class.java)
            startActivity(intent)
        }

        btnAdministradores.setOnClickListener(){
            val intent = Intent(this, ViewAdminActivity::class.java)
            startActivity(intent)
        }

        btnImagenes.setOnClickListener(){
            val intent = Intent(this, ImagesActivity::class.java)
            startActivity(intent)
        }
    }

    //Funcion mostrar menu top
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
        Toast.makeText(baseContext,"Sesion Cerrada.", Toast.LENGTH_SHORT).show()
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
    }
}