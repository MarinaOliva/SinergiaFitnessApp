package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper
import com.example.clubdeportivosinergiafitness.data.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val user = usernameEditText.text.toString()
            val pass = passwordEditText.text.toString()

            val dbHelper = ClubDBHelper(this)
            val loginCorrecto = dbHelper.login(user, pass)

            if (loginCorrecto) {
                // Obtener email del usuario desde la base
                val admin = dbHelper.obtenerDatosAdmin(user)
                val email = admin?.email ?: ""

                // Guardar sesión con SessionManager
                sessionManager.saveUserSession(user, email)

                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
