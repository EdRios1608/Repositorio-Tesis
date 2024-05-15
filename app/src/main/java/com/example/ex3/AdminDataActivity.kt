package com.example.ex3

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AdminDataActivity : AppCompatActivity() {


    private lateinit var btnSeleccionarImagenPersonal: Button
    private lateinit var btnSubirImagenPersonal: Button

    private lateinit var imageViewHorarioPersonal: ImageView

    private var imageUriPersonal: Uri? = null
    private lateinit var adminEmail: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_data)

        adminEmail = intent.getStringExtra("adminEmail") ?: ""

        btnSeleccionarImagenPersonal = findViewById(R.id.btnSeleccionarImagenPersonal)
        btnSubirImagenPersonal = findViewById(R.id.btnSubirImagenPersonal)

        imageViewHorarioPersonal = findViewById(R.id.imageViewHorarioPersonal)


        btnSeleccionarImagenPersonal.setOnClickListener {
            selectImage()
        }

        btnSubirImagenPersonal.setOnClickListener {
            imageUriPersonal?.let { uri ->
                uploadImageAndUpdateFirestore(uri)
            }
        }

    }



    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUriPersonal = data.data
            imageViewHorarioPersonal.setImageURI(imageUriPersonal)
        }
    }

    private fun uploadImageAndUpdateFirestore(fileUri: Uri) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Subiendo Imagen...")
        progressDialog.show()

        val fileName = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/admins_images/$fileName")

        storageRef.putFile(fileUri)
            .addOnSuccessListener {
                progressDialog.dismiss()
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateAdminImageUri(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error al subir la imagen: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }


    private fun updateAdminImageUri(imageUri: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(adminEmail)
            .update("imageUri", imageUri)
            .addOnSuccessListener {
                Toast.makeText(this, "Imagen guardada en el perfil", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar la imagen en el perfil: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

}