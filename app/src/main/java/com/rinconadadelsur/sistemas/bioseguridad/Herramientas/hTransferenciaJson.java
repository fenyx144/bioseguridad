package com.rinconadadelsur.sistemas.bioseguridad.Herramientas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper;
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;


public class hTransferenciaJson extends ConexionSQLiteHelper {

    /*<!-- TODO: CONEXION -->*/
    SQLiteDatabase db;
    dbEstructura dbE;
    String sql;
    /*<!-- TODO: HERRAMIENTAS -->*/
    hMetodos hM;

    /*<!-- TODO: VARIABLES G -->*/
    Integer iContador_Error = 0;
    String sMensaje, miFecha, miTime;

    /*<!-- TODO: RecepcionDeAves-->*/
    String dbc_rgId,dbc_rgUsuario,dbc_rgDate,dbc_rgTime,dbc_rgFecha,dbc_rgCencos,dbc_rgNomColaborador,dbc_rgTurno,dbc_rgNumFiltro,dbc_rgAnomalia,dbc_rgHora,dbc_rgDescripcion,dbc_rgAgenteCausal;
    String dbc_rmId,dbc_rmUsuario,dbc_rmDate,dbc_rmTime,dbc_rmFecha,dbc_rmCencos,dbc_rmTurno,dbc_rmElemento,dbc_rmFallo,dbc_rmDescripcion,dbc_rmNomColaborador;
    String dbc_rcId,dbc_rcUsuario,dbc_rcDate,dbc_rcTime,dbc_rcFecha,dbc_rcCencos,dbc_rcTurno,dbc_rcEstadoDerecho,dbc_rcDescEstadoDer,dbc_rcEstadoIzquierdo,dbc_rcDescEstadoIzq,dbc_rcNomEvaluador,dbc_rcHoraInicio,dbc_rcHoraFinal,dbc_rcComponente,dbc_rcFallo,dbc_rcDescripcion;
    String dbc_rfId,dbc_rfUsuario,dbc_rfDate,dbc_rfTime,dbc_rfFecha,dbc_rfCencos,dbc_rfTurno,dbc_rfReferencia,dbc_rfProceso,dbc_rfNomColaborador,dbc_rfDepredador,dbc_rfTipo,dbc_rfCantidad,dbc_rfHora,dbc_rfDentroCerco,dbc_rfObservacion;

    JsonObject jsonObjectDetalle;

