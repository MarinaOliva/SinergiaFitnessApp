package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.databinding.ActivityNonMemberBinding
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log

class NonMemberActivity : BaseActivity() {

    private lateinit var binding: ActivityNonMemberBinding
    private lateinit var dbHelper: ClubDBHelper
    private var idActividadSeleccionada: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNonMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ClubDBHelper(this)

        // Configurar los spinners personalizados
        setupCustomSpinner(binding.spinnerActividad, R.array.actividades_array)
        setupCustomSpinner(binding.spinnerDocumento, R.array.tipo_documento_array)

        // Recibir el DNI que fue ingresado en RegisterClientActivity
        val dniRecibido = intent.getStringExtra("dni_ingresado")
        if (!dniRecibido.isNullOrEmpty()) {
            binding.editNumeroDocumento.setText(dniRecibido) // Autocompletar el campo
            binding.editNumeroDocumento.isEnabled = false
        }

        // Listener del spinner de actividades
        binding.spinnerActividad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val nombreActividad = parent.getItemAtPosition(position).toString()
                Log.d("SpinnerDebug", "Seleccionaste: $nombreActividad")

                if (position != 0) {
                    // Obtener ID y monto de la actividad seleccionada
                    idActividadSeleccionada = dbHelper.obtenerIdActividadPorNombre(nombreActividad)
                    val monto = dbHelper.obtenerMontoActividadPorNombre(nombreActividad)

                    Log.d("SpinnerDebug", "ID Actividad: $idActividadSeleccionada")
                    Log.d("SpinnerDebug", "Monto obtenido: $monto")

                    // Mostrar el monto en pantalla si es válido
                    if (monto == 0.0) {
                        mostrarToast("No se encontró el monto de la actividad seleccionada")
                        binding.editTextMonto.setText("")
                        return
                    }

                    val montoFormateado = "$${String.format("%.2f", monto)}"
                    binding.editTextMonto.setText(montoFormateado)
                } else {
                    // Si no se eligió actividad, limpiar el campo de monto
                    idActividadSeleccionada = -1
                    binding.editTextMonto.setText("")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Configurar botones de la pantalla
        setupButtons()
    }

    // Configura los Spinners con diseño personalizado y primer ítem deshabilitado
    private fun setupCustomSpinner(spinner: Spinner, arrayResId: Int) {
        val items = resources.getStringArray(arrayResId).toList()

        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item,
            items
        ) {
            override fun isEnabled(position: Int): Boolean = position != 0

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                textView.text = getItem(position)
                textView.setTextColor(
                    ContextCompat.getColor(context, if (position == 0) R.color.hint_color else android.R.color.black)
                )
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                textView.text = getItem(position)
                textView.setTextColor(
                    ContextCompat.getColor(context, if (position == 0) R.color.hint_color else android.R.color.black)
                )
                return view
            }
        }

        spinner.adapter = adapter
        spinner.background = ContextCompat.getDrawable(this, R.drawable.edit_text_layer)
        spinner.setSelection(0)
    }

    // Configura los botones de cancelar y pagar
    private fun setupButtons() {
        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        }

        binding.btnPagar.setOnClickListener {
            val nombre = binding.editNombre?.text?.toString()?.trim() ?: ""
            val apellido = binding.editApellido?.text?.toString()?.trim() ?: ""
            val tipoDoc = binding.spinnerDocumento.selectedItem.toString()
            val numDoc = binding.editNumeroDocumento?.text?.toString()?.trim()?.toIntOrNull() ?: -1

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && numDoc != -1 && idActividadSeleccionada != -1) {
                // Intentar descontar cupo de la actividad
                val exito = dbHelper.descontarCupoActividad(idActividadSeleccionada)

                if (exito) {
                    val montoStr = binding.editTextMonto.text.toString()
                    val monto = montoStr.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0

                    val fechaPago = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    val nombreCompleto = "$nombre $apellido"

                    val intent = Intent(this, PrintReceiptActivity::class.java).apply {
                        putExtra("nombre", nombreCompleto)
                        putExtra("dni", numDoc.toString())
                        putExtra("monto", monto)
                        putExtra("fechaPago", fechaPago)
                        putExtra("numeroRecibo", "REC-${System.currentTimeMillis()}")
                    }
                    startActivity(intent)
                } else {
                    mostrarToast("No hay cupos disponibles, seleccione otra actividad")
                }
            } else {
                mostrarToast("Completa todos los campos correctamente")
            }
        }
    }

    // Muestra un toast con el mensaje recibido
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
