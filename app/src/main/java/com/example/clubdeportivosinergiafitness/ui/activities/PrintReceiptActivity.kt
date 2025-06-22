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

        // Obtener datos
        val nombre = intent.getStringExtra("nombre") ?: "Desconocido"
        val dni = intent.getStringExtra("dni") ?: "Sin DNI"
        val monto = intent.getDoubleExtra("monto", 0.0)
        val fechaPago = intent.getStringExtra("fechaPago") ?: "Sin fecha"
        val numeroRecibo = intent.getStringExtra("numeroRecibo") ?: "Sin número"
        val tieneRecargo = intent.getBooleanExtra("tieneRecargo", false)

        // Mostrar datos
        binding.tvNombre.text = "Nombre: $nombre"
        binding.tvDni.text = "DNI: $dni"
        binding.tvFechaPago.text = "Fecha de pago: $fechaPago"
        binding.tvNumeroRecibo.text = "Número de recibo: $numeroRecibo"

        val textoMonto = if (tieneRecargo) {
            "Monto: \$%.2f (incluye $5000 de interés por atraso)".format(monto)
        } else {
            "Monto: \$%.2f".format(monto)
        }
        binding.tvMonto.text = textoMonto

        // Botón imprimir (placeholder)
        binding.btnImprimir.setOnClickListener {
            Toast.makeText(this, "Funcionalidad próximamente", Toast.LENGTH_SHORT).show()
        }
    }
}
