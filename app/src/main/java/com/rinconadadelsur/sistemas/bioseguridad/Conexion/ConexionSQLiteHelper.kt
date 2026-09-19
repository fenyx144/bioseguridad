package com.rinconadadelsur.sistemas.bioseguridad.Conexion

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura

open class ConexionSQLiteHelper(
    context: Context?,
    name: String?,
    factory: CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(dbEstructura.CREATE_TABLA_CLV)
        db.execSQL(dbEstructura.CREATE_TABLA_ANDROID)
        db.execSQL(dbEstructura.CREATE_TABLA_USUARIOS)
        db.execSQL(dbEstructura.Create_t_TCencos)
        db.execSQL(dbEstructura.Create_t_Cencos)
        db.execSQL(dbEstructura.Create_t_AnomaliaGarita)
        db.execSQL(dbEstructura.Create_t_RFomites)
        db.execSQL(dbEstructura.Create_t_ProcesoFomites)
        db.execSQL(dbEstructura.Create_t_TipoFomites)
        db.execSQL(dbEstructura.Create_t_CcosGalpon)
        db.execSQL(dbEstructura.Create_t_RGarita)
        db.execSQL(dbEstructura.Create_t_RMantenimiento)
        db.execSQL(dbEstructura.Create_t_RCercoElectrico)
        db.execSQL(dbEstructura.Create_t_RefFomites)
    }

    override fun onUpgrade(db: SQLiteDatabase, versionAntigua: Int, versionNueva: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + dbEstructura.CREATE_TABLA_USUARIOS)
        onCreate(db)
    }
}
