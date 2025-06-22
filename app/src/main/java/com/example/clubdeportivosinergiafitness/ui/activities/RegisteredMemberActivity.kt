package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.databinding.ActivityRegisteredMemberBinding
import com.example.clubdeportivosinergiafitness.util.PdfUtils

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
            // Ocultar botones para que no salgan en el PDF
            binding.btnImprimir.visibility = View.GONE
            binding.btnSalir.visibility = View.GONE

            // Generar y guardar PDF
            val file = PdfUtils.guardarVistaComoPDF(
                context = this,
                view = binding.root, // Asumo que el layout raíz es root, cambiá si es distinto
                nombreArchivo = "socio_${System.currentTimeMillis()}"
            )

            // Volver a mostrar botones
            binding.btnImprimir.visibility = View.VISIBLE
            binding.btnSalir.visibility = View.VISIBLE

            Toast.makeText(
                this,
                "PDF guardado en:\n${file.absolutePath}",
                Toast.LENGTH_LONG
            ).show()

            // Abrir PDF
            PdfUtils.abrirPDF(this, file)
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
