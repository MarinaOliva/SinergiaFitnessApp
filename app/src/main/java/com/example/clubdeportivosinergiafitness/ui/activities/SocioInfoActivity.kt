package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper

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

        val numDoc = intent.getIntExtra("nro_doc", -1)

        if (numDoc != -1) {
            cargarDatosDelSocio(numDoc)
        } else {
            tvDatosSocio.text = "Error: documento inválido"
        }

        btnVerCarnet.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarDatosDelSocio(numDoc: Int) {
        val dbHelper = ClubDBHelper(this)
        val db = dbHelper.readableDatabase

        val query = """
            SELECT c.nombre, c.apellido, c.tipoDoc, c.numDoc, s.socioID
            FROM Cliente c
            JOIN Socio s ON c.clienteID = s.clienteID
            WHERE c.numDoc = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(numDoc.toString()))

        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(0)
            val apellido = cursor.getString(1)
            val tipoDoc = cursor.getString(2)
            val nroDoc = cursor.getInt(3)
            val socioID = cursor.getInt(4)

            tvDatosSocio.text = "$nombre $apellido - $tipoDoc $nroDoc"

            // Estado de cuota
            val cuotaCursor = db.rawQuery(
                "SELECT fechaPago FROM Cuota WHERE socioID = ? ORDER BY fechaVencimiento DESC LIMIT 1",
                arrayOf(socioID.toString())
            )

            if (cuotaCursor.moveToFirst()) {
                val fechaPago = cuotaCursor.getString(0)
                tvEstadoCuota.text = if (fechaPago != null) {
                    "Última cuota: PAGADA"
                } else {
                    "Última cuota: VENCIDA"
                }
            } else {
                tvEstadoCuota.text = "No tiene cuotas registradas"
            }
            cuotaCursor.close()

            // Actividades
            val actividadesCursor = db.rawQuery(
                """
                SELECT a.nombreActividad
                FROM Actividad a
                JOIN SocioActividad sa ON a.idActividad = sa.actividadID
                WHERE sa.socioID = ?
                """.trimIndent(),
                arrayOf(socioID.toString())
            )

            val actividades = mutableListOf<String>()
            while (actividadesCursor.moveToNext()) {
                actividades.add(actividadesCursor.getString(0))
            }
            actividadesCursor.close()

            if (actividades.isNotEmpty()) {
                tvActividades.text = actividades.joinToString("\n") { "• $it" }
            } else {
                tvActividades.text = "No está inscripto a actividades"
            }

        } else {
            tvDatosSocio.text = "No se encontró un socio con ese documento"
            tvEstadoCuota.text = ""
            tvActividades.text = ""
        }

        cursor.close()
        db.close()
    }
}
