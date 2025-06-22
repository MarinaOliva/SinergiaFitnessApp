package com.example.clubdeportivosinergiafitness.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.util.Log

data class SocioDatos(
    val nombre: String,
    val apellido: String,
    val tipoDoc: String,
    val numDoc: Int,
    val socioID: Int
)

class ClubDBHelper(context: Context) : SQLiteOpenHelper(context, "ClubDB", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
        CREATE TABLE Cliente (
            clienteID INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            apellido TEXT NOT NULL,
            tipoDoc TEXT,
            numDoc INTEGER
        );
    """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE Socio (
            socioID INTEGER PRIMARY KEY AUTOINCREMENT,
            clienteID INTEGER,
            telefono INTEGER,
            email TEXT,
            presentaAptoFisico INTEGER,
            FOREIGN KEY (clienteID) REFERENCES Cliente(clienteID)
        );
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE NoSocio (
                noSocioID INTEGER PRIMARY KEY AUTOINCREMENT,
                clienteID INTEGER,
                FOREIGN KEY (clienteID) REFERENCES Cliente(clienteID)
            );
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE Actividad (
                idActividad INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreActividad TEXT,
                costo REAL,
                cuposDisponibles INTEGER
            );
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE Cuota (
                idCuota INTEGER PRIMARY KEY AUTOINCREMENT,
                socioID INTEGER,
                fechaVencimiento TEXT,
                fechaPago TEXT,
                importe REAL,
                FOREIGN KEY (socioID) REFERENCES Socio(socioID)
            );
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE administrador (
                idAdministrador INTEGER PRIMARY KEY AUTOINCREMENT,
                NombreUsu TEXT,
                PassUsu TEXT,
                email TEXT,
                Activo INTEGER
            );
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE SocioActividad (
                socioID INTEGER,
                actividadID INTEGER,
                PRIMARY KEY (socioID, actividadID),
                FOREIGN KEY (socioID) REFERENCES Socio(socioID),
                FOREIGN KEY (actividadID) REFERENCES Actividad(idActividad)
            );
        """.trimIndent()
        )

        insertarDatosIniciales(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        // Insertar Clientes
        db.execSQL(
            """
           INSERT INTO Cliente (nombre, apellido, tipoDoc, numDoc) VALUES
        ('Martín', 'Aliaga', 'DNI', 12345678),
        ('Lucía', 'López', 'DNI', 87654321),
        ('Pedro', 'Fernández', 'DNI', 13579246),
        ('Sofía', 'Martínez', 'DNI', 24681357),
        ('Javier', 'Andino', 'DNI', 98765432),
        ('Carlos', 'Ramírez', 'DNI', 15975384),
        ('Ana', 'García', 'DNI', 75315986),
        ('Miguel', 'Torres', 'DNI', 85236914);
        """.trimIndent()
        )

        // Insertar Socios
        db.execSQL(
            """
        INSERT INTO Socio (clienteID, telefono, email, presentaAptoFisico) VALUES
        (1, 11223344, 'martin.gonzalez@yahoo.com', 1),
        (2, 22334455, 'lucia.lopez@gmail.com', 1),
        (4, 56677899, 'sofia.martinez@live.com', 1),
        (6, 78899001, 'carlos.ramirez@gmail.com', 1),
        (7, 99001122, 'ana.garcia@yahoo.com', 1),
        (8, 90011222, 'miguel.torres@hotmail.com', 1);
    """.trimIndent()
        )

        // Insertar No Socios
        db.execSQL(
            """
        INSERT INTO NoSocio (clienteID) VALUES (3), (5);
    """.trimIndent()
        )

        // Insertar Actividades
        db.execSQL(
            """
            INSERT INTO Actividad (nombreActividad, costo, cuposDisponibles) VALUES
            ('Musculación y Aparatos', 2000.00, NULL),
            ('Natación', 3000.00, NULL),
            ('Pilates', 2500.00, 25),
            ('Yoga', 2300.00, 25),
            ('Zumba', 2200.00, 1),
            ('Aikido', 2700.00, 10),
            ('Acrobacia en tela', 3000.00, 7);
        """.trimIndent()
        )

        // Insertar Cuotas
        db.execSQL(
            """
            INSERT INTO Cuota (socioID, fechaVencimiento, fechaPago, importe) VALUES
            (1, '2025-06-10', '2025-06-05', 33000.00),
            (2, '2025-06-10', '2025-06-07', 33000.00),
            (3, '2025-06-10', '2025-06-08', 33000.00),            
            (4, '2025-06-10', NULL, 33000.00),
            (5, '2025-06-10', NULL, 33000.00),
            (6, '2025-06-10', '2024-06-09', 33000.00);
        """.trimIndent()
        )

        // Insertar Administradores
        db.execSQL(
            """
            INSERT INTO administrador (NombreUsu, PassUsu, email, Activo) VALUES
            ('Juan_Gomez', 'Juan2025!', 'juan.gomez@club.com', 1),
            ('Maria_Perez', 'Maria@123', 'maria.perez@club.com', 1);
        """.trimIndent()
        )

        // Insertar SocioActividad
        db.execSQL(
            """
            INSERT INTO SocioActividad (socioID, actividadID) VALUES
            (1, 6),
            (1, 3),
            (2, 3),
            (2, 5),
            (4, 5),
            (6, 6);
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // En caso de actualización:
        db.execSQL("DROP TABLE IF EXISTS SocioActividad")
        db.execSQL("DROP TABLE IF EXISTS Cuota")
        db.execSQL("DROP TABLE IF EXISTS Actividad")
        db.execSQL("DROP TABLE IF EXISTS NoSocio")
        db.execSQL("DROP TABLE IF EXISTS Socio")
        db.execSQL("DROP TABLE IF EXISTS Cliente")
        db.execSQL("DROP TABLE IF EXISTS administrador")
        onCreate(db)
    }

    // FUNCION LOGIN
    fun login(nombreUsuario: String, contrasena: String): Boolean {
        val db = this.readableDatabase
        val query = """
        SELECT * FROM administrador
        WHERE NombreUsu = ? AND PassUsu = ? AND Activo = 1
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(nombreUsuario, contrasena))
        val tieneUsuarioValido = cursor.count > 0
        cursor.close()
        db.close()

        return tieneUsuarioValido
    }

    // FUNCION PARA OBTENER EL ID DEL CLIENTE
    fun obtenerClienteID(numDoc: Int): Long? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT clienteID FROM Cliente WHERE numDoc = ?",
            arrayOf(numDoc.toString())
        )
        val clienteID = if (cursor.moveToFirst()) cursor.getLong(0) else null
        cursor.close()
        return clienteID
    }

    // FUNCION PARA SABER SI ES SOCIO
    fun esSocio(numDoc: Int): Boolean {
        val db = readableDatabase
        val query = """
        SELECT s.socioID 
        FROM Socio s 
        JOIN Cliente c ON s.clienteID = c.clienteID 
        WHERE c.numDoc = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(numDoc.toString()))
        val esSocio = cursor.count > 0
        cursor.close()
        return esSocio
    }
    // FUNCION PARA INSCRIBIR NUEVO SOCIO ( y guardarlo tmb en la tabla clientes)
    fun insertarSocio(
        nombre: String,
        apellido: String,
        tipoDoc: String,
        numDoc: Int,
        telefono: Int,
        email: String,
        presentaApto: Boolean
    ): Long {
        val db = writableDatabase

        // Verificar si ya existe un cliente con ese DNI
        val cursor = db.rawQuery(
            "SELECT clienteID FROM Cliente WHERE numDoc = ? AND tipoDoc = ?",
            arrayOf(numDoc.toString(), tipoDoc)
        )

        val clienteID: Long = if (cursor.moveToFirst()) {
            cursor.getLong(0)
        } else {
            val clienteValues = ContentValues().apply {
                put("nombre", nombre)
                put("apellido", apellido)
                put("tipoDoc", tipoDoc)
                put("numDoc", numDoc)
            }

            val nuevoID = db.insert("Cliente", null, clienteValues)
            if (nuevoID == -1L) {
                cursor.close()
                return -1L
            }
            nuevoID
        }
        cursor.close()

        val cursorSocio = db.rawQuery(
            "SELECT socioID FROM Socio WHERE clienteID = ?",
            arrayOf(clienteID.toString())
        )
        if (cursorSocio.moveToFirst()) {
            cursorSocio.close()
            return -1L // Ya es socio
        }
        cursorSocio.close()

        val socioValues = ContentValues().apply {
            put("clienteID", clienteID)
            put("telefono", telefono)
            put("email", email)
            put("presentaAptoFisico", if (presentaApto) 1 else 0)
        }

        val socioID = db.insert("Socio", null, socioValues)
        if (socioID == -1L) {
            Log.e("DBHelper", "Error al insertar socio para clienteID $clienteID")
            return -1L
        } else {
            Log.i("DBHelper", "Socio insertado con ID $socioID para clienteID $clienteID")
        }

        db.delete("NoSocio", "clienteID = ?", arrayOf(clienteID.toString()))

        val hoy = java.util.Calendar.getInstance()
        val dia = hoy.get(java.util.Calendar.DAY_OF_MONTH)

        val fechaVenc = java.util.Calendar.getInstance()
        fechaVenc.set(java.util.Calendar.DAY_OF_MONTH, 10)

// Si ya pasó el 10, la cuota vence el 10 del mes siguiente
        if (dia > 10) {
            fechaVenc.add(java.util.Calendar.MONTH, 1)
        }

        val fechaVencimiento = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(fechaVenc.time)


        val cuotaValues = ContentValues().apply {
            put("socioID", socioID)
            put("fechaVencimiento", fechaVencimiento)
            put("fechaPago", null as String?)  // No pagada aún
            put("importe", 33000.00)
        }

        val resultadoCuota = db.insert("Cuota", null, cuotaValues)
        if (resultadoCuota == -1L) {
            Log.e("DBHelper", "Error al insertar cuota para socioID $socioID")
            return -1L
        }

        return socioID
    }


    // FUNCION OBTENER DATOS DEL SOCIO
    fun obtenerDatosSocioPorDocumento(numDoc: Int): SocioDatos? {
        val db = readableDatabase
        val query = """
            SELECT c.nombre, c.apellido, c.tipoDoc, c.numDoc, s.socioID
            FROM Cliente c
            JOIN Socio s ON c.clienteID = s.clienteID
            WHERE c.numDoc = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(numDoc.toString()))
        val socioDatos = if (cursor.moveToFirst()) {
            SocioDatos(
                nombre = cursor.getString(0),
                apellido = cursor.getString(1),
                tipoDoc = cursor.getString(2),
                numDoc = cursor.getInt(3),
                socioID = cursor.getInt(4)
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return socioDatos
    }

    // FUNCION OBTENER DATOS DEL SOCIO POR ID
    fun obtenerDatosSocioPorID(socioID: Int): SocioDatos? {
        val db = readableDatabase
        val query = """
        SELECT c.nombre, c.apellido, c.tipoDoc, c.numDoc, s.socioID
        FROM Socio s
        JOIN Cliente c ON s.clienteID = c.clienteID
        WHERE s.socioID = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(socioID.toString()))
        val socioDatos = if (cursor.moveToFirst()) {
            SocioDatos(
                nombre = cursor.getString(0),
                apellido = cursor.getString(1),
                tipoDoc = cursor.getString(2),
                numDoc = cursor.getInt(3),
                socioID = cursor.getInt(4)
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return socioDatos
    }

    //FUNCION VER ESTADO DE CUOTA
    fun obtenerEstadoUltimaCuota(socioID: Int): String {
        val db = readableDatabase
        val query =
            "SELECT fechaPago FROM Cuota WHERE socioID = ? ORDER BY fechaVencimiento DESC LIMIT 1"
        val cursor = db.rawQuery(query, arrayOf(socioID.toString()))

        val estado = if (cursor.moveToFirst()) {
            val fechaPago = cursor.getString(0)
            if (fechaPago != null) "Última cuota: PAGADA" else "Última cuota: VENCIDA"
        } else {
            "No tiene cuotas registradas"
        }

        cursor.close()
        db.close()
        return estado
    }

    // FUNCION LISTAR ACTIVIDADES DEL SOCIO
    fun obtenerActividadesDelSocio(socioID: Int): List<String> {
        val db = readableDatabase
        val query = """
            SELECT a.nombreActividad
            FROM Actividad a
            JOIN SocioActividad sa ON a.idActividad = sa.actividadID
            WHERE sa.socioID = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(socioID.toString()))
        val actividades = mutableListOf<String>()
        while (cursor.moveToNext()) {
            actividades.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return actividades
    }

    // FUNCION OBTENER ID ACTIVIDAD POR NOMBRE
    fun obtenerIdActividadPorNombre(nombre: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT idActividad FROM Actividad WHERE nombreActividad = ?",
            arrayOf(nombre)
        )
        val id = if (cursor.moveToFirst()) cursor.getInt(0) else -1
        cursor.close()
        return id
    }

    //FUNCION OBTENER MONTO ACTIVIDAD POR NOMBRE
    fun obtenerMontoActividadPorNombre(nombre: String): Double {
        val db = readableDatabase
        val cursor =
            db.rawQuery("SELECT costo FROM Actividad WHERE nombreActividad = ?", arrayOf(nombre))
        val monto = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return monto
    }


    // FUNCION DESCONTAR CUPOS DE ACTIVIDAD
    fun descontarCupoActividad(idActividad: Int): Boolean {
        val db = writableDatabase

        val cupoCursor = db.rawQuery(
            "SELECT cuposDisponibles FROM Actividad WHERE idActividad = ?",
            arrayOf(idActividad.toString())
        )
        if (!cupoCursor.moveToFirst()) {
            cupoCursor.close()
            return false // Actividad no existe
        }

        val cupos = cupoCursor.getInt(0)
        cupoCursor.close()

        if (cupos == 0) return false // No hay cupos

        // Descontar cupo
        db.execSQL(
            "UPDATE Actividad SET cuposDisponibles = cuposDisponibles - 1 WHERE idActividad = ?",
            arrayOf(idActividad.toString())
        )

        return true
    }


    // FUNCION OBTENER  DATOS DEL SOCIO PARA PAGO CUOTA
    data class DatosCuotaPago(
        val nombreCompleto: String,
        val socioID: Int,
        val importe: Double,
        val fechaVencimiento: String,
        val fechaPago: String
    )

    fun obtenerDatosParaPago(numSocio: Int): DatosCuotaPago? {
        val db = readableDatabase
        val query = """
        SELECT c.nombre || ' ' || c.apellido AS nombreCompleto,
               s.socioID,
               cu.importe,
               cu.fechaVencimiento,
               cu.fechaPago
        FROM Socio s
        JOIN Cliente c ON s.clienteID = c.clienteID
        JOIN Cuota cu ON cu.socioID = s.socioID
        WHERE s.socioID = ?
        ORDER BY cu.fechaVencimiento DESC
        LIMIT 1
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(numSocio.toString()))
        val datos = if (cursor.moveToFirst()) {
            val nombreCompleto = cursor.getString(0)
            val socioID = cursor.getInt(1)
            val importeOriginal = cursor.getDouble(2)
            val fechaVencimiento = cursor.getString(3)
            val fechaPago = cursor.getString(4) // puede ser null

            // Si ya está pagada, no hay nada que pagar
            if (!fechaPago.isNullOrEmpty()) {
                null // retorna null y la Activity lo interpretará como "ya pagó"
            } else {
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val fechaHoy = sdf.parse(sdf.format(java.util.Date()))
                val fechaVto = sdf.parse(fechaVencimiento)

                val importeFinal = if (fechaHoy.after(fechaVto)) {
                    importeOriginal + 5000
                } else {
                    importeOriginal
                }

                val hoyTexto = sdf.format(java.util.Date())
                DatosCuotaPago(nombreCompleto, socioID, importeFinal, fechaVencimiento, hoyTexto)
            }
        } else {
            null
        }
        cursor.close()
        db.close()
        return datos
    }

    // Devuelve:
// 0 → socio no existe
// 1 → socio existe pero no tiene cuotas (por error en registro o bbdd)
// 2 → socio tiene cuotas, todas pagadas
// 3 → socio tiene al menos una cuota pendiente
    fun verificarEstadoCuotas(socioID: Int): Int {
        val db = readableDatabase

        // ¿Existe el socio?
        val cursorSocio = db.rawQuery("SELECT COUNT(*) FROM Socio WHERE socioID = ?", arrayOf(socioID.toString()))
        val existe = cursorSocio.moveToFirst() && cursorSocio.getInt(0) > 0
        cursorSocio.close()

        if (!existe) return 0

        // ¿Tiene cuotas?
        val cursorCuotas = db.rawQuery("SELECT COUNT(*) FROM Cuota WHERE socioID = ?", arrayOf(socioID.toString()))
        val tieneCuotas = cursorCuotas.moveToFirst() && cursorCuotas.getInt(0) > 0
        cursorCuotas.close()

        if (!tieneCuotas) return 1

        // ¿Tiene cuotas impagas?
        val cursorPendientes = db.rawQuery("SELECT COUNT(*) FROM Cuota WHERE socioID = ? AND fechaPago IS NULL", arrayOf(socioID.toString()))
        val tieneImpagas = cursorPendientes.moveToFirst() && cursorPendientes.getInt(0) > 0
        cursorPendientes.close()

        return if (tieneImpagas) 3 else 2
    }



    // FUNCION REGISTRAR PAGO DE CUOTA
    fun registrarPagoCuota(socioID: Int, importe: Double, fechaVencimiento: String): Boolean {
        val db = writableDatabase

        val fechaPago = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())

        val valores = ContentValues().apply {
            put("fechaPago", fechaPago)
            put("importe", importe)
        }

        // Actualizar la cuota existente que corresponde al socio y fecha de vencimiento, y que esté sin pagar (fechaPago IS NULL)
        val filasActualizadas = db.update(
            "Cuota",
            valores,
            "socioID = ? AND fechaVencimiento = ? AND fechaPago IS NULL",
            arrayOf(socioID.toString(), fechaVencimiento)
        )

        db.close()
        return filasActualizadas > 0
    }


    // FUNCION PARA EDITAR LAS ACTIVIDADES DEL SOCIO
    // funcion privada para obtener las act del socio
    private fun obtenerActividadesDelSocioDesde(db: SQLiteDatabase, socioID: Int): List<String> {
        val actividades = mutableListOf<String>()
        val query = """
        SELECT a.nombreActividad
        FROM Actividad a
        JOIN SocioActividad sa ON a.idActividad = sa.actividadID
        WHERE sa.socioID = ?
    """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(socioID.toString()))
        while (cursor.moveToNext()) {
            actividades.add(cursor.getString(0))
        }
        cursor.close()
        return actividades
    }

    // usa la fc anterior para actualizar las actividades del socio, sin abrir otra conexión
    fun actualizarActividadesDelSocio(socioID: Int, nuevasActividades: List<String>): Boolean {
        val db = writableDatabase
        db.beginTransaction()

        try {
            // Usamos la versión segura con la misma conexión
            val actuales = obtenerActividadesDelSocioDesde(db, socioID)

            val aEliminar = actuales.filter { it !in nuevasActividades }
            val aAgregar = nuevasActividades.filter { it !in actuales }

            // Verificar cupos de actividades nuevas
            for (nombre in aAgregar) {
                val id = obtenerIdActividadPorNombre(nombre)
                val cursor = db.rawQuery(
                    "SELECT cuposDisponibles FROM Actividad WHERE idActividad = ?",
                    arrayOf(id.toString())
                )

                val cupo = if (cursor.moveToFirst()) cursor.getInt(0) else null
                cursor.close()

                if (cupo == null) {
                    Log.e("DBHelper", "Actividad $nombre no encontrada")
                    throw Exception("Actividad '$nombre' no encontrada.")
                }

                if (cupo <= 0) {
                    throw Exception("Sin cupo en '$nombre'")
                }
            }

            // Liberar cupos de actividades eliminadas
            for (nombre in aEliminar) {
                val id = obtenerIdActividadPorNombre(nombre)
                db.execSQL(
                    "UPDATE Actividad SET cuposDisponibles = cuposDisponibles + 1 WHERE idActividad = ?",
                    arrayOf(id)
                )
                db.execSQL(
                    "DELETE FROM SocioActividad WHERE socioID = ? AND actividadID = ?",
                    arrayOf(socioID, id)
                )
            }

            // Asignar nuevas actividades y descontar cupos
            for (nombre in aAgregar) {
                val id = obtenerIdActividadPorNombre(nombre)
                db.execSQL(
                    "INSERT INTO SocioActividad (socioID, actividadID) VALUES (?, ?)",
                    arrayOf(socioID, id)
                )
                db.execSQL(
                    "UPDATE Actividad SET cuposDisponibles = cuposDisponibles - 1 WHERE idActividad = ?",
                    arrayOf(id)
                )
            }

            db.setTransactionSuccessful()
            return true

        } catch (e: Exception) {
            Log.e("DBHelper", "Error al actualizar actividades: ${e.message}")
            return false
        } finally {
            db.endTransaction()
        }
    }


    // FUNCION OBTENER CUPO ACTUAL ACTIVIDAD
    fun obtenerCupoActividad(idActividad: Int): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT cuposDisponibles FROM Actividad WHERE idActividad = ?",
            arrayOf(idActividad.toString())
        )

        val cupo = if (cursor.moveToFirst()) cursor.getInt(0) else null
        cursor.close()
        return cupo
    }

 // FUNCION OBTENER CUOTAS VENCIDAS
    data class CuotaVencida(
        val idSocio: Int,
        val nombre: String,
        val apellido: String,
        val fechaVencimiento: String,
        val importe: Double
    )

    fun obtenerCuotasVencidas(): List<CuotaVencida> {
        val db = readableDatabase
        val lista = mutableListOf<CuotaVencida>()

        val query = """
        SELECT s.socioID, c.nombre, c.apellido, cu.fechaVencimiento, cu.importe
        FROM Cuota cu
        JOIN Socio s ON cu.socioID = s.socioID
        JOIN Cliente c ON s.clienteID = c.clienteID
        WHERE cu.fechaPago IS NULL
        AND date(cu.fechaVencimiento) < date('now')
    """.trimIndent()

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val cuota = CuotaVencida(
                idSocio = cursor.getInt(0),
                nombre = cursor.getString(1),
                apellido = cursor.getString(2),
                fechaVencimiento = cursor.getString(3),
                importe = cursor.getDouble(4)
            )
            lista.add(cuota)
        }
        cursor.close()
        db.close()
        return lista
    }

    // FUNCION OBTENER DATOS DEL ADMIN (a partir del nombre de usuario)
    fun obtenerDatosAdmin(nombreUsuario: String): AdminDatos? {
        val db = readableDatabase
        val query = """
        SELECT NombreUsu, email, PassUsu FROM administrador WHERE NombreUsu = ?
    """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(nombreUsuario))
        val datos = if (cursor.moveToFirst()) {
            AdminDatos(
                nombreUsu = cursor.getString(0),
                email = cursor.getString(1),
                passUsu = cursor.getString(2)
            )
        } else null
        cursor.close()
        db.close()
        return datos
    }

    // FUNCION ACTUALIZAR CONTRASEÑA DEL  ADMIN
    fun actualizarPassAdmin(nombreUsuario: String, nuevaPass: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("PassUsu", nuevaPass)
        }
        val filasActualizadas = db.update(
            "administrador",
            cv,
            "NombreUsu = ?",
            arrayOf(nombreUsuario)
        )
        db.close()
        return filasActualizadas > 0
    }

    // Data class para admin
    data class AdminDatos(
        val nombreUsu: String,
        val email: String,
        val passUsu: String
    )

}