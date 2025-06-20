package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper
import com.example.clubdeportivosinergiafitness.databinding.ActivityRegisterClientBinding

class RegisterClientActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterClientBinding
    private lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ClubDBHelper(this)

        setupCustomSpinner(binding.spinnerTipoDocumento, R.array.tipo_documento_array)

        binding.btnConsultar.setOnClickListener {
            val tipoDoc = binding.spinnerTipoDocumento.selectedItem.toString()

            // Validar que el usuario haya seleccionado un tipo distinto al hint por defecto
            if (tipoDoc == "Tipo de doc...") {
                Toast.makeText(this, "Por favor, seleccione un tipo de documento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Detener la ejecución si no seleccionó
            }

            val dniIngresado = binding.etNumeroDocumento.text.toString().trim()

            // Validar que el campo no esté vacío
            if (dniIngresado.isBlank()) {
                Toast.makeText(this, "Por favor, ingrese el número de documento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar que la longitud sea entre 7 y 8 caracteres (longitud típica del DNI)
            if (dniIngresado.length !in 7..8) {
                Toast.makeText(this, "El número de documento debe tener entre 7 y 8 dígitos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Intentar convertir el texto a número entero, si no es posible, mostrar error
            val dniInt = dniIngresado.toIntOrNull()
            if (dniInt == null) {
                Toast.makeText(this, "El número de documento debe ser numérico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Consultar en la base de datos si ya existe un cliente con ese DNI
            val existeCliente = dbHelper.obtenerClienteID(dniInt) != null
            // Consultar si ese cliente es socio
            val esSocio = dbHelper.esSocio(dniInt)

            // Según el caso, ir a la pantalla correspondiente:
            when {
                esSocio -> {
                    // Si es socio, ir a pantalla de info socio
                    val intent = Intent(this, SocioInfoActivity::class.java)
                    intent.putExtra("nro_doc", dniInt)
                    startActivity(intent)
                }
                existeCliente -> {
                    // Si existe como cliente pero no es socio, ir a registro para socio
                    val intent = Intent(this, RegistrationActivity::class.java)
                    intent.putExtra("dni_ingresado", dniIngresado)
                    startActivity(intent)
                }
                else -> {
                    // Si no existe ni como cliente ni socio, también ir a registro para nuevo cliente/socio
                    val intent = Intent(this, RegistrationActivity::class.java)
                    intent.putExtra("dni_ingresado", dniIngresado)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setupCustomSpinner(spinner: Spinner, arrayResId: Int) {
        val items = resources.getStringArray(arrayResId).toList()

        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item,
            items
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0 // Hint no seleccionable
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
        spinner.setSelection(0)
    }
}
