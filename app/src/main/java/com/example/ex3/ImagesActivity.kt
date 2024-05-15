package com.example.ex3

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.Exception
import java.util.UUID

class ImagesActivity : AppCompatActivity() {

    lateinit var btnSeleccionarImagen1 : Button
    lateinit var btnSubirImagen1 : Button

    lateinit var btnSeleccionarImagen2 : Button
    lateinit var btnSubirImagen2 : Button

    lateinit var imageViewHorario : ImageView

    var imageUri1 : Uri? = null
    var imageUri2 : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        btnSeleccionarImagen1 = findViewById(R.id.btnSeleccionarImagen1)
        btnSubirImagen1 = findViewById(R.id.btnSubirImagen1)

        btnSeleccionarImagen2 = findViewById(R.id.btnSeleccionarImagen2)
        btnSubirImagen2 = findViewById(R.id.btnSubirImagen2)


        imageViewHorario = findViewById(R.id.imageViewHorario)

        btnSeleccionarImagen1.setOnClickListener {
            selectImage(1)
        }

        btnSubirImagen1.setOnClickListener {
            imageUri1?.let { uri -> uploadImage(uri, "horario1") }
        }


        btnSeleccionarImagen2.setOnClickListener {
            selectImage(2)
        }

        btnSubirImagen2.setOnClickListener {
            imageUri2?.let { uri -> uploadImage(uri, "horario2") }
        }



    }



    private fun selectImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            when (requestCode) {
                1 -> {
                    imageUri1 = data.data
                    imageViewHorario.setImageURI(imageUri1)
                }
                2 -> {
                    imageUri2 = data.data
                    imageViewHorario.setImageURI(imageUri2)
                }
            }
        }
    }

    private fun uploadImage(fileUri: Uri, imageKey: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Subiendo $imageKey...")
        progressDialog.show()

        val fileName = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/images/$fileName")

        storageRef.putFile(fileUri)
            .addOnSuccessListener {
                progressDialog.dismiss()
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUrlToFirestore(uri.toString(), imageKey)
                    Toast.makeText(this, "$imageKey subido correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error al subir $imageKey: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageUrlToFirestore(imageUrl: String, imageKey: String) {
        val db = FirebaseFirestore.getInstance()
        val imageInfo = hashMapOf(imageKey to imageUrl)
        db.collection("adminImages").document("images").set(imageInfo, SetOptions.merge())
    }




}