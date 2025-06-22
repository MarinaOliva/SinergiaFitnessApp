package com.example.clubdeportivosinergiafitness.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    // Nombre del archivo donde se guardarán las preferencias
    private val prefsName = "user_session"
    // Objeto SharedPreferences para acceder y editar los datos
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    companion object {
        // Claves para guardar y recuperar valores en SharedPreferences
        private const val KEY_USERNAME = "key_username"
        private const val KEY_EMAIL = "key_email"
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
    }

    // Guarda la sesión del usuario: nombre, email y flag de sesión activa
    fun saveUserSession(username: String, email: String) {
        prefs.edit().apply {
            putString(KEY_USERNAME, username)      // Guarda el nombre de usuario
            putString(KEY_EMAIL, email)            // Guarda el email
            putBoolean(KEY_IS_LOGGED_IN, true)    // Marca que hay sesión activa
            apply()                               // Aplica los cambios de forma asíncrona
        }
    }

    // Recupera el nombre de usuario guardado o null si no existe
    fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }

    // Recupera el email guardado o null si no existe
    fun getEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    // Limpia la sesión, elimina todos los datos guardados
    fun logout() {
        prefs.edit().clear().apply()
    }
}
