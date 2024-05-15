package com.example.ex3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ex3.R.id.edtEmailLogin
import com.example.ex3.R.id.edtPasswordLogin
import com.example.ex3.R.id.btnLogin
import com.example.ex3.R.id.edtOlvideContrasena
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        val edtEmailLogin = findViewById<EditText>(edtEmailLogin)
        val edtPasswordLogin = findViewById<EditText>(edtPasswordLogin)
        val edtOlvideContrasena = findViewById<EditText>(edtOlvideContrasena)
        val btnLogin = findViewById<Button>(btnLogin)

        btnLogin.setOnClickListener(){
            val email = edtEmailLogin.text.toString()
            val password = edtPasswordLogin.text.toString()

            if((email.isNotEmpty()) && (password.isNotEmpty())){
                signIn(email,password)
            } else {
                Toast.makeText(baseContext,"Alguno de los campos esta vacio",Toast.LENGTH_SHORT).show()
            }
        }

        //Funcion olvide mi contrasena
        edtOlvideContrasena.setOnClickListener(){
            val intent = Intent(this, ForgotEmailActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    db.collection("usuarios").document(email).get().addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val user = document.toObject(User::class.java) ?: User() // Asegúrate de manejar un posible valor nulo aquí
                            when (user.rango) {
                                0 -> {

                                    val intent = Intent(this, ClientHomeActivity::class.java).apply {
                                        putExtra("userDetails", user)
                                    }
                                    startActivity(intent)

                                }
                                1 -> {

                                    val intent = Intent(this, AdminHomeActivity::class.java).apply {
                                        putExtra("userDetails", user)
                                    }
                                    startActivity(intent)

                                }
                                2 -> {

                                    val intent = Intent(this, SuperAdminHomeActivity::class.java).apply {
                                        putExtra("userDetails", user)
                                    }
                                    startActivity(intent)

                                }
                                else -> Toast.makeText(baseContext, "Rango no reconocido", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(baseContext, "Error: email/contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            }
    }

}