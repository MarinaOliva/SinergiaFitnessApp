package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.databinding.ActivityRegisteredMemberBinding

class RegisteredMemberActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisteredMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisteredMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibimos datos del Intent
        val apellido = intent.getStringExtra("apellido") ?: ""
        val nombre = intent.getStringExtra("nombre") ?: ""
        val dni = intent.getStringExtra("dni") ?: ""
        val numeroSocio = intent.getStringExtra("numeroSocio") ?: ""

        // Seteamos los datos en los TextViews usando binding
        binding.tvApellido.text = "Apellido: $apellido"
        binding.tvNombre.text = "Nombre: $nombre"
        binding.tvDni.text = "DNI: $dni"
        binding.tvNumeroSocio.text = "Número de socio: $numeroSocio"

        // Botón de imprimir
        binding.btnImprimir.setOnClickListener {
            Toast.makeText(this, "Funcionalidad próximamente", Toast.LENGTH_SHORT).show()
        }

        // Botón de salir
        binding.btnSalir.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
