package com.example.clubdeportivosinergiafitness.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClubDBHelper(context: Context) : SQLiteOpenHelper(context, "ClubDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE Cliente (
                clienteID INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                tipoDoc TEXT,
                numDoc INTEGER,
                telefono INTEGER,
                email TEXT,
                presentaAptoFisico INTEGER
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE Socio (
                socioID INTEGER PRIMARY KEY AUTOINCREMENT,
                clienteID INTEGER,
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
            INSERT INTO Cliente (nombre, apellido, tipoDoc, numDoc, telefono, email, presentaAptoFisico) VALUES
            ('Martín', 'Aliaga', 'DNI', 12345678, 11223344, 'martin.gonzalez@yahoo.com', 1),
            ('Lucía', 'López', 'DNI', 87654321, 22334455, 'lucia.lopez@gmail.com', 1),
            ('Pedro', 'Fernández', 'DNI', 13579246, 45566778, 'pedro.fernandez@hotmail.com', 0),
            ('Sofía', 'Martínez', 'DNI', 24681357, 56677899, 'sofia.martinez@live.com', 1),
            ('Javier', 'Andino', 'DNI', 98765432, 67788900, 'javier.perez@outlook.com', 0),
            ('Carlos', 'Ramírez', 'DNI', 15975384, 78899001, 'carlos.ramirez@gmail.com', 1),
            ('Ana', 'García', 'DNI', 75315986, 99001122, 'ana.garcia@yahoo.com', 1),
            ('Miguel', 'Torres', 'DNI', 85236914, 90011222, 'miguel.torres@hotmail.com', 1);
        """.trimIndent())

        // Insertar Socios y No Socios (clienteID de 1 a 8)
        db.execSQL("INSERT INTO Socio (clienteID) VALUES (1), (2), (4), (6), (7), (8);")
        db.execSQL("INSERT INTO NoSocio (clienteID) VALUES (3), (5);")

        // Insertar Actividades
        db.execSQL("""
            INSERT INTO Actividad (nombreActividad, costo, cuposDisponibles) VALUES
            ('Musculación y Aparatos', 2000.00, NULL),
            ('Natación', 3000.00, NULL),
            ('Pilates', 2500.00, 25),
            ('Yoga', 2300.00, 25),
            ('Zumba', 2200.00, 0),
            ('Aikido', 2700.00, 10),
            ('Acrobacia en tela', 3000.00, 7);
        """.trimIndent())

        // Insertar Cuotas
        db.execSQL("""
            INSERT INTO Cuota (socioID, fechaVencimiento, fechaPago, importe) VALUES
            (1, '2024-10-31', '2024-10-31', 23000.00),
            (2, '2024-10-15', '2024-10-14', 23000.00),
            (4, '2024-10-28', NULL, 23000.00),
            (5, '2024-10-20', NULL, 23000.00),
            (6, '2024-10-25', '2024-10-25', 23000.00);
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

    // función para login
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

}

