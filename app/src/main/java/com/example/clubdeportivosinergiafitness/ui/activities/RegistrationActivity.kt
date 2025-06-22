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

        // Vinculamos vistas
        btnElegirActividad = findViewById(R.id.btnElegirActividad)
        btnAsociar = findViewById(R.id.btnAsociar)
        idSocioTextView = findViewById(R.id.IdSocio)

        // Obtenemos el DNI y el estado de socio
        val dni = intent.getStringExtra("dni_ingresado")
        val esSocio = intent.getBooleanExtra("es_socio", false)

        // Mostramos el resultado
        idSocioTextView.text = if (esSocio) {
            "El n° de documento ingresado pertenece a un: socio"
        } else {
            "El n° de documento ingresado pertenece a un: no socio"
        }

        // Acción para botón Elegir Actividad
        btnElegirActividad.setOnClickListener {
            val intent = Intent(this, NonMemberActivity::class.java)
            intent.putExtra("dni_ingresado", dni)
            startActivity(intent)
        }

        // Acción para botón Asociar
        btnAsociar.setOnClickListener {
            val intent = Intent(this, RegisterMemberActivity::class.java)
            intent.putExtra("dni_ingresado", dni)
            startActivity(intent)
        }
    }
}
