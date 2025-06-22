package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper
import com.example.clubdeportivosinergiafitness.databinding.ActivityPayFeeBinding
import java.text.SimpleDateFormat
import java.util.*

class PayFeeActivity : BaseActivity() {

    private lateinit var binding: ActivityPayFeeBinding
    private lateinit var dbHelper: ClubDBHelper

    private lateinit var nombreCompleto: String
    private var socioID: Int = -1
    private var importe: Double = 0.0
    private lateinit var fechaPago: String
    private lateinit var fechaVencimiento: String

    private val recargo = 5000.0

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

        // Verificación mínima de datos recibidos
        if (socioID == -1 || nombreCompleto.isBlank()) {
            Toast.makeText(this, "Datos del socio no recibidos", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Determinar si la cuota está vencida
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val hoy = Calendar.getInstance().time
        val fechaVen = try {
            formatoFecha.parse(fechaVencimiento)
        } catch (e: Exception) {
            null
        }

        val vencida = fechaVen?.before(hoy) ?: false

        // Mostrar los datos en pantalla con leyenda si está vencida
        val leyendaImporte = if (vencida) {
            "Importe cuota: $%.2f (+ $5000 por pago vencido)".format(importe)
        } else {
            "Importe cuota: $%.2f".format(importe)
        }

        binding.nombreTextView.text = "Nombre: $nombreCompleto"
        binding.numeroTextView.text = "Número: $socioID"
        binding.importeTextView.text = leyendaImporte
        binding.fechaPagoTextView.text = "Fecha de pago: $fechaPago"
        binding.fechaVencimientoTextView.text = "Fecha de vencimiento: $fechaVencimiento"

        // Botón salir
        binding.btnSalir.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        }

        // Botón pagar
        binding.btnPagar.setOnClickListener {
            val socioDatos = dbHelper.obtenerDatosSocioPorID(socioID)
            val dniReal = socioDatos?.numDoc?.toString() ?: "Sin DNI"

            // Ajustar importe si está vencida
            val importeFinal = if (vencida) importe + recargo else importe

            val exito = dbHelper.registrarPagoCuota(socioID, importeFinal, fechaVencimiento)
            if (exito) {
                val numeroRecibo = "REC-${System.currentTimeMillis()}"
                val intent = Intent(this, PrintReceiptActivity::class.java).apply {
                    putExtra("nombre", nombreCompleto)
                    putExtra("dni", dniReal) // DNI real
                    putExtra("monto", importeFinal)
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
