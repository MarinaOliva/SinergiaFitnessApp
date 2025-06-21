package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val user = usernameEditText.text.toString()
            val pass = passwordEditText.text.toString()

            val dbHelper = ClubDBHelper(this)
            val loginCorrecto = dbHelper.login(user, pass)

            if (loginCorrecto) {
                // Guardar usuario logueado en SharedPreferences
                val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
                prefs.edit().putString("usuarioLogueado", user).apply()

                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
