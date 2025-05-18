package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.data.FakeSocioData
import com.example.clubdeportivosinergiafitness.databinding.ActivityPayFeeBinding
import com.example.clubdeportivosinergiafitness.data.Recibo
import com.example.clubdeportivosinergiafitness.ui.activities.MenuPrincipalActivity

class PayFeeActivity : BaseActivity() {

    private lateinit var binding: ActivityPayFeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el binding
        binding = ActivityPayFeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numeroSocio = intent.getIntExtra("numero_socio", -1)
        if (numeroSocio == -1) {
            Toast.makeText(this, "Número de socio no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val socio = FakeSocioData.obtenerSocioPorNumero(numeroSocio)
        if (socio == null) {
            Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Setear datos en la vista
        binding.nombreTextView.text = "Nombre: ${socio.nombre}"
        binding.numeroTextView.text = "Número: ${socio.numero}"
        binding.importeTextView.text = "Importe cuota: \$${socio.cuotaImporte}"
        binding.fechaVencimientoTextView.text = "Fecha de vencimiento: ${socio.fechaVencimiento}"

        binding.fechaPagoTextView.text = if (socio.fechaPago != null) {
            "Fecha de pago: ${socio.fechaPago}"
        } else {
            "No paga"
        }

        // Acción del botón "Salir"
        binding.btnSalir.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnPagar.setOnClickListener {
            // Crear datos simulados del recibo
            val recibo = Recibo(
                nombre = socio.nombre,
                dni = socio.dni,
                monto = socio.cuotaImporte,
                fechaPago = "2025-05-18", // simulamos una fecha fija
                numeroRecibo = "REC-${System.currentTimeMillis()}" // número único simulado
            )

            // Crear Intent para abrir PrintReceiptActivity y pasar datos
            val intent = Intent(this, PrintReceiptActivity::class.java).apply {
                putExtra("nombre", recibo.nombre)
                putExtra("dni", recibo.dni)
                putExtra("monto", recibo.monto)
                putExtra("fechaPago", recibo.fechaPago)
                putExtra("numeroRecibo", recibo.numeroRecibo)
            }
            startActivity(intent)
        }

    }
}
