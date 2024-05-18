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

// Clase principal para la actividad de administración de datos
class AdminDataActivity : AppCompatActivity() {

    // Declaración de botones y vistas de imagen
    private lateinit var btnSeleccionarImagenPersonal: Button
    private lateinit var btnSubirImagenPersonal: Button
    private lateinit var imageViewHorarioPersonal: ImageView

    // Variable para almacenar la URI de la imagen seleccionada
    private var imageUriPersonal: Uri? = null
    private lateinit var adminEmail: String

    // Método que se llama cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_data)

        // Obtener el correo electrónico del administrador desde el Intent
        adminEmail = intent.getStringExtra("adminEmail") ?: ""

        // Inicialización de los elementos de la interfaz de usuario
        btnSeleccionarImagenPersonal = findViewById(R.id.btnSeleccionarImagenPersonal)
        btnSubirImagenPersonal = findViewById(R.id.btnSubirImagenPersonal)
        imageViewHorarioPersonal = findViewById(R.id.imageViewHorarioPersonal)

        // Establecer el listener para el botón de seleccionar imagen
        btnSeleccionarImagenPersonal.setOnClickListener {
            selectImage()
        }

        // Establecer el listener para el botón de subir imagen
        btnSubirImagenPersonal.setOnClickListener {
            imageUriPersonal?.let { uri ->
                uploadImageAndUpdateFirestore(uri)
            }
        }
    }

    // Método para seleccionar una imagen desde la galería
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    // Método que se llama cuando se recibe el resultado de una actividad
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Asignar la URI de la imagen seleccionada y mostrarla en la ImageView
            imageUriPersonal = data.data
            imageViewHorarioPersonal.setImageURI(imageUriPersonal)
        }
    }

    // Método para subir la imagen a Firebase Storage y actualizar Firestore con la URI de la imagen
    private fun uploadImageAndUpdateFirestore(fileUri: Uri) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Subiendo Imagen...")
        progressDialog.show()

        // Generar un nombre único para la imagen
        val fileName = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/admins_images/$fileName")

        // Subir el archivo a Firebase Storage
        storageRef.putFile(fileUri)
            .addOnSuccessListener {
                progressDialog.dismiss()
                // Obtener la URL de descarga y actualizar Firestore
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateAdminImageUri(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error al subir la imagen: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    // Método para actualizar la URI de la imagen en el perfil del administrador en Firestore
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
