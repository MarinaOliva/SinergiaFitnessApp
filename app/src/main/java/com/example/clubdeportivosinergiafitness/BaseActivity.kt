package com.example.clubdeportivosinergiafitness

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivosinergiafitness.data.SessionManager
import com.example.clubdeportivosinergiafitness.ui.activities.AboutActivity
import com.example.clubdeportivosinergiafitness.ui.activities.LoginActivity
import com.example.clubdeportivosinergiafitness.ui.activities.UserActivity
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity() {

    private lateinit var contentFrame: FrameLayout  // contenedor donde se insertará el contenido de cada pantalla hija
    private lateinit var sessionManager: SessionManager // para manejar sesión usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base) // carga la base
        contentFrame = findViewById(R.id.contentFrame) // referencia al FrameLayout donde irá el contenido dinámico
        sessionManager = SessionManager(this)  // inicializo session manager
        configurarToolbar()
    }

    // Sobrescribimos este método para insertar la vista en el layout base
    override fun setContentView(layoutResID: Int) {
        val view = LayoutInflater.from(this).inflate(layoutResID, contentFrame, false)
        contentFrame.removeAllViews()
        contentFrame.addView(view)
    }

    // También sobreescribimos este por si se pasa un View directamente (como binding.root)
    override fun setContentView(view: View?) {
        contentFrame.removeAllViews()
        contentFrame.addView(view)
    }

    fun configurarToolbar() {
        val btnVolver = findViewById<ImageButton>(R.id.btnBack)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)

        // Acción del botón de volver
        btnVolver?.setOnClickListener {
            onBackPressed()  // Esto hace que se regrese a la pantalla anterior
        }

        // Acción del botón de menú hamburguesa
        btnMenu?.setOnClickListener {
            // Mostrar el menú hamburguesa
            val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)  // Si está abierto, lo cierra
            } else {
                drawerLayout.openDrawer(GravityCompat.START)  // Si está cerrado, lo abre
            }
        }

        // Configuración del menú lateral
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemUsuario -> {
                    // Obtengo usuario y email guardados en session
                    val usuario = sessionManager.getUsername() ?: ""
                    val email = sessionManager.getEmail() ?: ""

                    val intent = Intent(this, UserActivity::class.java)
                    // Paso usuario y email a UserActivity
                    intent.putExtra("usuario", usuario)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
                R.id.itemCambiarTema -> {
                    // TODO: Implementar cambio de tema más adelante
                    Toast.makeText(this, "Funcionalidad próximamente", Toast.LENGTH_SHORT).show()
                }
                R.id.itemAcercaDeApp -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                R.id.itemCerrarSesion -> {
                    // Limpio sesión antes de salir
                    sessionManager.logout()

                    // Abro Login y limpio stack de actividades
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }

            val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
            drawerLayout.closeDrawer(GravityCompat.START)

            true
        }
    }
}
