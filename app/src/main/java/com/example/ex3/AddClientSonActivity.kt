package com.example.ex3

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

// Clase principal para la actividad de agregar un hijo al cliente
class AddClientSonActivity : AppCompatActivity() {
    // Declaración de la instancia de autenticación de Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    // Método que se llama cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client_son)

        // Inicialización de la instancia de autenticación de Firebase
        firebaseAuth = FirebaseAuth.getInstance()

        // Referencias a los elementos de la interfaz de usuario
        val edtNombreHijo = findViewById<EditText>(R.id.edtNombreHijo)
        val edtApellidoHijo = findViewById<EditText>(R.id.edtApellidoHijo)
        val edtFechaNacimiento = findViewById<EditText>(R.id.edtFechaNacimientoHijo)
        val edtEdad = findViewById<EditText>(R.id.edtEdadHijo)
        val btnGuardarHijos = findViewById<Button>(R.id.btnGuardarHijo)

        // Establecer el listener para mostrar el selector de fecha al hacer clic en el campo de fecha de nacimiento
        edtFechaNacimiento.setOnClickListener { showDatePickerDialog() }

        // Establecer el listener para el botón de guardar información del hijo
        btnGuardarHijos.setOnClickListener {
            // Obtener y convertir los datos ingresados por el usuario
            val nombreHijo = edtNombreHijo.text.toString()
            val apellidoHijo = edtApellidoHijo.text.toString()
            val fechaNacimiento = edtFechaNacimiento.text.toString()
            val edadStr = edtEdad.text.toString().filter { it.isDigit() }
            val edad = edadStr.toIntOrNull()

            // Validar que todos los campos estén completos y que la edad sea un número válido
            if (nombreHijo.isEmpty() || apellidoHijo.isEmpty() || fechaNacimiento.isEmpty() || edad == null) {
                Toast.makeText(this, "Todos los campos son obligatorios y la edad debe ser un número válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Asegúrate de retornar aquí para prevenir ejecución adicional si hay error
            }

            // Verificar si el usuario está autenticado
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamar al método para guardar la información del hijo en Firestore
            saveSonInfo(currentUser.email!!, Hijo(nombreHijo, apellidoHijo, fechaNacimiento, edad))
        }
    }

    // Mostrar el diálogo de selección de fecha
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    // Método que se llama cuando se selecciona una fecha en el diálogo de selección de fecha
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val selectedDate = "$day/${month + 1}/$year"
        findViewById<EditText>(R.id.edtFechaNacimientoHijo).setText(selectedDate)

        val edad = calculateAge(year, month, day)
        findViewById<EditText>(R.id.edtEdadHijo).setText("$edad años")
    }

    // Calcular la edad a partir de la fecha de nacimiento
    private fun calculateAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(year, month, day)
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        // Ajustar la edad si el día de nacimiento aún no ha pasado este año
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

    // Guardar la información del hijo en Firestore
    private fun saveSonInfo(userEmail: String, hijo: Hijo) {
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios")
            .document(userEmail)
            .collection("hijos")
            .document(hijo.nombre) // Especifica el ID del documento manualmente
            .set(hijo.toMap()) // Usa .set() para crear o sobrescribir el documento con ese ID
            .addOnSuccessListener {
                Toast.makeText(this, "Hijo agregado correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al agregar hijo: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
