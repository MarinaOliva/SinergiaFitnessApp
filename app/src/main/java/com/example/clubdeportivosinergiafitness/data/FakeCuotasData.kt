package com.example.clubdeportivosinergiafitness.data

data class CuotaVencida(
    val idSocio: Int,
    val nombre: String,
    val apellido: String,
    val fechaVencimiento: String,
    val importe: Double
)

object FakeCuotasData {
    fun obtenerCuotasVencidasDelDia(): List<CuotaVencida> {
        return listOf(
            CuotaVencida(101, "María", "Pérez", "18/05/2025", 35000.0),
            CuotaVencida(102, "Lucas", "Martínez", "18/05/2025", 42000.0),
            CuotaVencida(103, "Sofía", "López", "18/05/2025", 39000.0)
        )
    }
}
