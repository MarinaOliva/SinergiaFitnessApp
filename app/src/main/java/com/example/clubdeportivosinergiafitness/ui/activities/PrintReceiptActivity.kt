package com.example.clubdeportivosinergiafitness.ui.activities

import android.os.Bundle
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.databinding.ActivityPrintReceiptBinding


class PrintReceiptActivity : BaseActivity() {

    private lateinit var binding: ActivityPrintReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrintReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del Intent
        val nombre = intent.getStringExtra("nombre") ?: "Desconocido"
        val dni = intent.getStringExtra("dni") ?: "Sin DNI"
        val monto = intent.getDoubleExtra("monto", 0.0)
        val fechaPago = intent.getStringExtra("fechaPago") ?: "Sin fecha"
        val numeroRecibo = intent.getStringExtra("numeroRecibo") ?: "Sin número"

        // Asignar los datos a los TextViews
        binding.tvNombre.text = "Nombre: $nombre"
        binding.tvDni.text = "DNI: $dni"
        binding.tvMonto.text = "Monto: \$${String.format("%.2f", monto)}"
        binding.tvFechaPago.text = "Fecha de pago: $fechaPago"
        binding.tvNumeroRecibo.text = "Número de recibo: $numeroRecibo"

        // Botón de imprimir
        // TODO: Implementar la lógica para imprimir el recibo o guardar imagen
        binding.btnImprimir.setOnClickListener {
            Toast.makeText(this, "Funcionalidad próximamente", Toast.LENGTH_SHORT).show()
        }
    }
}
