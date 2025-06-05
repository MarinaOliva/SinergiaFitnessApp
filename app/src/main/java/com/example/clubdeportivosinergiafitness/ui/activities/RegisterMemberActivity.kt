package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R

class RegisterMemberActivity : BaseActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etDni: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register_member)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etDni = findViewById(R.id.etNumeroDocumento)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val dni = etDni.text.toString().trim()

            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val numeroSocio = (1000..9999).random().toString()

            val intent = Intent(this, RegisteredMemberActivity::class.java)
            intent.putExtra("nombre", nombre)
            intent.putExtra("apellido", apellido)
            intent.putExtra("dni", dni)
            intent.putExtra("numeroSocio", numeroSocio)
            startActivity(intent)
        }

        btnCancelar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
