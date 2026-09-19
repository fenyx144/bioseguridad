package com.rinconadadelsur.sistemas.bioseguridad.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.ActivityRegistrarDispositivoAndroidBinding;

import java.util.HashMap;
import java.util.Map;

public class RegistrarDispositivoAndroidActivity extends AppCompatActivity {

    ActivityRegistrarDispositivoAndroidBinding bg;

    String sMensaje="",sMensajeError="";
    String tempTabla,sSaltoLinea,sCadena,sCerrar;
    String sIdAndroid,sFabricante,sFamilia,sModelo,sDescripcion;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    Integer iCouE,iCouI,iCouF;

    hVariables hV;
    hMetodos hM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bg=ActivityRegistrarDispositivoAndroidBinding.inflate(getLayoutInflater());
        setContentView(bg.getRoot());

        sIdAndroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        sFabricante = Build.MANUFACTURER;//Xiaomi
        sFamilia = Build.BRAND;//Redmi
        sModelo = Build.MODEL;//mk

        sSaltoLinea="\n";
        bg.tietIdAndroid.setText(sIdAndroid);
        bg.tietFabricante.setText(sFabricante);
        bg.tietFamilia.setText(sFamilia);
        bg.tietModelo.setText(sModelo);

        requestQueue= Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);

        bg.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sDescripcion=bg.tietDescrpcion.getText().toString();

                if(sDescripcion.trim().length()<=0){
                    bg.tvRespuestaServidor.setText("Ingrese una descripcion valida");

                }else {
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setTitle("Conectando al servidor");
                    progressDialog.setMessage("Guardado información...");
                    progressDialog.show();

                    sCadena="";
                    bg.tvRespuestaServidor.setText("");


                    hV.sMiIpDispositivo = hM.getIP();
                    hV.sMiIpConexion = hM.ipConexion(hV.sMiIpDispositivo);
                    hV.sMiURL = hV.sMiSeg + hV.sMiIpConexion + "/" + hV.sMiCarpeta + "/" + hV.phpRegistrarDispositivo;


                    registrarDispositivo(hV.sMiURL,sDescripcion);
                }

            }
        });


    }

    private void registrarDispositivo(String xUrl,String xDescripcion){

        sMensaje="";
        iCouE=0;iCouI=0;iCouF=0;
        try{
            Map<String, String> parametros = new HashMap<String, String>();
            parametros.put("idandroid", sIdAndroid);
            parametros.put("fabricante", sFabricante);
            parametros.put("familia", sFamilia);
            parametros.put("modelo", sModelo);
            parametros.put("descripcion", xDescripcion);

            progressDialog.show();
            ProcessRequest(parametros,xUrl,sCerrar);

        }catch (Exception ex){
            sMensaje=ex.getMessage();
            sCadena=sCadena+sMensaje+sSaltoLinea;
            bg.tvRespuestaServidor.setText(sCadena);
            progressDialog.dismiss();
        }
    }

    private void ProcessRequest(final Map<String, String> parameters, String URL, String sCerrar) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("Existente")){
                    iCouE=iCouE+1;
                }else if (response.equals("Insertado")){
                    iCouI=iCouI+1;
                }else{
                    iCouF=iCouF+1;
                    sMensajeError=response;
                }
                if (iCouI>0) {
                    sCadena=sCadena+"Se han Insertado: "+iCouI.toString()+" Registros "+sSaltoLinea;
                }
                if (iCouE>0) {
                    if(response.contains("Estado Activo")){
                        sCadena = sCadena + "Dispositivo : " + iCouE.toString() + " Existente :Estado Activo " + sSaltoLinea;
                    }else{
                        sCadena = sCadena + "Dispositivo : " + iCouE.toString() + " Existente :Estado Inactivo " + sSaltoLinea;
                    }

                }
                if (iCouF>0) {
                    sCadena = sCadena + "No se pudieron Completar: " + iCouF.toString() + " Registros " + sSaltoLinea;
                    sCadena = sCadena + "Información: " + sMensajeError + sSaltoLinea;
                }
                sCadena=sCadena+sMensaje+sSaltoLinea;
                bg.tvRespuestaServidor.setText(sCadena);

                progressDialog.dismiss();
                if (iCouI>0) {
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sMensaje=error.getMessage().toString();
                sCadena=sCadena+sMensaje+sSaltoLinea;
                bg.tvRespuestaServidor.setText(sCadena);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return parameters;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                hV.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
}