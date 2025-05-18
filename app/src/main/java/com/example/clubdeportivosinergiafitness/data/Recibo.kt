package com.example.clubdeportivosinergiafitness.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recibo(
    val nombre: String,
    val dni: String,
    val monto: Double,
    val fechaPago: String,
    val numeroRecibo: String
) : Parcelable
