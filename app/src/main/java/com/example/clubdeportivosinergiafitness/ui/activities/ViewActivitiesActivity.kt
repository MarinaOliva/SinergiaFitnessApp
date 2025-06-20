package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper


class ViewActivitiesActivity : BaseActivity() {

    private lateinit var etNumSocio: EditText
    private lateinit var tvListaActividades: TextView
    private lateinit var btnConsultar: Button
    private lateinit var btnConfirmar: Button
    private lateinit var btnModificar: Button

    private lateinit var dbHelper: ClubDBHelper
    private var socioIDActual: Int = -1  // ID interno del socio en la DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_activities)

        // Referencias a vistas
        etNumSocio = findViewById(R.id.et_NumSocio)
        tvListaActividades = findViewById(R.id.tv_ListaActividades)
        btnConsultar = findViewById(R.id.btn_Consultar)
        btnConfirmar = findViewById(R.id.btn_ConfirmarMismas)
        btnModificar = findViewById(R.id.btn_Modificar)

        dbHelper = ClubDBHelper(this)

        btnConsultar.setOnClickListener {
            val numSocioTexto = etNumSocio.text.toString()

            if (numSocioTexto.isEmpty()) {
                Toast.makeText(this, "Ingrese el número de socio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val numSocio = numSocioTexto.toIntOrNull()
            if (numSocio == null) {
                Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener los datos del socio
            val socioDatos = dbHelper.obtenerDatosSocioPorID(numSocio)
            if (socioDatos == null) {
                tvListaActividades.text = ""
                Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            socioIDActual = socioDatos.socioID

            val actividades = dbHelper.obtenerActividadesDelSocio(socioIDActual)

            if (actividades.isEmpty()) {
                tvListaActividades.text = "Actividades de ${socioDatos.nombre} ${socioDatos.apellido}:\n(No tiene actividades asignadas este mes.)"
            } else {
                tvListaActividades.text = " ${socioDatos.nombre} ${socioDatos.apellido}:\n" + actividades.joinToString(separator = "\n") { "- $it" }
            }
        }


        btnConfirmar.setOnClickListener {
            if (socioIDActual != -1) {
                Toast.makeText(this, "Actividades confirmadas para este mes", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Primero consulte un socio válido", Toast.LENGTH_SHORT).show()
            }
        }

        btnModificar.setOnClickListener {
            if (socioIDActual != -1) {
                val intent = Intent(this, SelectActivitiesActivity::class.java)
                intent.putExtra("SOCIO_ID", socioIDActual)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Primero consulte un socio válido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
