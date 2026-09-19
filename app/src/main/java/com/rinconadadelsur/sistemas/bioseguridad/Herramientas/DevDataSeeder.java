package com.rinconadadelsur.sistemas.bioseguridad.Herramientas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper;
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;

/**
 * Datos mínimos locales para revisar la UI sin importar desde el servidor.
 * TODO: desactivar junto con DEV_OMITIR_LOGIN_SERVIDOR antes de producción.
 */
public final class DevDataSeeder {

    public static final String DEMO_CENCOS_CODIGO = "000001";
    public static final String DEMO_CENCOS_NOMBRE = "Planta Demo";

    private DevDataSeeder() {
    }

    public static void ensureDemoData(Context context) {
        dbEstructura dbE = new dbEstructura();
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, dbE.miBaseDatos, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        if (count(db, dbE.t_Cencos) == 0) {
            db.execSQL("INSERT INTO " + dbE.t_Cencos + " (" + dbE.c_cCodigo + "," + dbE.c_cNombre + ") VALUES ('"
                    + DEMO_CENCOS_CODIGO + "','" + DEMO_CENCOS_NOMBRE + "')");
        }
        if (count(db, dbE.t_TCencos) == 0) {
            db.execSQL("INSERT INTO " + dbE.t_TCencos + " (" + dbE.c_tcCodigo + "," + dbE.c_tcNombre + ") VALUES ('"
                    + DEMO_CENCOS_CODIGO + "','" + DEMO_CENCOS_NOMBRE + "')");
        }
        if (count(db, dbE.TABLA_CLV) == 0) {
            db.execSQL("INSERT INTO " + dbE.TABLA_CLV + " (" + dbE.Campo_uCodClv + "," + dbE.Campo_uNomClv
                    + ") VALUES ('DEV','Usuario demo')");
        }
        if (count(db, dbE.t_android) == 0) {
            db.execSQL("INSERT INTO " + dbE.t_android + " (" + dbE.c_aCodigo + "," + dbE.c_aSerie + ","
                    + dbE.c_aFecha + "," + dbE.c_aEstado + ") VALUES ('DEV','DEMO','2000-01-01','A')");
        }
        if (count(db, dbE.t_AnomaliaGarita) == 0) {
            db.execSQL("INSERT INTO " + dbE.t_AnomaliaGarita + " (" + dbE.c_anCodigo + "," + dbE.c_anDescripcion
                    + ") VALUES ('01','Anomalía demo')");
        }
    }

    /** Primeros 6 caracteres del campo cencos (acepta "000001 - X" o "000001|X"). */
    public static String codigoCencos(String textoCampo) {
        if (textoCampo == null) {
            return DEMO_CENCOS_CODIGO;
        }
        String t = textoCampo.trim();
        if (t.length() >= 6) {
            return t.substring(0, 6);
        }
        return DEMO_CENCOS_CODIGO;
    }

    private static int count(SQLiteDatabase db, String table) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }
        return 0;
    }
}
