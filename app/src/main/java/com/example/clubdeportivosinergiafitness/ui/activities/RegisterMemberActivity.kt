package com.example.clubdeportivosinergiafitness.ui.activities

import android.os.Bundle
import android.widget.*
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper
import java.text.SimpleDateFormat
import java.util.*

class RegisterMemberActivity : BaseActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etDni: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etEmail: EditText
    private lateinit var rgAptoFisico: RadioGroup
    private lateinit var rbAptoSi: RadioButton
    private lateinit var rbAptoNo: RadioButton
    private lateinit var tvFecha: TextView
    private lateinit var btnRegistrar: Button
    private lateinit var btnCancelar: Button

    private lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_member)

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etDni = findViewById(R.id.etNumeroDocumento)
        etTelefono = findViewById(R.id.etTelefono)
        etEmail = findViewById(R.id.etEmail)
        rgAptoFisico = findViewById(R.id.rgAptoFisico)
        rbAptoSi = findViewById(R.id.rbSi)
        rbAptoNo = findViewById(R.id.rbNo)
        tvFecha = findViewById(R.id.etFecha)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Instancia DB Helper
        dbHelper = ClubDBHelper(this)

        // Autocompletar fecha actual en formato yyyy-MM-dd
        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        tvFecha.text = fechaActual

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val dniStr = etDni.text.toString().trim()
            val telefonoStr = etTelefono.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val aptoSeleccionadoId = rgAptoFisico.checkedRadioButtonId

            // Validaciones básicas
            if (nombre.isEmpty() || apellido.isEmpty() || dniStr.isEmpty() || telefonoStr.isEmpty() || email.isEmpty() || aptoSeleccionadoId == -1) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dni = dniStr.toIntOrNull()
            val telefono = telefonoStr.toIntOrNull()

            if (dni == null || telefono == null) {
                Toast.makeText(this, "Documento y teléfono deben ser números válidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val presentaApto = aptoSeleccionadoId == rbAptoSi.id

            // Guardar en la base
            val exito = dbHelper.insertarSocio(
                nombre = nombre,
                apellido = apellido,
                tipoDoc = "DNI",
                numDoc = dni,
                telefono = telefono,
                email = email,
                presentaApto = presentaApto
            )

            if (exito) {
                Toast.makeText(this, "Socio registrado correctamente", Toast.LENGTH_LONG).show()
                finish() // Opcional: cierra esta activity y vuelve a la anterior
            } else {
                Toast.makeText(this, "Error al registrar socio", Toast.LENGTH_LONG).show()
            }
        }

        btnCancelar.setOnClickListener {
            finish() // Solo cierra esta activity y vuelve atrás
        }
    }
}
