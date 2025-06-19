package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper
import com.example.clubdeportivosinergiafitness.data.SocioDatos

class SocioInfoActivity : BaseActivity() {

    private lateinit var tvDatosSocio: TextView
    private lateinit var tvNumeroSocio: TextView
    private lateinit var tvEstadoCuota: TextView
    private lateinit var tvActividades: TextView
    private lateinit var btnVerCarnet: Button
    private lateinit var btnSalir: Button
    private lateinit var dbHelper: ClubDBHelper

    private var socioActual: SocioDatos? = null // variable para guardar el socio cargado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socio_info)

        // Vinculación con la vista
        tvDatosSocio = findViewById(R.id.tvDatosSocio)
        tvNumeroSocio = findViewById(R.id.tvNumeroSocio)
        tvEstadoCuota = findViewById(R.id.tvEstadoCuota)
        tvActividades = findViewById(R.id.tvActividades)
        btnVerCarnet = findViewById(R.id.btnVerCarnet)
        btnSalir = findViewById(R.id.btnSalir)

        dbHelper = ClubDBHelper(this)

        val numDoc = intent.getIntExtra("nro_doc", -1)
        if (numDoc != -1) {
            cargarDatosDelSocio(numDoc)
        } else {
            tvDatosSocio.text = "Error: documento inválido"
        }

        btnVerCarnet.setOnClickListener {
            socioActual?.let { socio ->
                val intent = Intent(this, RegisteredMemberActivity::class.java).apply {
                    putExtra("apellido", socio.apellido)
                    putExtra("nombre", socio.nombre)
                    putExtra("dni", socio.numDoc.toString())
                    putExtra("numeroSocio", socio.socioID.toString())
                }
                startActivity(intent)
            }
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarDatosDelSocio(numDoc: Int) {
        val socio = dbHelper.obtenerDatosSocioPorDocumento(numDoc)
        socioActual = socio

        if (socio != null) {
            tvNumeroSocio.text = "N° de socio: ${socio.socioID}"
            tvDatosSocio.text = "${socio.nombre} ${socio.apellido} - ${socio.tipoDoc} ${socio.numDoc}"
            tvEstadoCuota.text = dbHelper.obtenerEstadoUltimaCuota(socio.socioID)

            val actividades = dbHelper.obtenerActividadesDelSocio(socio.socioID)
            tvActividades.text = if (actividades.isNotEmpty()) {
                actividades.joinToString("\n") { "• $it" }
            } else {
                "No está inscripto a actividades"
            }
        } else {
            tvDatosSocio.text = "No se encontró un socio con ese documento"
            tvNumeroSocio.text = ""
            tvEstadoCuota.text = ""
            tvActividades.text = ""
        }
    }
}
