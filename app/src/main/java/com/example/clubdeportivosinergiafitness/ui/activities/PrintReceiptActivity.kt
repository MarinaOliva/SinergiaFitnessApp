package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.databinding.ActivityPrintReceiptBinding
import com.example.clubdeportivosinergiafitness.util.PdfUtils
import android.view.View

class PrintReceiptActivity : BaseActivity() {

    private lateinit var binding: ActivityPrintReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrintReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del intent
        val nombre = intent.getStringExtra("nombre") ?: "Desconocido"
        val dni = intent.getStringExtra("dni") ?: "Sin DNI"
        val monto = intent.getDoubleExtra("monto", 0.0)
        val fechaPago = intent.getStringExtra("fechaPago") ?: "Sin fecha"
        val numeroRecibo = intent.getStringExtra("numeroRecibo") ?: "Sin número"
        val tieneRecargo = intent.getBooleanExtra("tieneRecargo", false)

        // Mostrar los datos en pantalla
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

        // Acción del botón "Imprimir"
        binding.btnImprimir.setOnClickListener {
            // Ocultar botones para que no se impriman
            binding.btnImprimir.visibility = View.GONE
            binding.btnSalir.visibility = View.GONE

            // Generar y guardar PDF
            val file = PdfUtils.guardarVistaComoPDF(
                context = this,
                view = binding.printReceiptLayout,
                nombreArchivo = "comprobante_${System.currentTimeMillis()}"
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

        // Acción del botón "Salir"
        binding.btnSalir.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
