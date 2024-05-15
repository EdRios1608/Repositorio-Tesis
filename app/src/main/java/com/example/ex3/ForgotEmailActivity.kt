package com.example.ex3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.*

class ForgotEmailActivity : AppCompatActivity() {
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_email)
        firebaseAuth = Firebase.auth

        val txtEmailRecovery : EditText = findViewById(R.id.edtEmailRecuperation)
        val btnEnviarCambio : Button = findViewById(R.id.btnEnviarRecuperacionPass)

        btnEnviarCambio.setOnClickListener(){
            var email = txtEmailRecovery.text.toString()
            if(email.isNotEmpty()){
                sendPasswordReset(email)
            } else {
                Toast.makeText(baseContext,"Campo vacio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendPasswordReset(email: String){
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(){task ->
                if(task.isSuccessful){
                    Toast.makeText(baseContext,"Correo Enviado. Revisa tu bandeja.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext,"Correo No Registrado, Ingresa nuevamente", Toast.LENGTH_SHORT).show()
                }

            }
    }
}