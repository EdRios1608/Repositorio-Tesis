package com.example.ex3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        firebaseAuth = Firebase.auth

        // Obteniendo la instancia de User pasada desde LoginActivity
        val user = intent.getParcelableExtra<User>("userDetails") ?: User()


        val btnVerClasesVistaAdmin = findViewById<Button>(R.id.btnVerClasesVistaAdmin)
        val btnVerHorarios = findViewById<Button>(R.id.btnVerHorarios)
        val btnVerClasesAsignadas = findViewById<Button>(R.id.btnVerClasesAsiganadas)

        btnVerClasesVistaAdmin.setOnClickListener(){
            val intent = Intent(this, ViewClassesActivity::class.java)
            startActivity(intent)
        }

        btnVerHorarios.setOnClickListener(){
            val intent = Intent(this, AdminImageActivity::class.java).apply {
                putExtra("adminEmail", user.email)
            }
            startActivity(intent)
        }

        btnVerClasesAsignadas.setOnClickListener(){
            val intent = Intent(this, AssignedClassesActivity::class.java).apply {
                putExtra("adminEmail", user.email)
                putExtra("clasesAsignadas", ArrayList(user.clasesAsignadas))
            }
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
        Toast.makeText(baseContext,"Sesion Cerrada.", Toast.LENGTH_SHORT).show()
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
    }

}