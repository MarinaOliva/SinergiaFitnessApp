package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.databinding.ActivityNonMemberBinding
import java.text.SimpleDateFormat
import java.util.*

class NonMemberActivity : BaseActivity() {

    private lateinit var binding: ActivityNonMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNonMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCustomSpinner(binding.spinnerActividad, R.array.actividades_array)
        setupCustomSpinner(binding.spinnerMonto, R.array.montos_array)
        setupCustomSpinner(binding.spinnerDocumento, R.array.tipo_documento_array)

        setupButtons()
    }

    private fun setupCustomSpinner(spinner: Spinner, arrayResId: Int) {
        val items = resources.getStringArray(arrayResId).toList()

        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item,
            items
        ) {
            override fun isEnabled(position: Int): Boolean {
                // Deshabilita la primera opción (hint)
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                textView.text = getItem(position)

                val colorRes = if (position == 0) R.color.hint_color else android.R.color.black
                textView.setTextColor(ContextCompat.getColor(context, colorRes))

                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                textView.text = getItem(position)

                val colorRes = if (position == 0) R.color.hint_color else android.R.color.black
                textView.setTextColor(ContextCompat.getColor(context, colorRes))

                return view
            }
        }

        spinner.adapter = adapter
        spinner.background = ContextCompat.getDrawable(this, R.drawable.edit_text_layer)
        spinner.setSelection(0) // Asegura que el hint aparezca inicialmente
    }

    private fun setupButtons() {
        binding.btnCancelar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnPagar.setOnClickListener {
            // Obtener datos ingresados
            val nombre = binding.editNombre?.text?.toString()?.trim() ?: ""
            val apellido = binding.editApellido?.text?.toString()?.trim() ?: ""
            val nombreCompleto = "$nombre $apellido".trim()

            val dni = binding.editNumeroDocumento?.text?.toString()?.trim() ?: "Sin DNI"


            val montoStr = binding.spinnerMonto.selectedItem.toString()


            val monto = montoStr.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0

            val fechaPago = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val numeroRecibo = "REC-${System.currentTimeMillis()}" // número único simulado

            val intent = Intent(this, PrintReceiptActivity::class.java).apply {
                putExtra("nombre", nombreCompleto)
                putExtra("dni", dni)
                putExtra("monto", monto)
                putExtra("fechaPago", fechaPago)
                putExtra("numeroRecibo", numeroRecibo)
            }
            startActivity(intent)
        }
    }
}
