package com.example.clubdeportivosinergiafitness.ui.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R

class RegistrationActivity : BaseActivity() {

    private lateinit var idSocioTextView: TextView
    private lateinit var btnElegirActividad: Button
    private lateinit var btnAsociar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Vinculamos los botones y el TextView
        btnElegirActividad = findViewById(R.id.btnElegirActividad)
        btnAsociar = findViewById(R.id.btnAsociar)
        idSocioTextView = findViewById(R.id.IdSocio)

        // Acción para botón Elegir Actividad
        btnElegirActividad.setOnClickListener {
            val intent = Intent(this, NonMemberActivity::class.java)
            startActivity(intent)
        }

        // Acción para botón Asociar
        btnAsociar.setOnClickListener {
            val intent = Intent(this, RegisterMemberActivity::class.java)
            startActivity(intent)
        }

        // Obtenemos el DNI enviado desde la otra Activity
        val dni = intent.getStringExtra("dni_ingresado")

        // Verificamos que no sea null y simulamos la consulta
        if (!dni.isNullOrEmpty()) {
            simularConsultaSocio(dni)
        }
    }

    private fun simularConsultaSocio(dni: String) {
        val esSocio = dni == "12345678"
        val resultado = if (esSocio) "socio" else "no socio"
        idSocioTextView.text = "El n° de documento ingresado pertenece a un: $resultado"
    }
}