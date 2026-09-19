package com.rinconadadelsur.sistemas.bioseguridad.Herramientas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper;
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;

public class hProcedimiento extends ConexionSQLiteHelper {

    SQLiteDatabase db;
    String sql;
    dbEstructura dbE;

    public hProcedimiento(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public  String getIdAndroid() {
        String rIdAndroid="";
        db=this.getReadableDatabase();
        try{
            sql="SELECT "+dbE.c_aCodigo+" FROM "+dbE.t_android+"  GROUP BY "+dbE.c_aCodigo;
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        rIdAndroid=cursor.getString(0);
                    }
                }
            }

        }catch (Exception ex){

        }
        return rIdAndroid;
    }

    public  String getSerieDispositivo() {
        String rSerie="";
        db=this.getReadableDatabase();
        try{
            sql="SELECT "+dbE.c_aSerie+" FROM "+dbE.t_android+"  GROUP BY "+dbE.c_aCodigo;
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        rSerie=cursor.getString(0);
                    }
                }
            }

        }catch (Exception ex){

        }
        return rSerie;
    }

    public  String getCodigoUsuario() {
        String rIdAndroid="";
        db=this.getReadableDatabase();
        try{
            sql="SELECT "+dbE.c_cCodigo+" FROM "+dbE.TABLA_CLV+"  GROUP BY "+dbE.c_cCodigo;
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        rIdAndroid=cursor.getString(0);
                    }
                }
            }

        }catch (Exception ex){

        }
        return rIdAndroid;
    }

    public  String getNombreUsuario() {
        String sNombre="";
        db=this.getReadableDatabase();
        try{
            sql="SELECT "+dbE.c_cNombre+" FROM "+dbE.TABLA_CLV+"  GROUP BY "+dbE.c_cNombre;
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sNombre=cursor.getString(0);
                    }
                }
            }

        }catch (Exception ex){

        }
        return sNombre;
    }

    public  Integer getIdTabla(String xTabla,String xCampoId,String xCampoFecha,String xFiltroFecha,String xCampoDoc, String xDoc,String xCampoSerie, String xSerie,String xCampoNumero, String xNumero) {
        Integer rIdTabla=0;
        db=this.getReadableDatabase();
        try{
            sql="SELECT max(abs("+ xCampoId + ")) " +
                    " FROM " + xTabla+ " " +
                    " where "+xCampoFecha+"='"+xFiltroFecha+"'  AND "+xCampoDoc+"='"+xDoc+"'   AND "+xCampoSerie+"='"+xSerie+"'   AND "+xCampoNumero+"='"+xNumero+"'   " +
                    " GROUP BY "+xCampoFecha;
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        rIdTabla=cursor.getInt(0);
                    }
                }
            }

        }catch (Exception ex){

        }
        return rIdTabla;
    }

    public  Integer getIdMaxTabla(String xTabla,String xCampoId,String xCampoFecha,String xFiltroFecha) {
        Integer rIdTabla=1;
        db=this.getReadableDatabase();
        try{
            sql="SELECT max(abs("+ xCampoId + ")) FROM " + xTabla+ " where "+xCampoFecha+"='"+xFiltroFecha+"' GROUP BY "+xCampoFecha;
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        rIdTabla=cursor.getInt(0);
                        rIdTabla=rIdTabla+1;
                    }
                }else{
                    rIdTabla=1;
                }
            }

        }catch (Exception ex){

        }
        return rIdTabla;
    }

    public  Integer getIdMaxTablaDetalle(String xTabla,String xCampoId,String xCampoFecha,String xFecha,String xCampoDoc, String xDoc,String xCampoSerie, String xSerie,String xCampoNumero, String xNumero) {
        Integer rIdTabla=1;
        db=this.getReadableDatabase();
        try{
            sql="SELECT max(abs("+ xCampoId + ")) FROM " + xTabla+
                    " where "+xCampoFecha+"='"+xFecha+"'  AND "+xCampoDoc+"='"+xDoc+"'   AND "+xCampoSerie+"='"+xSerie+"'   AND "+xCampoNumero+"='"+xNumero+"'   " +
                    " GROUP BY "+xCampoFecha;
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        rIdTabla=cursor.getInt(0);
                        rIdTabla=rIdTabla+1;
                    }
                }else{
                    rIdTabla=1;
                }
            }

        }catch (Exception ex){

        }
        return rIdTabla;
    }

    public  String getElimarRegistro(String xTabla,String xCampoFecha,String xFecha,String xCampoCencos,String xCencos,String xCampoId,String xId) {
        String sMensaje="Registro Eliminado";
        db=this.getWritableDatabase();
        try{
            sql="DELETE  FROM " + xTabla+
                    " where "+xCampoFecha+"='"+xFecha+"'  AND "+xCampoCencos+"='"+xCencos+"'   AND "+xCampoId+"='"+xId+"' " ;
            db.execSQL(sql);
        }catch (Exception ex){
            sMensaje=ex.getMessage();
        }
        return sMensaje;
    }

}
