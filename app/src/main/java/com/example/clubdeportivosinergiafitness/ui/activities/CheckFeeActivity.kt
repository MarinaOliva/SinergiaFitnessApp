package com.example.clubdeportivosinergiafitness.ui.activities

import android.os.Bundle
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper

class CheckFeeActivity : BaseActivity() {

    private lateinit var editNumSocio: EditText
    private lateinit var btnConsultar: Button
    private lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_fee)

        editNumSocio = findViewById(R.id.numeroSocioEditText)
        btnConsultar = findViewById(R.id.consultarButton)
        dbHelper = ClubDBHelper(this)

        btnConsultar.setOnClickListener {
            val numSocioText = editNumSocio.text.toString()

            if (numSocioText.isBlank()) {
                Toast.makeText(this, "Ingresá un número de socio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val datos = dbHelper.obtenerDatosParaPago(numSocioText.toInt())

            if (datos != null) {
                val intent = Intent(this, PayFeeActivity::class.java).apply {
                    putExtra("nombreCompleto", datos.nombreCompleto)
                    putExtra("socioID", datos.socioID)
                    putExtra("importe", datos.importe)
                    putExtra("fechaPago", datos.fechaPago)
                    putExtra("fechaVencimiento", datos.fechaVencimiento)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Socio no encontrado o sin cuotas registradas", Toast.LENGTH_LONG).show()
            }
        }
    }
}
