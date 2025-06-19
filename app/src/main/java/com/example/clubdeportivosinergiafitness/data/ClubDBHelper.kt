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

class ClubDBHelper(context: Context) : SQLiteOpenHelper(context, "ClubDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
        CREATE TABLE Cliente (
            clienteID INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            apellido TEXT NOT NULL,
            tipoDoc TEXT,
            numDoc INTEGER
        );
    """.trimIndent())

        db.execSQL("""
            CREATE TABLE Socio (
            socioID INTEGER PRIMARY KEY AUTOINCREMENT,
            clienteID INTEGER,
            telefono INTEGER,
            email TEXT,
            presentaAptoFisico INTEGER,
            FOREIGN KEY (clienteID) REFERENCES Cliente(clienteID)
        );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE NoSocio (
                noSocioID INTEGER PRIMARY KEY AUTOINCREMENT,
                clienteID INTEGER,
                FOREIGN KEY (clienteID) REFERENCES Cliente(clienteID)
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE Actividad (
                idActividad INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreActividad TEXT,
                costo REAL,
                cuposDisponibles INTEGER
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE Cuota (
                idCuota INTEGER PRIMARY KEY AUTOINCREMENT,
                socioID INTEGER,
                fechaVencimiento TEXT,
                fechaPago TEXT,
                importe REAL,
                FOREIGN KEY (socioID) REFERENCES Socio(socioID)
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE administrador (
                idAdministrador INTEGER PRIMARY KEY AUTOINCREMENT,
                NombreUsu TEXT,
                PassUsu TEXT,
                Activo INTEGER
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE SocioActividad (
                socioID INTEGER,
                actividadID INTEGER,
                PRIMARY KEY (socioID, actividadID),
                FOREIGN KEY (socioID) REFERENCES Socio(socioID),
                FOREIGN KEY (actividadID) REFERENCES Actividad(idActividad)
            );
        """.trimIndent())

        insertarDatosIniciales(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        // Insertar Clientes
        db.execSQL("""
           INSERT INTO Cliente (nombre, apellido, tipoDoc, numDoc) VALUES
        ('Martín', 'Aliaga', 'DNI', 12345678),
        ('Lucía', 'López', 'DNI', 87654321),
        ('Pedro', 'Fernández', 'DNI', 13579246),
        ('Sofía', 'Martínez', 'DNI', 24681357),
        ('Javier', 'Andino', 'DNI', 98765432),
        ('Carlos', 'Ramírez', 'DNI', 15975384),
        ('Ana', 'García', 'DNI', 75315986),
        ('Miguel', 'Torres', 'DNI', 85236914);
        """.trimIndent())

        // Insertar Socios
        db.execSQL("""
        INSERT INTO Socio (clienteID, telefono, email, presentaAptoFisico) VALUES
        (1, 11223344, 'martin.gonzalez@yahoo.com', 1),
        (2, 22334455, 'lucia.lopez@gmail.com', 1),
        (4, 56677899, 'sofia.martinez@live.com', 1),
        (6, 78899001, 'carlos.ramirez@gmail.com', 1),
        (7, 99001122, 'ana.garcia@yahoo.com', 1),
        (8, 90011222, 'miguel.torres@hotmail.com', 1);
    """.trimIndent())

        // Insertar No Socios
        db.execSQL("""
        INSERT INTO NoSocio (clienteID) VALUES (3), (5);
    """.trimIndent())

        // Insertar Actividades
        db.execSQL("""
            INSERT INTO Actividad (nombreActividad, costo, cuposDisponibles) VALUES
            ('Musculación y Aparatos', 2000.00, NULL),
            ('Natación', 3000.00, NULL),
            ('Pilates', 2500.00, 25),
            ('Yoga', 2300.00, 25),
            ('Zumba', 2200.00, 1),
            ('Aikido', 2700.00, 10),
            ('Acrobacia en tela', 3000.00, 7);
        """.trimIndent())

        // Insertar Cuotas
        db.execSQL("""
            INSERT INTO Cuota (socioID, fechaVencimiento, fechaPago, importe) VALUES
            (1, '2025-07-31', '2024-10-31', 33000.00),
            (2, '2025-07-15', '2024-10-14', 33000.00),
            (3, '2025-07-15', '2024-10-14', 33000.00),            
            (4, '2025-07-28', NULL, 33000.00),
            (5, '2025-07-20', NULL, 33000.00),
            (6, '2025-07-25', '2024-10-25', 33000.00);
        """.trimIndent())

        // Insertar Administradores
        db.execSQL("""
            INSERT INTO administrador (NombreUsu, PassUsu, Activo) VALUES
            ('Juan_Gomez', 'Juan2025!', 1),
            ('Maria_Perez', 'Maria@123', 1);
        """.trimIndent())

        // Insertar SocioActividad
        db.execSQL("""
            INSERT INTO SocioActividad (socioID, actividadID) VALUES
            (1, 6),
            (1, 3),
            (2, 3),
            (2, 5),
            (4, 5),
            (6, 6);
        """.trimIndent())
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
        val cursor = db.rawQuery("SELECT clienteID FROM Cliente WHERE numDoc = ?", arrayOf(numDoc.toString()))
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

    // FUNCION PARA CREAR NUEVO SOCIO + CLIENTE
    fun insertarSocio(
        nombre: String,
        apellido: String,
        tipoDoc: String,
        numDoc: Int,
        telefono: Int,
        email: String,
        presentaApto: Boolean
    ): Boolean {
        val db = writableDatabase

        // Verificar si ya existe un cliente con ese DNI
        val cursor = db.rawQuery(
            "SELECT clienteID FROM Cliente WHERE numDoc = ? AND tipoDoc = ?",
            arrayOf(numDoc.toString(), tipoDoc)
        )

        val clienteID: Long = if (cursor.moveToFirst()) {
            cursor.getLong(0) // Ya existe
        } else {
            // Insertar en Cliente
            val clienteValues = ContentValues().apply {
                put("nombre", nombre)
                put("apellido", apellido)
                put("tipoDoc", tipoDoc)
                put("numDoc", numDoc)
            }

            val nuevoID = db.insert("Cliente", null, clienteValues)
            if (nuevoID == -1L) {
                cursor.close()
                return false
            }
            nuevoID
        }
        cursor.close()

        // Verificar si ya es socio
        val cursorSocio = db.rawQuery(
            "SELECT socioID FROM Socio WHERE clienteID = ?",
            arrayOf(clienteID.toString())
        )
        if (cursorSocio.moveToFirst()) {
            cursorSocio.close()
            return false // Ya es socio
        }
        cursorSocio.close()

        // Insertar en Socio
        val socioValues = ContentValues().apply {
            put("clienteID", clienteID)
            put("telefono", telefono)
            put("email", email)
            put("presentaAptoFisico", if (presentaApto) 1 else 0)
        }

        val socioID = db.insert("Socio", null, socioValues)
        if (socioID == -1L) {
            Log.e("DBHelper", "Error al insertar socio para clienteID $clienteID")
            return false
        } else {
            Log.i("DBHelper", "Socio insertado con ID $socioID para clienteID $clienteID")
        }
        // Eliminar si estaba en NoSocio
        db.delete("NoSocio", "clienteID = ?", arrayOf(clienteID.toString()))
        return true
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

    //FUNCION VER ESTADO DE ULTIMA CUOTA
    fun obtenerEstadoUltimaCuota(socioID: Int): String {
        val db = readableDatabase
        val query = "SELECT fechaPago FROM Cuota WHERE socioID = ? ORDER BY fechaVencimiento DESC LIMIT 1"
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
        val cursor = db.rawQuery("SELECT idActividad FROM Actividad WHERE nombreActividad = ?", arrayOf(nombre))
        val id = if (cursor.moveToFirst()) cursor.getInt(0) else -1
        cursor.close()
        return id
    }

    //FUNCION OBTENER MONTO ACTIVIDAD POR NOMBRE
    fun obtenerMontoActividadPorNombre(nombre: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT costo FROM Actividad WHERE nombreActividad = ?", arrayOf(nombre))
        val monto = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return monto
    }




    // FUNCION DESCONTAR CUPOS DE ACTIVIDAD
    fun descontarCupoActividad(idActividad: Int): Boolean {
        val db = writableDatabase

        val cupoCursor = db.rawQuery("SELECT cuposDisponibles FROM Actividad WHERE idActividad = ?", arrayOf(idActividad.toString()))
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
               cu.fechaVencimiento
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
            val importe = cursor.getDouble(2)
            val fechaVencimiento = cursor.getString(3)

            val fechaPago = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date())

            DatosCuotaPago(nombreCompleto, socioID, importe, fechaVencimiento, fechaPago)
        } else {
            null
        }
        cursor.close()
        db.close()
        return datos
    }
    // FUNCION REGISTRAR PAGO DE CUOTA
    fun registrarPagoCuota(socioID: Int, importe: Double, fechaVencimiento: String): Boolean {
        val db = writableDatabase

        val fechaPago = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())

        val valores = ContentValues().apply {
            put("socioID", socioID)
            put("fechaPago", fechaPago)
            put("fechaVencimiento", fechaVencimiento)
            put("importe", importe)
        }

        val resultado = db.insert("Cuota", null, valores)
        db.close()
        return resultado != -1L
    }

}


