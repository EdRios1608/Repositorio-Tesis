package com.example.ex3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ClasesAdministrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clases_administration)
        val btnCrearClasesActividad = findViewById<Button>(R.id.btnCrearClasesActividad)
        val btnVerClasesAdmin = findViewById<Button>(R.id.btnVerClasesAdmin)
        val btnEliminarClases = findViewById<Button>(R.id.btnEliminarClases)


        btnCrearClasesActividad.setOnClickListener(){
            val intent = Intent(this, CreateClassesActivity::class.java)
            startActivity(intent)
        }

        btnVerClasesAdmin.setOnClickListener(){
            val intent = Intent(this, ViewClassesActivity::class.java)
            startActivity(intent)
        }

        btnEliminarClases.setOnClickListener(){
            val intent = Intent(this, DeleteClassesActivity::class.java)
            startActivity(intent)
        }



    }
}