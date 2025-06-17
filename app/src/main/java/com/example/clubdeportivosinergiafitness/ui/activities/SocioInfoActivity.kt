package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R

class SocioInfoActivity : BaseActivity() {

    private lateinit var tvDatosSocio: TextView
    private lateinit var tvEstadoCuota: TextView
    private lateinit var tvActividades: TextView
    private lateinit var btnVerCarnet: Button
    private lateinit var btnSalir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socio_info)

        tvDatosSocio = findViewById(R.id.tvDatosSocio)
        tvEstadoCuota = findViewById(R.id.tvEstadoCuota)
        tvActividades = findViewById(R.id.tvActividades)
        btnVerCarnet = findViewById(R.id.btnVerCarnet)
        btnSalir = findViewById(R.id.btnSalir)

        // Simulamos datos recibidos
        val nombre = intent.getStringExtra("nombre") ?: "Nombre"
        val apellido = intent.getStringExtra("apellido") ?: "Apellido"
        val tipoDoc = intent.getStringExtra("tipo_doc") ?: "DNI"
        val numeroDoc = intent.getStringExtra("nro_doc") ?: "12345678"
        val cuotaPagada = intent.getBooleanExtra("cuota_pagada", true)
        val actividades = intent.getStringArrayListExtra("actividades") ?: arrayListOf("Fútbol", "Yoga")

        tvDatosSocio.text = "$nombre $apellido - $tipoDoc $numeroDoc"
        tvEstadoCuota.text = "Última cuota: ${if (cuotaPagada) "PAGADA" else "VENCIDA"}"
        tvActividades.text = actividades.joinToString(separator = "\n") { "• $it" }

        btnVerCarnet.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            finish()
        }
    }
}
