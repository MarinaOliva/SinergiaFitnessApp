package com.example.clubdeportivosinergiafitness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.ClubDBHelper

class SelectActivitiesActivity : BaseActivity() {

    private lateinit var checkboxes: List<CheckBox>
    private lateinit var dbHelper: ClubDBHelper
    private var socioID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_activities)

        dbHelper = ClubDBHelper(this)
        socioID = intent.getIntExtra("SOCIO_ID", -1)

        checkboxes = listOf(
            findViewById(R.id.cb_Aikido),
            findViewById(R.id.cb_Yoga),
            findViewById(R.id.cb_Pilates),
            findViewById(R.id.cb_Tela),
            findViewById(R.id.cb_Zumba)
        )

        val nombreActividades = listOf("Aikido", "Yoga", "Pilates", "Acrobacia en tela", "Zumba")

        // Preseleccionar actividades actuales del socio
        val actividadesActuales = dbHelper.obtenerActividadesDelSocio(socioID)
        for ((i, nombre) in nombreActividades.withIndex()) {
            if (actividadesActuales.contains(nombre)) {
                checkboxes[i].isChecked = true
            }
        }

        // Confirmar selección
        findViewById<Button>(R.id.btn_ConfirmarSeleccion).setOnClickListener {
            val seleccionadas = checkboxes.mapIndexedNotNull { index, cb ->
                if (cb.isChecked) nombreActividades[index] else null
            }

            if (seleccionadas.size > 3) {
                Toast.makeText(this, "Solo puedes seleccionar hasta 3 actividades.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevas = seleccionadas.toSet() - actividadesActuales.toSet()
            val eliminadas = actividadesActuales.toSet() - seleccionadas.toSet()

            val sinCupo = nuevas.filter { act ->
                val id = dbHelper.obtenerIdActividadPorNombre(act)
                val cupo = dbHelper.obtenerCupoActividad(id)
                cupo != null && cupo == 0
            }

            if (sinCupo.isNotEmpty()) {
                Toast.makeText(this, "Sin cupo: ${sinCupo.joinToString()}", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            dbHelper.actualizarActividadesDelSocio(socioID, seleccionadas)
            Toast.makeText(this, "Actividades actualizadas correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btn_CancelarSeleccion).setOnClickListener {
            finish()
        }
    }
}
