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
            if (tipoDoc == "Seleccione tipo documento") {
                Toast.makeText(this, "Por favor, seleccione un tipo de documento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dniIngresado = binding.etNumeroDocumento.text.toString()
            if (dniIngresado.isBlank()) {
                Toast.makeText(this, "Por favor, ingrese un DNI válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dniInt = dniIngresado.toIntOrNull()
            if (dniInt == null) {
                Toast.makeText(this, "El DNI debe ser numérico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val existeCliente = dbHelper.obtenerClienteID(dniInt) != null
            val esSocio = dbHelper.esSocio(dniInt)

            when {
                esSocio -> {
                    // Si es socio, ir a SocioInfoActivity
                    val intent = Intent(this, SocioInfoActivity::class.java)
                    intent.putExtra("dni_ingresado", dniIngresado)
                    startActivity(intent)
                }
                existeCliente -> {
                    // Cliente, pero no socio: ir a RegistrationActivity
                    val intent = Intent(this, RegistrationActivity::class.java)
                    intent.putExtra("dni_ingresado", dniIngresado)
                    startActivity(intent)
                }
                else -> {
                    // No existe como cliente ni socio: también a RegistrationActivity
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
