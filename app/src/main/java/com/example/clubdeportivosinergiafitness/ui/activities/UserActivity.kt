package com.example.clubdeportivosinergiafitness.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper
import com.example.clubdeportivosinergiafitness.data.SessionManager

class UserActivity : BaseActivity() {

    private lateinit var dbHelper: ClubDBHelper
    private lateinit var sessionManager: SessionManager

    private lateinit var etNombreApellido: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassActual: EditText
    private lateinit var etNewPass: EditText
    private lateinit var etRepPass: EditText
    private lateinit var btnCambiar: Button

    private var nombreUsuario: String = ""
    private var emailUsuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        dbHelper = ClubDBHelper(this)
        sessionManager = SessionManager(this)

        etNombreApellido = findViewById(R.id.editTextText)
        etEmail = findViewById(R.id.et_mail)
        etPassActual = findViewById(R.id.textView3)
        etNewPass = findViewById(R.id.et_NewPass)
        etRepPass = findViewById(R.id.et_PassConfirmation)
        btnCambiar = findViewById(R.id.btn_Cambiar)

        // Intent puede no traer datos, por eso primero intento obtenerlos del Intent
        nombreUsuario = intent.getStringExtra("usuario") ?: sessionManager.getUsername() ?: ""
        emailUsuario = intent.getStringExtra("email") ?: sessionManager.getEmail() ?: ""

        cargarDatosAdmin()

        btnCambiar.setOnClickListener {
            cambiarContrasena()
        }
    }

    private fun cargarDatosAdmin() {
        val admin = dbHelper.obtenerDatosAdmin(nombreUsuario)
        if (admin != null) {
            etNombreApellido.setText(admin.nombreUsu.replace("_", " "))
            etEmail.setText(admin.email ?: emailUsuario) // si no hay email en db usa el recibido o guardado
            etPassActual.setText("") // siempre vacío por seguridad
        } else {
            Toast.makeText(this, "Error al cargar datos del admin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cambiarContrasena() {
        val passActualIngresada = etPassActual.text.toString()
        val nuevaPass = etNewPass.text.toString()
        val repPass = etRepPass.text.toString()

        if (passActualIngresada.isBlank() || nuevaPass.isBlank() || repPass.isBlank()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val admin = dbHelper.obtenerDatosAdmin(nombreUsuario)
        if (admin == null) {
            Toast.makeText(this, "Error interno", Toast.LENGTH_SHORT).show()
            return
        }

        if (passActualIngresada != admin.passUsu) {
            Toast.makeText(this, "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show()
            return
        }

        if (nuevaPass != repPass) {
            Toast.makeText(this, "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = dbHelper.actualizarPassAdmin(nombreUsuario, nuevaPass)
        if (exito) {
            Toast.makeText(this, "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show()
            etPassActual.setText("")
            etNewPass.setText("")
            etRepPass.setText("")
        } else {
            Toast.makeText(this, "Error al cambiar contraseña", Toast.LENGTH_SHORT).show()
        }
    }
}
