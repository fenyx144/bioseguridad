package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transferencias;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper;
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hTransferenciaJson;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentBaseDatosExportacionBinding;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentBaseDatosImportacionBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class BaseDatosExportacionFragment extends Fragment {

    private FragmentBaseDatosExportacionBinding bg;

    String tempTabla,sSaltoLinea,sCadena,sCadenaUrl;

    String sCodTipoPollo,sNomTipoPollo;//Tipo de Pollo
    String sCodDestPollo,sNomDestPollo;//Destino de Pollo
    String sFecha,sFechaSaca,sPlaca;//Placas de Camion

    String sCodUser,sNomUser,sDniUser,sPerUser,sIdAndroid,sMensaje;
    //Recepcion de Aves

    Integer iDia,iMes,iAnio;
    String sDia,sMes;


    String miFecha,miTime,sMiApp,sMiVersion;

    Integer iCount=0;
    Integer iCountTipoPollo=0;
    Integer iCountDestinoPollo=0;
    Integer iCountPlacasCamion=0;
    Integer iCountUsuarios=0;

    hProcedimiento hP;
    static hMetodos hM;
    hVariables hV;
    dbEstructura dbE;
    hTransferenciaJson hTJ;

    String sNombreUsuario,sCodigoUsuario,sFechaActual;

    ConexionSQLiteHelper conn;
    SQLiteDatabase db;
    String sql,insertar;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    Integer iCouE,iCouI,iCouF;
    Integer iNumError=0;
    JsonObject jsonObjectDetalle;


    public BaseDatosExportacionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_datos_exportacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bg = FragmentBaseDatosExportacionBinding.bind(view);
        conn=new ConexionSQLiteHelper(getContext(), dbE.miBaseDatos,null,1);
        hP=new hProcedimiento(getContext(), dbE.miBaseDatos,null,1);
        hTJ=new hTransferenciaJson(getContext(), dbE.miBaseDatos,null,1);
        hM = new hMetodos();

        sFechaActual = hM.getfechaActual();

        sNombreUsuario=hP.getNombreUsuario();
        sCodigoUsuario=hP.getCodigoUsuario();

        bg.tvFecha.setText(sFechaActual);
        bg.tvNombreUsuario.setText(sNombreUsuario);


        jsonObjectDetalle = new JsonObject();

        requestQueue= Volley.newRequestQueue(getContext());
        progressDialog = new ProgressDialog(getContext());

        sSaltoLinea="\n";

        bg.tvExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sFecha=bg.tvFecha.getText().toString();
                if(sFecha.trim().length()<=0){
                    bg.tvRespuestaServidor.setText("Ingrese una fecha valida");

                }else {
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setTitle("Conectando al servidor");
                    progressDialog.setMessage("Guardado información...");
                    progressDialog.show();

                    sCadena="";
                    bg.tvRespuestaServidor.setText("");
                    sIdAndroid = hP.getIdAndroid();

                    sMiApp=getString(R.string.app_name);
                    sMiVersion=getString(R.string.sVersion);


                    hV.sMiIpDispositivo = hM.getIP();
                    hV.sMiIpConexion = hM.ipConexion(hV.sMiIpDispositivo);
                    hV.sMiURL = hV.sMiSeg + hV.sMiIpConexion + "/" + hV.sMiCarpeta + "/" + hV.phpExportar;


                    //exportarDatos(hV.sMiURL, sFecha, sIdAndroid);

                    AsyncTaskRecepcionAvesVivas asyncTaskRecepcionAvesVivas;
                    asyncTaskRecepcionAvesVivas= new AsyncTaskRecepcionAvesVivas(sFecha,hV.sMiURL);
                    asyncTaskRecepcionAvesVivas.execute("");

                }

            }
        });

        bg.tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c= Calendar.getInstance();
                iDia=c.get(Calendar.DAY_OF_MONTH);
                iMes=c.get(Calendar.MONTH);
                iAnio=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        sDia="00"+i2;
                        sDia = sDia.substring(Math.max(0, sDia.length() - 2));
                        sMes="00"+(i1+1);
                        sMes = sMes.substring(Math.max(0, sMes.length() - 2));
                        bg.tvFecha.setText(i+"-"+sMes+"-"+sDia);
                    }
                },iAnio,iMes,iDia);
                datePickerDialog.show();
            }
        });
    }

    public class AsyncTaskRecepcionAvesVivas extends AsyncTask<String,String,String> {
        String atPD_mensaje="";
        String atPD_data="";
        @Override
        protected void onPreExecute(){
            progressDialog.show();
        }
        String atFecha,atUrl;
        public AsyncTaskRecepcionAvesVivas(String xFecha,String xUrl) {
            atFecha=xFecha;
            atUrl=xUrl;
        }
        @Override
        protected String doInBackground(String... params){
            //Variables de Conexion
            iNumError=0;
            if (atFecha.trim().equals("")){
                atPD_mensaje="Ingrese una fecha valida";
            }
            else{
                JsonArray jsonArrayData = new JsonArray();
                //Todo: Codigo para guardar el Historial
                jsonObjectDetalle.addProperty("tabla", "t_bitacora_exportacion");
                jsonObjectDetalle.addProperty("tuser", sCodigoUsuario);
                jsonObjectDetalle.addProperty("tapp", sMiApp);
                jsonObjectDetalle.addProperty("tversion", sMiVersion);
                jsonObjectDetalle.addProperty("tidandroid", sIdAndroid);
                jsonObjectDetalle.addProperty("tfecpro", atFecha);
                jsonArrayData.add(jsonObjectDetalle);
                //Todo: Codigo para Transferir la Data
                String resp01[] =hTJ.crearDataGarita(atFecha,sCodigoUsuario,sIdAndroid,jsonArrayData);
                atPD_mensaje =atPD_mensaje+ resp01[0]+sSaltoLinea;
                atPD_data= resp01[1];
                iNumError=iNumError+Integer.parseInt(resp01[2]);

                String resp02[] =hTJ.crearDataMantenimiento(atFecha,sCodigoUsuario,sIdAndroid,jsonArrayData);
                atPD_mensaje =atPD_mensaje+ resp02[0]+sSaltoLinea;
                atPD_data= resp02[1];
                iNumError=iNumError+Integer.parseInt(resp02[2]);

                String resp03[] =hTJ.crearDataCercoElectrico(atFecha,sCodigoUsuario,sIdAndroid,jsonArrayData);
                atPD_mensaje =atPD_mensaje+ resp03[0]+sSaltoLinea;
                atPD_data= resp03[1];
                iNumError=iNumError+Integer.parseInt(resp03[2]);

                String resp04[] =hTJ.crearDataFomites(atFecha,sCodigoUsuario,sIdAndroid,jsonArrayData);
                atPD_mensaje =atPD_mensaje+ resp04[0]+sSaltoLinea;
                atPD_data= resp04[1];
                iNumError=iNumError+Integer.parseInt(resp04[2]);


                jsonArrayData = JsonParser.parseString(atPD_data).getAsJsonArray() ;//JsonParser.parseString(atPD_data);// JSON.parse(resp01[1]);

                //Todo: Codigo de Respuesta del Servidor
                String respServer[] =makeRequest(atUrl,jsonArrayData.toString());
                atPD_mensaje =atPD_mensaje+ respServer[0]+sSaltoLinea;
                iNumError=iNumError+Integer.parseInt(respServer[1]);

            }
            return atPD_mensaje;
        }
        @Override
        protected void onPostExecute(String r){
            progressDialog.dismiss();
            bg.tvRespuestaServidor.setText(r);
            if (iNumError>0){
                bg.tvRespuestaServidor.setTextColor(Color.parseColor("#ff0000"));
            }else{
                bg.tvRespuestaServidor.setTextColor(Color.parseColor("#008000"));
            }
        }

    }



    public static String[] makeRequest(String uri, String json) {
        HttpURLConnection urlConnection;
        String url;
        String data = json;
        String result = null;
        String sNumError="0";
        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(data);
            writer.close();
            outputStream.close();

            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                if  (line.contains("NUMERODEERRORES=")){//16
                    sNumError=hM.rightCadena(line.toString(),line.toString().length()-16);
                }else{
                    sb.append(line + "\n");//Respuesta del Servidor
                }
            }

            bufferedReader.close();
            result = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result=e.getMessage();
            sNumError="1";
        } catch (IOException e) {
            e.printStackTrace();
            result=e.getMessage();
            sNumError="1";
        }
        return  new String[]{result,sNumError};
    }
}