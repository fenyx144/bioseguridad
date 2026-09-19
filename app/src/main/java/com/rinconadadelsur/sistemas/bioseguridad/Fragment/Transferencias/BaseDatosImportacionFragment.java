package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transferencias;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper;
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eCencos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentBaseDatosImportacionBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseDatosImportacionFragment extends Fragment {

    private FragmentBaseDatosImportacionBinding bg;

    /*<!-- TODO: VARIABLES G -->*/
    Integer iPosiCen = -1;
    String xCencos,sCencos;

    /*<!-- TODO: PROCEDIMIENTOS -->*/
    String sNombreUsuario,sFechaActual,sIdAndroid;

    /*<!-- TODO: IMPORTACION -->*/
    String tempTabla,sSaltoLinea,sCadena,sCadenaUrl;
    Integer iCount=0;
    Integer iCountCencos=0;
    Integer iCountAnomalias=0;
    Integer iCountReferencias=0;
    Integer iCountProcesos=0;
    Integer iCountTipoFomites=0;
    Integer iCountGalpones=0;
    /*<!-- TODO: Cencos -->*/
    String sCodCcos,sNomCcos;
    /*<!-- TODO: Anomalías -->*/
    String sCodAnomalia,sAnomalia;
    /*<!-- TODO: Referencias -->*/
    String sCodRef,sReferencia;
    /*<!-- TODO: Procesos -->*/
    String sCodProc,sProceso;
    /*<!-- TODO: TipoFomites -->*/
    String sCodFom,sTipoFomites;
    /*<!-- TODO: Galpones -->*/
    String sCodCcosGal,sGalpon;

    /*<!-- TODO: CONEXION -->*/
    dbEstructura dbE;
    ConexionSQLiteHelper conn;
    SQLiteDatabase db;
    String insertar;

    /*<!-- TODO: VOLLEY -->*/
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    /*<!-- TODO: HERRAMIENTAS -->*/
    hProcedimiento hP;
    hMetodos hM;
    hVariables hV;

    /*<!-- TODO: LISTAS-ENTIDADES -->*/
    ArrayList<String> listaCencos;
    ArrayList<eCencos>cencosList;

    public BaseDatosImportacionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_datos_importacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bg = FragmentBaseDatosImportacionBinding.bind(view);

        /*<!-- TODO: DATA -->*/
        conn=new ConexionSQLiteHelper(getContext(), dbE.miBaseDatos,null,1);
        hP=new hProcedimiento(getContext(), dbE.miBaseDatos,null,1);
        hM = new hMetodos();

        /*<!-- TODO: PROCEDEMIENTOS -->*/
        sFechaActual = hM.getfechaActual();
        sNombreUsuario=hP.getNombreUsuario();

        /*<!-- TODO: ASIGNAR VARIABLES -->*/
        bg.tvFecha.setText(sFechaActual);
        bg.tvNombreUsuario.setText(sNombreUsuario);

        /*<!-- TODO: DESACTIVAR BOTONES AL INICIO -->*/
        bg.btnImportar.setEnabled(false);

        poblarCencos();
        sSaltoLinea="\n";

        requestQueue= Volley.newRequestQueue(getContext());
        progressDialog = new ProgressDialog(getContext());


        /*<!-- TODO: CAPTURA DE POSICION EXCLUYENDO LA PRIMERA  -->*/
        bg.spnCencos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position!=0){
                    iPosiCen = position-1;
                    bg.btnImportar.setEnabled(true);
                    xCencos=cencosList.get(iPosiCen).getC_cencos().substring(0,6);
                    sCencos=xCencos;
                }else{
                    iPosiCen = -1;
                    bg.btnImportar.setEnabled(false);
                }
            }
        });

        /*<!-- TODO: CLICK AL BOTON AGREGAR -->*/
        bg.btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle("Conectando al servidor");
                progressDialog.setMessage("Importando información...");
                progressDialog.show();

                bg.tvRespuestaServidor.setText("");
                sCadena="";
                sCadenaUrl="?codigo="+sCencos;

                hV.sMiIpDispositivo=hM.getIP();
                hV.sMiIpConexion=hM.ipConexion(hV.sMiIpDispositivo);
                hV.sMiURL=hV.sMiSeg+hV.sMiIpConexion+"/"+hV.sMiCarpeta+"/" +hV.phpImportar+sCadenaUrl ;
                importarDatos(hV.sMiURL);
            }
        });
    }

    private void poblarCencos() {
        db=conn.getReadableDatabase();
        eCencos cencos=null;
        cencosList=new ArrayList<eCencos>();
        try{
            insertar="SELECT "+ dbE.c_tcCodigo+","+ dbE.c_tcNombre+" FROM "+dbE.t_TCencos+" ORDER BY "+ dbE.c_tcCodigo;
            Cursor cursor=db.rawQuery(insertar,null);
            if (cursor != null) {
                listaCencos= new ArrayList<String>();
                listaCencos.add("Seleccione");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        cencos=new eCencos();
                        cencos.setC_cencos(cursor.getString(0));
                        cencos.setC_nombre(cursor.getString(1));
                        cencosList.add(cencos);
                    }

                    for(int i=0;i<cencosList.size();i++){
                        listaCencos.add(cencosList.get(i).getC_cencos()+" | "+cencosList.get(i).getC_nombre());
                    }

                    ArrayAdapter<CharSequence> adaptador= new ArrayAdapter(getContext(),R.layout.items_list,listaCencos);
                    bg.spnCencos.setAdapter(adaptador);
                    cursor.close();
                    db.close();
                }else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Cencos no encontrados" ,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }catch (Exception ex){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),ex.getMessage() ,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void importarDatos(String URL){
        String requestBody = "";

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest( URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    JSONObject jsonObject = null;
                    String sTabla,sMensaje;
                    db=conn.getWritableDatabase();

                    sMensaje="";
                    tempTabla="";
                    iCount=0;

                    iCountCencos=0;
                    iCountAnomalias=0;
                    iCountReferencias=0;
                    iCountProcesos=0;
                    iCountTipoFomites=0;
                    iCountGalpones=0;

                    for (int i = 0; i < response.length(); i++) {
                        iCount = iCount + 1;
                        jsonObject = response.getJSONObject(i);
                        sTabla=(jsonObject.getString("miTabla"));

                        if(sTabla.equals(tempTabla)){
                        }else{
                            tempTabla=sTabla;
                            iCount=1;
                        }

                        if (sTabla.equals("Cencos")){
                            iCountCencos=iCountCencos+1;
                            if (iCount==1){
                                db.delete(dbE.t_Cencos, null, null);
                            }

                            sCodCcos=(jsonObject.getString("codigo"));
                            sNomCcos=(jsonObject.getString("nombre"));

                            insertar="INSERT INTO "+dbE.t_Cencos+
                                    "("+dbE.c_cCodigo+","+dbE.c_cNombre+")"+
                                    "VALUES('"+sCodCcos+"','"+sNomCcos+"')";
                            db.execSQL(insertar);

                        } else if (sTabla.equals("Anomalia")){
                            iCountAnomalias=iCountAnomalias+1;
                            if (iCount==1){
                                db.delete(dbE.t_AnomaliaGarita, null, null);
                            }
                            sCodAnomalia =(jsonObject.getString("codigo"));
                            sAnomalia =(jsonObject.getString("descripcion"));

                            insertar="INSERT INTO "+dbE.t_AnomaliaGarita+
                                    "("+dbE.c_anCodigo+","+dbE.c_anDescripcion+")"+
                                    "VALUES('"+sCodAnomalia+"','"+sAnomalia+"')";
                            db.execSQL(insertar);
                        } else if (sTabla.equals("Referencia")){
                            iCountReferencias=iCountReferencias+1;
                            if (iCount==1){
                                db.delete(dbE.t_RefFomites, null, null);
                            }
                            sCodRef =(jsonObject.getString("codigo"));
                            sReferencia =(jsonObject.getString("descripcion"));

                            insertar="INSERT INTO "+dbE.t_RefFomites+
                                    "("+dbE.c_rfCodigo+","+dbE.c_rfDescripcion+")"+
                                    "VALUES('"+sCodRef+"','"+sReferencia+"')";
                            db.execSQL(insertar);
                        } else if (sTabla.equals("Proceso")){
                            iCountProcesos=iCountProcesos+1;
                            if (iCount==1){
                                db.delete(dbE.t_ProcesoFomites, null, null);
                            }
                            sCodProc =(jsonObject.getString("codigo"));
                            sProceso =(jsonObject.getString("descripcion"));

                            insertar="INSERT INTO "+dbE.t_ProcesoFomites+
                                    "("+dbE.c_pCodigo+","+dbE.c_pDescripcion+")"+
                                    "VALUES('"+sCodProc+"','"+sProceso+"')";
                            db.execSQL(insertar);
                        } else if (sTabla.equals("TipFomites")){
                            iCountTipoFomites=iCountTipoFomites+1;
                            if (iCount==1){
                                db.delete(dbE.t_TipoFomites, null, null);
                            }
                            sCodFom =(jsonObject.getString("codigo"));
                            sTipoFomites =(jsonObject.getString("descripcion"));

                            insertar="INSERT INTO "+dbE.t_TipoFomites+
                                    "("+dbE.c_tCodigo+","+dbE.c_tDescripcion+")"+
                                    "VALUES('"+sCodFom+"','"+sTipoFomites+"')";
                            db.execSQL(insertar);
                        } else if (sTabla.equals("Galpon")){
                            iCountGalpones=iCountGalpones+1;
                            if (iCount==1){
                                db.delete(dbE.t_CencosGalpon, null, null);
                            }
                            sCodCcosGal =(jsonObject.getString("tcencos"));
                            sGalpon =(jsonObject.getString("tcodint"));

                            insertar="INSERT INTO "+dbE.t_CencosGalpon+
                                    "("+dbE.c_cgCodigo+","+dbE.c_cgGalpon+")"+
                                    "VALUES('"+sCodCcosGal+"','"+sGalpon+"')";
                            db.execSQL(insertar);
                        } else if (sTabla.equals("ERROR")){
                            sMensaje =(jsonObject.getString("Mensaje"));
                        }
                    }
                    sCadena=sCadena+"Se han importado "+iCountCencos.toString()+" Cencos "+sSaltoLinea;
                    sCadena=sCadena+"Se han importado "+iCountAnomalias.toString()+" Anomalías "+sSaltoLinea;
                    sCadena=sCadena+"Se han importado "+iCountReferencias.toString()+" Referencias "+sSaltoLinea;
                    sCadena=sCadena+"Se han importado "+iCountProcesos.toString()+" Procesos "+sSaltoLinea;
                    sCadena=sCadena+"Se han importado "+iCountTipoFomites.toString()+" Tipo de Fomites "+sSaltoLinea;
                    sCadena=sCadena+"Se han importado "+iCountGalpones.toString()+" Galpones "+sSaltoLinea;

                    sCadena=sCadena+sMensaje+sSaltoLinea;
                    bg.tvRespuestaServidor.setText(sCadena);

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    sCadena=sCadena+e.getMessage()+sSaltoLinea;
                    bg.tvRespuestaServidor.setText(sCadena);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sCadena=sCadena+error.getMessage()+sSaltoLinea;
                bg.tvRespuestaServidor.setText(sCadena);
                progressDialog.dismiss();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }

}