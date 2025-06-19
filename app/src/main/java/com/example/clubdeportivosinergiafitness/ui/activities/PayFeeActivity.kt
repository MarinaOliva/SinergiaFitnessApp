package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper
import com.example.clubdeportivosinergiafitness.databinding.ActivityPayFeeBinding

class PayFeeActivity : BaseActivity() {

    private lateinit var binding: ActivityPayFeeBinding
    private lateinit var dbHelper: ClubDBHelper

    private lateinit var nombreCompleto: String
    private var socioID: Int = -1
    private var importe: Double = 0.0
    private lateinit var fechaPago: String
    private lateinit var fechaVencimiento: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayFeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ClubDBHelper(this)

        // Obtener los datos del intent
        nombreCompleto = intent.getStringExtra("nombreCompleto") ?: ""
        socioID = intent.getIntExtra("socioID", -1)
        importe = intent.getDoubleExtra("importe", 0.0)
        fechaPago = intent.getStringExtra("fechaPago") ?: ""
        fechaVencimiento = intent.getStringExtra("fechaVencimiento") ?: ""

        if (socioID == -1 || nombreCompleto.isBlank()) {
            Toast.makeText(this, "Datos del socio no recibidos", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Mostrar los datos en pantalla
        binding.nombreTextView.text = "Nombre: $nombreCompleto"
        binding.numeroTextView.text = "Número: $socioID"
        binding.importeTextView.text = "Importe cuota: $%.2f".format(importe)
        binding.fechaPagoTextView.text = "Fecha de pago: $fechaPago"
        binding.fechaVencimientoTextView.text = "Fecha de vencimiento: $fechaVencimiento"

        // Botón salir
        binding.btnSalir.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        }

        // Botón pagar
        binding.btnPagar.setOnClickListener {
            val exito = dbHelper.registrarPagoCuota(socioID, importe, fechaVencimiento)
            if (exito) {
                val numeroRecibo = "REC-${System.currentTimeMillis()}"
                val intent = Intent(this, PrintReceiptActivity::class.java).apply {
                    putExtra("nombre", nombreCompleto)
                    putExtra("dni", socioID.toString()) // Lo adaptamos como "dni" aunque sea ID
                    putExtra("monto", importe)
                    putExtra("fechaPago", fechaPago)
                    putExtra("numeroRecibo", numeroRecibo)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