    public hTransferenciaJson(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public String[] crearDataGarita(String xFecha,String xClv,String xIdAndroid, JsonArray json){
        try{
            iContador_Error=0;
            sMensaje="No se encontraron Datos a exportar";
            db=this.getReadableDatabase();
            sql="SELECT * FROM "+dbE.t_RGarita+" WHERE  "+dbE.c_rgFecha+"='"+xFecha+"' ORDER BY ABS("+dbE.c_rgId+")";
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    JsonObject jsonParent = new JsonObject();//Para agrupar por padres
                    Integer contar=0,iTotReg=0;
                    iTotReg=cursor.getCount();
                    jsonObjectDetalle = new JsonObject();
                    jsonObjectDetalle.addProperty("tabla", "rgarita");
                    jsonObjectDetalle.addProperty("tmodo_query", "ELIMINAR");
                    jsonObjectDetalle.addProperty("tfecpro", xFecha);
                    jsonObjectDetalle.addProperty("tidandroid", xIdAndroid);
                    json.add(jsonObjectDetalle);
                    while (cursor.moveToNext()) {
                        contar++;

                        dbc_rgId= cursor.getString(0);if(dbc_rgId==null){dbc_rgId="";}//"c_id";
                        dbc_rgUsuario= cursor.getString(1);if(dbc_rgUsuario==null){dbc_rgUsuario="";}//"c_tuser";
                        dbc_rgDate= cursor.getString(2);if(dbc_rgDate==null){dbc_rgDate="1000-01-01";}//"c_tdate";
                        dbc_rgTime= cursor.getString(3);if(dbc_rgTime==null){dbc_rgTime="00:00:00";}//"c_ttime";

                        dbc_rgFecha= cursor.getString(4);if(dbc_rgFecha==null){dbc_rgFecha="1000-01-01";}//"c_fecha";
                        dbc_rgCencos = cursor.getString(5);if(dbc_rgCencos==null){dbc_rgCencos="";}//"c_cencos";
                        dbc_rgNomColaborador = cursor.getString(6);if(dbc_rgNomColaborador==null){dbc_rgNomColaborador="";}//"c_nomcolaborador";
                        dbc_rgTurno= cursor.getString(7);if(dbc_rgTurno==null){dbc_rgTurno="";}//"c_turno";
                        dbc_rgNumFiltro= cursor.getString(8);if(dbc_rgNumFiltro==null){dbc_rgNumFiltro="";}//"c_numfiltro";
                        dbc_rgAnomalia= cursor.getString(9);if(dbc_rgAnomalia==null){dbc_rgAnomalia="";}//"c_anomalia";
                        dbc_rgHora= cursor.getString(10);if(dbc_rgHora==null){dbc_rgHora="";}//"c_hora";
                        dbc_rgDescripcion= cursor.getString(11);if(dbc_rgDescripcion==null){dbc_rgDescripcion="";}//"c_descripcion";
                        dbc_rgAgenteCausal= cursor.getString(12);if(dbc_rgAgenteCausal==null){dbc_rgAgenteCausal="";}//"c_agentecausal";



                        jsonObjectDetalle = new JsonObject();
                        jsonObjectDetalle.addProperty("tabla", "rgarita");
                        jsonObjectDetalle.addProperty("tmodo_query", "INSERTAR");

                        jsonObjectDetalle.addProperty("id", dbc_rgId);//0
                        jsonObjectDetalle.addProperty("usuario", dbc_rgUsuario);//1
                        jsonObjectDetalle.addProperty("date", dbc_rgDate);//2
                        jsonObjectDetalle.addProperty("time", dbc_rgTime);//3
                        jsonObjectDetalle.addProperty("fecha", dbc_rgFecha);//4
                        jsonObjectDetalle.addProperty("cencos", dbc_rgCencos);//5
                        jsonObjectDetalle.addProperty("colaborador", dbc_rgNomColaborador);//6
                        jsonObjectDetalle.addProperty("turno", dbc_rgTurno);//7
                        jsonObjectDetalle.addProperty("numfiltro", dbc_rgNumFiltro);//8
                        jsonObjectDetalle.addProperty("anomalia", dbc_rgAnomalia);//9
                        jsonObjectDetalle.addProperty("hora", dbc_rgHora);//10
                        jsonObjectDetalle.addProperty("descripcion", dbc_rgDescripcion);//11
                        jsonObjectDetalle.addProperty("agentecausal", dbc_rgAgenteCausal);//12

                        jsonObjectDetalle.addProperty("idandroid", xIdAndroid);//13
                        jsonObjectDetalle.addProperty("clv", xClv);//14
                        jsonObjectDetalle.addProperty("num_reg_enviados", iTotReg);//15
                        json.add(jsonObjectDetalle);
                    }
                    sMensaje="";
                    cursor.close();
                    db.close();
                }
            }
        }catch (Exception ex){
            sMensaje=ex.getMessage();
            iContador_Error=1;
        }
        return new String[]{sMensaje,json.toString(),iContador_Error.toString()};
    }
    public String[] crearDataMantenimiento(String xFecha,String xClv,String xIdAndroid, JsonArray json){
        try{
            iContador_Error=0;
            sMensaje="No se encontraron Datos a exportar";
            db=this.getReadableDatabase();
            sql="SELECT * FROM "+dbE.t_RMantenimiento+" WHERE  "+dbE.c_rmFecha+"='"+xFecha+"' ORDER BY ABS("+dbE.c_rmId+")";
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    JsonObject jsonParent = new JsonObject();//Para agrupar por padres
                    Integer contar=0,iTotReg=0;
                    iTotReg=cursor.getCount();
                    jsonObjectDetalle = new JsonObject();
                    jsonObjectDetalle.addProperty("tabla", "rmantenimiento");
                    jsonObjectDetalle.addProperty("tmodo_query", "ELIMINAR");
                    jsonObjectDetalle.addProperty("tfecpro", xFecha);
                    jsonObjectDetalle.addProperty("tidandroid", xIdAndroid);
                    json.add(jsonObjectDetalle);
                    while (cursor.moveToNext()) {
                        contar++;

                        dbc_rmId = cursor.getString(0);if(dbc_rmId==null){dbc_rmId="";}//"c_id";
                        dbc_rmUsuario =cursor.getString(1);if(dbc_rmUsuario==null){dbc_rmUsuario="";}// "c_tuser";
                        dbc_rmDate = cursor.getString(2);if(dbc_rmDate==null){dbc_rmDate="1000-01-01";}//"c_tdate";
                        dbc_rmTime = cursor.getString(3);if(dbc_rmTime==null){dbc_rmTime="00:00:00";}//"c_ttime";

                        dbc_rmFecha = cursor.getString(4);if(dbc_rmFecha==null){dbc_rmFecha="1000-01-01";}//"c_fecha";
                        dbc_rmCencos = cursor.getString(5);if(dbc_rmCencos==null){dbc_rmCencos="";}//"c_cencos";
                        dbc_rmTurno = cursor.getString(6);if(dbc_rmTurno==null){dbc_rmTurno="";}//"c_turno";
                        dbc_rmElemento = cursor.getString(7);if(dbc_rmElemento==null){dbc_rmElemento="";}//"c_elemento";
                        dbc_rmFallo =cursor.getString(8);if(dbc_rmFallo==null){dbc_rmFallo="";}// "c_fallo";
                        dbc_rmDescripcion =cursor.getString(9);if(dbc_rmDescripcion==null){dbc_rmDescripcion="";}// "c_descripcion";
                        dbc_rmNomColaborador = cursor.getString(10);if(dbc_rmNomColaborador==null){dbc_rmNomColaborador="";}//"c_nomcolaborador";



                        jsonObjectDetalle = new JsonObject();
                        jsonObjectDetalle.addProperty("tabla", "rmantenimiento");
                        jsonObjectDetalle.addProperty("tmodo_query", "INSERTAR");

                        jsonObjectDetalle.addProperty("id", dbc_rmId);//0
                        jsonObjectDetalle.addProperty("usuario", dbc_rmUsuario);//1
                        jsonObjectDetalle.addProperty("date", dbc_rmDate);//2
                        jsonObjectDetalle.addProperty("time", dbc_rmTime);//3
                        jsonObjectDetalle.addProperty("fecha", dbc_rmFecha);//4
                        jsonObjectDetalle.addProperty("cencos", dbc_rmCencos);//5
                        jsonObjectDetalle.addProperty("turno", dbc_rmTurno);//6
                        jsonObjectDetalle.addProperty("elemento", dbc_rmElemento);//7
                        jsonObjectDetalle.addProperty("fallo", dbc_rmFallo);//8
                        jsonObjectDetalle.addProperty("descripcion", dbc_rmDescripcion);//9
                        jsonObjectDetalle.addProperty("colaborador", dbc_rmNomColaborador);//10

                        jsonObjectDetalle.addProperty("idandroid", xIdAndroid);//11
                        jsonObjectDetalle.addProperty("clv", xClv);//12
                        jsonObjectDetalle.addProperty("num_reg_enviados", iTotReg);//13
                        json.add(jsonObjectDetalle);
                    }
                    sMensaje="";
                    cursor.close();
                    db.close();
                }
            }
        }catch (Exception ex){
            sMensaje=ex.getMessage();
            iContador_Error=1;
        }
        return new String[]{sMensaje,json.toString(),iContador_Error.toString()};
    }
    public String[] crearDataCercoElectrico(String xFecha,String xClv,String xIdAndroid, JsonArray json){
        try{
            iContador_Error=0;
            sMensaje="No se encontraron Datos a exportar";
            db=this.getReadableDatabase();
            sql="SELECT * FROM "+dbE.t_RCercoElectrico+" WHERE  "+dbE.c_rcFecha+"='"+xFecha+"' ORDER BY ABS("+dbE.c_rcId+")";
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    JsonObject jsonParent = new JsonObject();//Para agrupar por padres
                    Integer contar=0,iTotReg=0;
                    iTotReg=cursor.getCount();
                    jsonObjectDetalle = new JsonObject();
                    jsonObjectDetalle.addProperty("tabla", "rcercoelectrico");
                    jsonObjectDetalle.addProperty("tmodo_query", "ELIMINAR");
                    jsonObjectDetalle.addProperty("tfecpro", xFecha);
                    jsonObjectDetalle.addProperty("tidandroid", xIdAndroid);
                    json.add(jsonObjectDetalle);
                    while (cursor.moveToNext()) {
                        contar++;

                        dbc_rcId=cursor.getString(0);if(dbc_rcId==null){dbc_rcId="";}// "c_id";
                        dbc_rcUsuario =cursor.getString(1);if(dbc_rcUsuario==null){dbc_rcUsuario="";}// "c_tuser";
                        dbc_rcDate =cursor.getString(2);if(dbc_rcDate==null){dbc_rcDate="1000-01-01";}// "c_tdate";
                        dbc_rcTime =cursor.getString(3);if(dbc_rcTime==null){dbc_rcTime="00:00:00";}// "c_ttime";

                        dbc_rcFecha= cursor.getString(4);if(dbc_rcFecha==null){dbc_rcFecha="1000-01-01";}//"c_fecha";
                        dbc_rcCencos = cursor.getString(5);if(dbc_rcCencos==null){dbc_rcCencos="";}//"c_cencos";
                        dbc_rcTurno= cursor.getString(6);if(dbc_rcTurno==null){dbc_rcTurno="";}//"c_turno";
                        dbc_rcEstadoDerecho=cursor.getString(7);if(dbc_rcEstadoDerecho==null){dbc_rcEstadoDerecho="";}// "c_estadoderecho";
                        dbc_rcDescEstadoDer= cursor.getString(8);if(dbc_rcDescEstadoDer==null){dbc_rcDescEstadoDer="";}//"c_descestadoder";
                        dbc_rcEstadoIzquierdo= cursor.getString(9);if(dbc_rcEstadoIzquierdo==null){dbc_rcEstadoIzquierdo="";}//"c_estadoizquierdo";
                        dbc_rcDescEstadoIzq= cursor.getString(10);if(dbc_rcDescEstadoIzq==null){dbc_rcDescEstadoIzq="";}//"c_descestadoizq";
                        dbc_rcNomEvaluador= cursor.getString(11);if(dbc_rcNomEvaluador==null){dbc_rcNomEvaluador="";}//"c_nomevaluador";
                        dbc_rcHoraInicio= cursor.getString(12);if(dbc_rcHoraInicio==null){dbc_rcHoraInicio="";}//"c_horainicio";
                        dbc_rcHoraFinal= cursor.getString(13);if(dbc_rcHoraFinal==null){dbc_rcHoraFinal="";}//"c_horafinal";
                        dbc_rcComponente= cursor.getString(14);if(dbc_rcComponente==null){dbc_rcComponente="";}//"c_componente";
                        dbc_rcFallo= cursor.getString(15);if(dbc_rcFallo==null){dbc_rcFallo="";}//"c_fallo";
                        dbc_rcDescripcion= cursor.getString(16);if(dbc_rcDescripcion==null){dbc_rcDescripcion="";}//"c_descripcion";



                        jsonObjectDetalle = new JsonObject();
                        jsonObjectDetalle.addProperty("tabla", "rcercoelectrico");
                        jsonObjectDetalle.addProperty("tmodo_query", "INSERTAR");

                        jsonObjectDetalle.addProperty("id", dbc_rgId);//0
                        jsonObjectDetalle.addProperty("usuario", dbc_rcUsuario);//1
                        jsonObjectDetalle.addProperty("date", dbc_rcDate);//2
                        jsonObjectDetalle.addProperty("time", dbc_rcTime);//3
                        jsonObjectDetalle.addProperty("fecha", dbc_rcFecha);//4
                        jsonObjectDetalle.addProperty("cencos", dbc_rcCencos);//5
                        jsonObjectDetalle.addProperty("turno", dbc_rcTurno);//6
                        jsonObjectDetalle.addProperty("derecho", dbc_rcEstadoDerecho);//7
                        jsonObjectDetalle.addProperty("descderecho", dbc_rcDescEstadoDer);//8
                        jsonObjectDetalle.addProperty("izquierdo", dbc_rcEstadoIzquierdo);//9
                        jsonObjectDetalle.addProperty("descizquierdo", dbc_rcDescEstadoIzq);//10
                        jsonObjectDetalle.addProperty("nomevaluador", dbc_rcNomEvaluador);//11
                        jsonObjectDetalle.addProperty("horaini", dbc_rcHoraInicio);//12
                        jsonObjectDetalle.addProperty("horafin", dbc_rcHoraFinal);//13
                        jsonObjectDetalle.addProperty("componente", dbc_rcComponente);//14
                        jsonObjectDetalle.addProperty("fallo", dbc_rcFallo);//15
                        jsonObjectDetalle.addProperty("descripcion", dbc_rcDescripcion);//16

                        jsonObjectDetalle.addProperty("idandroid", xIdAndroid);//17
                        jsonObjectDetalle.addProperty("clv", xClv);//18
                        jsonObjectDetalle.addProperty("num_reg_enviados", iTotReg);//19
                        json.add(jsonObjectDetalle);
                    }
                    sMensaje="";
                    cursor.close();
                    db.close();
                }
            }
        }catch (Exception ex){
            sMensaje=ex.getMessage();
            iContador_Error=1;
        }
        return new String[]{sMensaje,json.toString(),iContador_Error.toString()};
    }
    public String[] crearDataFomites(String xFecha,String xClv,String xIdAndroid, JsonArray json){
        try{
            iContador_Error=0;
            sMensaje="No se encontraron Datos a exportar";
            db=this.getReadableDatabase();
            sql="SELECT * FROM "+dbE.t_RFomites+" WHERE  "+dbE.c_rfFecha+"='"+xFecha+"' ORDER BY ABS("+dbE.c_rfId+")";
            Cursor cursor=db.rawQuery(sql,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    JsonObject jsonParent = new JsonObject();//Para agrupar por padres
                    Integer contar=0,iTotReg=0;
                    iTotReg=cursor.getCount();
                    jsonObjectDetalle = new JsonObject();
                    jsonObjectDetalle.addProperty("tabla", "rfomites");
                    jsonObjectDetalle.addProperty("tmodo_query", "ELIMINAR");
                    jsonObjectDetalle.addProperty("tfecpro", xFecha);
                    jsonObjectDetalle.addProperty("tidandroid", xIdAndroid);
                    json.add(jsonObjectDetalle);
                    while (cursor.moveToNext()) {
                        contar++;


                        dbc_rfId= cursor.getString(0);if(dbc_rfId==null){dbc_rfId="";}// "c_id";
                        dbc_rfUsuario =cursor.getString(1);if(dbc_rfUsuario==null){dbc_rfUsuario="";}//  "c_tuser";
                        dbc_rfDate = cursor.getString(2);if(dbc_rfDate==null){dbc_rfDate="1000-01-01";}// "c_tdate";
                        dbc_rfTime =cursor.getString(3);if(dbc_rfTime==null){dbc_rfTime="00:00:00";}//  "c_ttime";

                        dbc_rfFecha= cursor.getString(4);if(dbc_rfFecha==null){dbc_rfFecha="1000-01-01";}// "c_fecha";
                        dbc_rfCencos =cursor.getString(5);if(dbc_rfCencos==null){dbc_rfCencos="";}//  "c_cencos";
                        dbc_rfTurno=cursor.getString(6);if(dbc_rfTurno==null){dbc_rfTurno="";}// "c_turno";
                        dbc_rfReferencia=cursor.getString(7);if(dbc_rfReferencia==null){dbc_rfReferencia="";}// "c_referencia";
                        dbc_rfProceso=cursor.getString(8);if(dbc_rfProceso==null){dbc_rfProceso="";}//  "c_proceso";
                        dbc_rfNomColaborador= cursor.getString(9);if(dbc_rfNomColaborador==null){dbc_rfNomColaborador="";}// "c_nomcolaborador";
                        dbc_rfDepredador= cursor.getString(10);if(dbc_rfDepredador==null){dbc_rfDepredador="";}// "c_depredador";
                        dbc_rfTipo=cursor.getString(11);if(dbc_rfTipo==null){dbc_rfTipo="";}//  "c_tipo";
                        dbc_rfCantidad= cursor.getString(12);if(dbc_rfCantidad==null){dbc_rfCantidad="0";}// "c_cantidad";
                        dbc_rfHora= cursor.getString(13);if(dbc_rfHora==null){dbc_rfHora="";}// "c_hora";
                        dbc_rfDentroCerco= cursor.getString(14);if(dbc_rfDentroCerco==null){dbc_rfDentroCerco="";}// "c_dentrocerco";
                        dbc_rfObservacion=cursor.getString(15);if(dbc_rfObservacion==null){dbc_rfObservacion="";}// "c_observacion";



                        jsonObjectDetalle = new JsonObject();
                        jsonObjectDetalle.addProperty("tabla", "rfomites");
                        jsonObjectDetalle.addProperty("tmodo_query", "INSERTAR");

                        jsonObjectDetalle.addProperty("id", dbc_rfId);//0
                        jsonObjectDetalle.addProperty("usuario", dbc_rfUsuario);//1
                        jsonObjectDetalle.addProperty("date", dbc_rfDate);//2
                        jsonObjectDetalle.addProperty("time", dbc_rfTime);//3
                        jsonObjectDetalle.addProperty("fecha", dbc_rfFecha);//4
                        jsonObjectDetalle.addProperty("cencos", dbc_rfCencos);//5
                        jsonObjectDetalle.addProperty("turno", dbc_rfTurno);//6
                        jsonObjectDetalle.addProperty("referencia", dbc_rfReferencia);//7
                        jsonObjectDetalle.addProperty("proceso", dbc_rfProceso);//8
                        jsonObjectDetalle.addProperty("colabordor", dbc_rfNomColaborador);//9
                        jsonObjectDetalle.addProperty("depredador", dbc_rfDepredador);//10
                        jsonObjectDetalle.addProperty("tipo", dbc_rfTipo);//11
                        jsonObjectDetalle.addProperty("cantidad", dbc_rfCantidad);//12
                        jsonObjectDetalle.addProperty("hora", dbc_rfHora);//13
                        jsonObjectDetalle.addProperty("dentrocerco", dbc_rfDentroCerco);//14
                        jsonObjectDetalle.addProperty("observacion", dbc_rfObservacion);//15

                        jsonObjectDetalle.addProperty("idandroid", xIdAndroid);//16
                        jsonObjectDetalle.addProperty("clv", xClv);//17
                        jsonObjectDetalle.addProperty("num_reg_enviados", iTotReg);//18
                        json.add(jsonObjectDetalle);
                    }
                    sMensaje="";
                    cursor.close();
                    db.close();
                }
            }
        }catch (Exception ex){
            sMensaje=ex.getMessage();
            iContador_Error=1;
        }
        return new String[]{sMensaje,json.toString(),iContador_Error.toString()};
    }
}

