package com.example.ex3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

//Clase que sirve para crear distintas actividades del centro vacacional
class CreateClassesActivity : AppCompatActivity() {

    //Definicion de variables
    private lateinit var db: FirebaseFirestore
    private lateinit var spinnerNombreProfesor: Spinner
    private lateinit var adapterProfesores: ArrayAdapter<String>

    private var profesores = ArrayList<String>()
    private var profesorEmails = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_classes)
        db = FirebaseFirestore.getInstance()

        // Link de componentes UI con sus IDs respectivos
        spinnerNombreProfesor = findViewById(R.id.spinnerNombreProfesor)
        val edtNombreClase = findViewById<EditText>(R.id.edtNombreClase)
        val edtLugar = findViewById<EditText>(R.id.edtLugar)
        val edtCupos = findViewById<EditText>(R.id.edtCupos)
        val edtDia = findViewById<EditText>(R.id.edtDia)
        val edtHoraInicio = findViewById<EditText>(R.id.edtHoraInicio)
        val edtHoraFin = findViewById<EditText>(R.id.edtHoraFin)
        val edtEdadMinima = findViewById<EditText>(R.id.edtEdadMinima)
        val edtEdadMaxima = findViewById<EditText>(R.id.edtEdadMaxima)
        val btnCrearClase = findViewById<Button>(R.id.btnCrearClase)

        // Configuración del spinner para profesores
        adapterProfesores = ArrayAdapter(this, android.R.layout.simple_spinner_item, profesores)
        adapterProfesores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNombreProfesor.adapter = adapterProfesores

        // Cargar profesores en el Spinner
        loadProfesores()

        // Evento clic para el botón crear clase
        btnCrearClase.setOnClickListener {
            val nombreClase = edtNombreClase.text.toString()
            val nombreProfesor = spinnerNombreProfesor.selectedItem.toString()
            val lugar = edtLugar.text.toString()
            val cupos = edtCupos.text.toString().toIntOrNull()
            val dia = edtDia.text.toString()
            val horaInicio = edtHoraInicio.text.toString()
            val horaFin = edtHoraFin.text.toString()
            val edadMinima = edtEdadMinima.text.toString().toIntOrNull()
            val edadMaxima = edtEdadMaxima.text.toString().toIntOrNull()

            if (validarCampos(nombreClase, lugar, cupos, dia, horaInicio, horaFin, edadMinima, edadMaxima)) {
                val nuevaClase = Classes(
                    nombreClase = nombreClase,
                    nombreProfesor = nombreProfesor,
                    lugar = lugar,
                    cupos = cupos!!,
                    cuposRestantes = cupos,
                    dia = dia,
                    horaInicio = horaInicio,
                    horaFin = horaFin,
                    edadMinima = edadMinima!!,
                    edadMaxima = edadMaxima!!
                )
                createClass(nuevaClase)
            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos correctamente.", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Validacion de campos
    private fun validarCampos(nombreClase: String, lugar: String, cupos: Int?, dia: String, horaInicio: String, horaFin: String, edadMinima: Int?, edadMaxima: Int?): Boolean {
        return !(nombreClase.isEmpty() || lugar.isEmpty() || cupos == null || dia.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty() || edadMinima == null || edadMaxima == null)
    }


    //Funcion para crear la clase
    private fun createClass(nuevaClase: Classes) {
        val selectedPosition = spinnerNombreProfesor.selectedItemPosition
        val profesorEmail = profesorEmails[selectedPosition]  // Obtén el email del profesor seleccionado

        db.collection("clases").document(nuevaClase.nombreClase)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "Una clase con ese nombre ya existe.", Toast.LENGTH_SHORT).show()
                } else {
                    db.collection("clases").document(nuevaClase.nombreClase)
                        .set(nuevaClase.toMap())
                        .addOnSuccessListener {
                            // Actualizar el documento del profesor con la nueva clase asignada
                            updateProfesorClasses(profesorEmail, nuevaClase.nombreClase)
                            Toast.makeText(this, "Clase creada correctamente.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al crear la clase: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }

    //Funcion para agregar la clase al proefsor
    private fun updateProfesorClasses(profesorEmail: String, claseAsignada: String) {
        val userRef = db.collection("usuarios").document(profesorEmail)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentClasses = snapshot.get("clasesAsignadas") as? ArrayList<String> ?: arrayListOf()
            currentClasses.add(claseAsignada)
            transaction.update(userRef, "clasesAsignadas", currentClasses)
        }.addOnSuccessListener {
            Toast.makeText(this, "Clase asignada al perfil del profesor.", Toast.LENGTH_LONG).show()
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Error al asignar clase al profesor: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    //Funcion para mostrar los administradores
    private fun loadProfesores() {
        db.collection("usuarios").whereEqualTo("rango", 1)
            .get()
            .addOnSuccessListener { result ->
                profesores.clear()
                profesorEmails.clear()
                for (document in result) {
                    val nombreCompleto = "${document.getString("nombre")} ${document.getString("apellido")}"
                    val email = document.getString("email") ?: ""
                    profesores.add(nombreCompleto)
                    profesorEmails.add(email)
                }
                adapterProfesores.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar los profesores.", Toast.LENGTH_SHORT).show()
            }
    }
}
