package com.rinconadadelsur.sistemas.bioseguridad.Herramientas

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura

/**
 * Datos mínimos locales para revisar la UI sin importar desde el servidor.
 * TODO: desactivar junto con DEV_OMITIR_LOGIN_SERVIDOR antes de producción.
 */
object DevDataSeeder {

    const val DEMO_CENCOS_CODIGO = "000001"
    const val DEMO_CENCOS_NOMBRE = "Planta Demo"

    @JvmStatic
    fun ensureDemoData(context: Context) {
        val conn = ConexionSQLiteHelper(context, dbEstructura.miBaseDatos, null, 1)
        val db = conn.writableDatabase

        if (count(db, dbEstructura.t_Cencos) == 0) {
            db.execSQL(
                "INSERT INTO ${dbEstructura.t_Cencos} (${dbEstructura.c_cCodigo},${dbEstructura.c_cNombre}) " +
                    "VALUES ('$DEMO_CENCOS_CODIGO','$DEMO_CENCOS_NOMBRE')"
            )
        }
        if (count(db, dbEstructura.t_TCencos) == 0) {
            db.execSQL(
                "INSERT INTO ${dbEstructura.t_TCencos} (${dbEstructura.c_tcCodigo},${dbEstructura.c_tcNombre}) " +
                    "VALUES ('$DEMO_CENCOS_CODIGO','$DEMO_CENCOS_NOMBRE')"
            )
        }
        if (count(db, dbEstructura.TABLA_CLV) == 0) {
            db.execSQL(
                "INSERT INTO ${dbEstructura.TABLA_CLV} (${dbEstructura.Campo_uCodClv},${dbEstructura.Campo_uNomClv}) " +
                    "VALUES ('DEV','Usuario demo')"
            )
        }
        if (count(db, dbEstructura.t_android) == 0) {
            db.execSQL(
                "INSERT INTO ${dbEstructura.t_android} (${dbEstructura.c_aCodigo},${dbEstructura.c_aSerie}," +
                    "${dbEstructura.c_aFecha},${dbEstructura.c_aEstado}) VALUES ('DEV','DEMO','2000-01-01','A')"
            )
        }
        if (count(db, dbEstructura.t_AnomaliaGarita) == 0) {
            db.execSQL(
                "INSERT INTO ${dbEstructura.t_AnomaliaGarita} (${dbEstructura.c_anCodigo},${dbEstructura.c_anDescripcion}) " +
                    "VALUES ('01','Anomalía demo')"
            )
        }
    }

    @JvmStatic
    fun codigoCencos(textoCampo: String?): String {
        if (textoCampo == null) return DEMO_CENCOS_CODIGO
        val t = textoCampo.trim()
        return if (t.length >= 6) t.substring(0, 6) else DEMO_CENCOS_CODIGO
    }

    private fun count(db: SQLiteDatabase, table: String): Int {
        db.rawQuery("SELECT COUNT(*) FROM $table", null).use { cursor ->
            if (cursor.moveToFirst()) return cursor.getInt(0)
        }
        return 0
    }
}
