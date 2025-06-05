package com.example.clubdeportivosinergiafitness.ui.activities

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.clubdeportivosinergiafitness.BaseActivity
import com.example.clubdeportivosinergiafitness.R
import com.example.clubdeportivosinergiafitness.data.FakeCuotasData
import android.graphics.Color


class CheckOverdueFeesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_overdue_fees)

        val tablaCuotas = findViewById<LinearLayout>(R.id.tablaCuotas)

        val cuotasVencidas = FakeCuotasData.obtenerCuotasVencidasDelDia()

        for ((index, cuota) in cuotasVencidas.withIndex()) {
            val fila = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)

                // Alternar color de fondo para filas pares e impares
                setBackgroundColor(
                    if (index % 2 == 0) 0xFFEFEFEF.toInt() else 0xFFFFFFFF.toInt()
                )
            }

            val idText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                minWidth = dpToPx(60)
                text = cuota.idSocio.toString()
                setTextColor(Color.BLACK)
            }

            val nombreText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                minWidth = dpToPx(120)
                setPadding(dpToPx(8), 0, dpToPx(8), 0)
                text = cuota.nombre
                setTextColor(Color.BLACK)
            }

            val apellidoText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                minWidth = dpToPx(120)
                setPadding(dpToPx(8), 0, dpToPx(8), 0)
                text = cuota.apellido
                setTextColor(Color.BLACK)
            }

            val fechaText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                minWidth = dpToPx(150)
                setPadding(dpToPx(8), 0, dpToPx(8), 0)
                text = cuota.fechaVencimiento
                setTextColor(Color.BLACK)
            }

            val importeText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                minWidth = dpToPx(100)
                setPadding(dpToPx(12), 0, dpToPx(8), 0)
                text = "$${cuota.importe}"
                setTextColor(Color.BLACK)
            }

            fila.addView(idText)
            fila.addView(nombreText)
            fila.addView(apellidoText)
            fila.addView(fechaText)
            fila.addView(importeText)

            tablaCuotas.addView(fila)
        }
    }

    // Función auxiliar para convertir dp a px
    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
