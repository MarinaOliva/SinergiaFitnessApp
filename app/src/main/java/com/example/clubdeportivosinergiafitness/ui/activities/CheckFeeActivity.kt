package com.example.clubdeportivosinergiafitness.ui.activities

import android.os.Bundle
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent

class CheckFeeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_fee)

        val numeroSocioEditText = findViewById<EditText>(R.id.numeroSocioEditText)
        val consultarButton = findViewById<Button>(R.id.loginButton)

        consultarButton.setOnClickListener {
            val numeroStr = numeroSocioEditText.text.toString()
            if (numeroStr.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese un número de socio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val numero = numeroStr.toIntOrNull()
            if (numero == null) {
                Toast.makeText(this, "Número de socio inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, PayFeeActivity::class.java)
            intent.putExtra("numero_socio", numero)
            startActivity(intent)
        }
    }
}
