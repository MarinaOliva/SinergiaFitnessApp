package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R

class EnrollActivitiesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroll_activities)

        val btnCancelar = findViewById<Button>(R.id.btn_Cancelar)
        val btnInscribir = findViewById<Button>(R.id.btn_Inscribir)

        btnCancelar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnInscribir.setOnClickListener {
            Toast.makeText(this, "Inscripción exitosa", Toast.LENGTH_SHORT).show()
        }
    }
}
