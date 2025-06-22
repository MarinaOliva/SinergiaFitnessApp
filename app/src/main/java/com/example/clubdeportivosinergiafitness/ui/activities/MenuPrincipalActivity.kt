package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.databinding.ActivityMenuPrincipalBinding
import com.example.clubdeportivosinergiafitness.data.SessionManager

class MenuPrincipalActivity : BaseActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        // Inflar el contenido y agregarlo al contentFrame definido en BaseActivity
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botones
        binding.btnBuscarRegistrar.setOnClickListener {
            val intent = Intent(this, RegisterClientActivity::class.java)
            startActivity(intent)
        }

        binding.btnAbonarCuota.setOnClickListener {
            val intent = Intent(this, CheckFeeActivity::class.java)
            startActivity(intent)
        }

        binding.btnActividadesGuiadas.setOnClickListener {
            val intent = Intent(this, ViewActivitiesActivity::class.java)
            startActivity(intent)
        }

        binding.btnCuotasVencidas.setOnClickListener {
            val intent = Intent(this, CheckOverdueFeesActivity::class.java)
            startActivity(intent)
        }

        binding.btnCerrarSesion.setOnClickListener {
            // Cierra la sesión limpiando SharedPreferences
            sessionManager.logout()

            // Volver al login y cerrar actividad actual
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
