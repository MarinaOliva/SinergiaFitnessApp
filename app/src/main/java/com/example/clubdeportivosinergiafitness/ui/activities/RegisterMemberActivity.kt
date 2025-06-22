package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
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

        dbHelper = ClubDBHelper(this)

        // Fecha actual en formato yyyy-MM-dd
        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        tvFecha.text = fechaActual

        // dni ya ingresado
        val dni = intent.getStringExtra("dni_ingresado")
        etDni.setText(dni)

        // Botón Registrar
        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val dniStr = etDni.text.toString().trim()
            val telefonoStr = etTelefono.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val aptoSeleccionadoId = rgAptoFisico.checkedRadioButtonId

            // Validaciones
            if (!validarCampos(nombre, apellido, dniStr, telefonoStr, email, aptoSeleccionadoId)) {
                return@setOnClickListener
            }

            val dni = dniStr.toInt()
            val telefono = telefonoStr.toInt()
            val presentaApto = aptoSeleccionadoId == rbAptoSi.id

            val socioID = dbHelper.insertarSocio(
                nombre = nombre,
                apellido = apellido,
                tipoDoc = "DNI",
                numDoc = dni,
                telefono = telefono,
                email = email,
                presentaApto = presentaApto
            )

            if (socioID != -1L) {
                Toast.makeText(this, "Socio registrado correctamente", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, RegisteredMemberActivity::class.java).apply {
                    putExtra("nombre", nombre)
                    putExtra("apellido", apellido)
                    putExtra("dni", dniStr)
                    putExtra("numeroSocio", socioID.toString())
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar socio", Toast.LENGTH_LONG).show()
            }
        }

        // Botón Cancelar
        btnCancelar.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        }
    }

    // Validación de campos
    private fun validarCampos(
        nombre: String,
        apellido: String,
        dniStr: String,
        telefonoStr: String,
        email: String,
        aptoSeleccionadoId: Int
    ): Boolean {
        if (nombre.isBlank() || apellido.isBlank() || dniStr.isBlank() || telefonoStr.isBlank() || email.isBlank() || aptoSeleccionadoId == -1) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }

        val regexNombre = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$".toRegex()
        if (!regexNombre.matches(nombre) || !regexNombre.matches(apellido)) {
            Toast.makeText(this, "Nombre y apellido deben contener solo letras", Toast.LENGTH_SHORT).show()
            return false
        }

        if (dniStr.length !in 7..8 || dniStr.toIntOrNull() == null) {
            Toast.makeText(this, "DNI inválido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (telefonoStr.length < 8 || telefonoStr.toIntOrNull() == null) {
            Toast.makeText(this, "Teléfono inválido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email no válido", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
