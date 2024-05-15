package com.example.ex3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.ex3.R.id.btnRegistrarUsuario
import com.example.ex3.R.layout.activity_register
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class RegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var clubUsuario: String
    private lateinit var db: FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_register)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = Firebase.auth

        //Link entre lo visual mediante el nombre
        val edtNombreRegister = findViewById<EditText>(R.id.edtNombreRegister)
        val edtApellidoRegister = findViewById<EditText>(R.id.edtApellidoRegister)
        val edtEmailRegister = findViewById<EditText>(R.id.edtEmailRegister)
        val edtPasswordRegister1 = findViewById<EditText>(R.id.edtPasswordRegister1)
        val edtPasswordRegister2 = findViewById<EditText>(R.id.edtPasswordRegister2)
        val edtTelefonoRegister = findViewById<EditText>(R.id.edtTelefonoRegister)
        val edtCedulaRegister = findViewById<EditText>(R.id.edtCedulaRegister)

        val spinnerId = findViewById<Spinner>(R.id.spinnerRegister)
        val clubes = arrayOf("Arrayanes", "Cumbaya") //Arreglo de clubes
        val btnRegistrarUsuario = findViewById<Button>(btnRegistrarUsuario)


        //Funcion Spinner Cubes
        val arrayAdp = ArrayAdapter(this@RegisterActivity, android.R.layout.simple_spinner_dropdown_item, clubes)
        spinnerId.adapter = arrayAdp
        spinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                clubUsuario = parent?.getItemAtPosition(position).toString()
                //Estas dos lineas puedo comentar
                //Toast.makeText(this@RegisterActivity, "Seleccionado: $clubUsuario", Toast.LENGTH_SHORT).show()
                //Log.d("SpinnerSelection", clubUsuario)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        btnRegistrarUsuario.setOnClickListener() {
            val user = User(
                //Inicializar las variables
                nombre = edtNombreRegister.text.toString(),
                apellido = edtApellidoRegister.text.toString(),
                email = edtEmailRegister.text.toString(),
                password1 = edtPasswordRegister1.text.toString(),
                password2 = edtPasswordRegister2.text.toString(),
                telefono = edtTelefonoRegister.text.toString(),
                cedula = edtCedulaRegister.text.toString(),
                club = clubUsuario,
                rango = 0
            )


            //Funcion para verificar que los campos esten completos
            val emptyFields = validateEmptyFields(user.nombre, user.apellido, user.email, user.password1, user.password2, user.telefono, user.cedula)

            //Funcion crear cuenta
            createAccount(user,emptyFields)
        }
    }

    //Funcion para validar si los campos estan llenos
    private fun validateEmptyFields(nombreUsuario: String, apellidoUsuario: String, emailUsuario: String, pass1: String, pass2: String, telefonoUsuario: String, cedulaUsuario: String): Boolean {
        return nombreUsuario.isNotEmpty() && apellidoUsuario.isNotEmpty() && emailUsuario.isNotEmpty() && pass1.isNotEmpty() && pass2.isNotEmpty() && telefonoUsuario.isNotEmpty() && cedulaUsuario.isNotEmpty()
    }


    private fun createAccount(user:User, emptyFields: Boolean) {

        if ((user.password1 == user.password2) && emptyFields) {
            val userInfo = user.toMap()

            //Agregar nuevo usuario a la base de datos al path reference "usuarios" nombre de la base de datos
            db.collection("usuarios")
                .document(user.email)
                .set(userInfo)
                .addOnSuccessListener {
                    Toast.makeText(baseContext, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show()
                } .addOnFailureListener { exception ->
                    Toast.makeText(baseContext, "Error al agregar usuario: ${exception.message}", Toast.LENGTH_LONG).show()
                }

            guardarEmailPasswordFirebase(user.email, user.password1)

        } else if (!emptyFields) {
            Toast.makeText(baseContext, "Alguno de los campos esta vacio", Toast.LENGTH_SHORT).show()
        } else  {
            Toast.makeText(baseContext, "Error:Password no coinciden", Toast.LENGTH_SHORT).show()
        }
    }

    //Funcion para guardar Email y Contrasena como metodo de ingreso.
    private fun guardarEmailPasswordFirebase(emailUsuario: String, pass1: String) {
        firebaseAuth.createUserWithEmailAndPassword(emailUsuario, pass1)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Usar log para guardar? y no hacer tantos toast?
                    Toast.makeText(baseContext, "Cuenta creada correctamente!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Cuenta no creada, error" + task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }

}