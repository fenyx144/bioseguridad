package com.rinconadadelsur.sistemas.bioseguridad.Conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbEstructura.CREATE_TABLA_CLV);
        db.execSQL(dbEstructura.CREATE_TABLA_ANDROID);
        db.execSQL(dbEstructura.CREATE_TABLA_USUARIOS);
        db.execSQL(dbEstructura.Create_t_TCencos);
        db.execSQL(dbEstructura.Create_t_Cencos);
        db.execSQL(dbEstructura.Create_t_AnomaliaGarita);
        db.execSQL(dbEstructura.Create_t_RFomites);
        db.execSQL(dbEstructura.Create_t_ProcesoFomites);
        db.execSQL(dbEstructura.Create_t_TipoFomites);
        db.execSQL(dbEstructura.Create_t_CcosGalpon);
        db.execSQL(dbEstructura.Create_t_RGarita);
        db.execSQL(dbEstructura.Create_t_RMantenimiento);
        db.execSQL(dbEstructura.Create_t_RCercoElectrico);
        db.execSQL(dbEstructura.Create_t_RefFomites);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+dbEstructura.CREATE_TABLA_USUARIOS);
        onCreate(db);
    }
}
