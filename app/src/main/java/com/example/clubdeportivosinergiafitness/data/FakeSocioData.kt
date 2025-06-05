package com.example.clubdeportivosinergiafitness.data

data class Socio(
    val numero: Int,
    val nombre: String,
    val cuotaImporte: Double,
    val fechaPago: String?,
    val fechaVencimiento: String,
    val dni: String = "12345678"
)

object FakeSocioData {
    private val socios = listOf(
        Socio(123, "Maria Rodríguez", 35000.0, "31-05-2025", "31-05-2025"),
        Socio(456, "Juan Pérez", 20000.0, null, "31-05-2025"), // no pagó aún
        Socio(789, "Laura Gómez", 35000.0, "31-05-2025", "31-05-2025")
    )

    fun obtenerSocioPorNumero(numero: Int): Socio? {
        return socios.find { it.numero == numero }
    }
}
