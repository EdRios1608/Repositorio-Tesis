package com.example.ex3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class AdminImageActivity : AppCompatActivity() {

    private lateinit var adminEmail: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_image)

        val db = FirebaseFirestore.getInstance()
        adminEmail = intent.getStringExtra("adminEmail") ?: ""


        val btnVerImagenAdmin1 = findViewById<Button>(R.id.btnVerImagenAdmin1)
        val btnVerImagenAdmin2 = findViewById<Button>(R.id.btnVerImagenAdmin2)
        val btnVerImagenAdmin3 = findViewById<Button>(R.id.btnVerImagenAdmin3)

        val imageViewHorarioAdmin = findViewById<ImageView>(R.id.imageViewHorarioAdmin)


        btnVerImagenAdmin1.setOnClickListener {
            db.collection("adminImages").document("images")
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val imageUrl = document.getString("horario1")
                        imageUrl?.let { loadImageIntoView(it, imageViewHorarioAdmin) }
                    }
                }
        }

        btnVerImagenAdmin2.setOnClickListener {
            db.collection("adminImages").document("images")
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val imageUrl = document.getString("horario2")
                        imageUrl?.let { loadImageIntoView(it, imageViewHorarioAdmin) }
                    }
                }
        }


        btnVerImagenAdmin3.setOnClickListener {
            db.collection("usuarios").document(adminEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists() && document.contains("imageUri")) {
                        val imageUrl = document.getString("imageUri")
                        imageUrl?.let {
                            loadImageIntoView(it, imageViewHorarioAdmin)
                        } ?: run {
                            Toast.makeText(this, "No se encontró la imagen personal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Aun no asignan el horario personal", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al cargar la imagen: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
        }


    }

    private fun loadImageIntoView(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .into(imageView)
    }

}