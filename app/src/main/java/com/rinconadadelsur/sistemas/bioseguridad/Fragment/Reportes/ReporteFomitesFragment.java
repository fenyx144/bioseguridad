package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Reportes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper;
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.DevDataSeeder;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentReporteCercoBinding;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentReporteFomitesBinding;

import org.jetbrains.annotations.NotNull;

public class ReporteFomitesFragment extends Fragment {

    private FragmentReporteFomitesBinding bg;

    /*<!-- TODO: VARIABLES G -->*/
    String mifectra,sHora,sql,sObservacion,sDepredador,sTipo,sCantidad,sEstado,sFecha,sCencos,sTurno,sReferencia;
    String sProceso,sColaborador;

    /*<!-- TODO: VARIABLES CABECERA -->*/
    String sFechaActual,sNombreUsuario,sSerieDispositivo,sDoc,sSerie,sNumero;

    /*<!-- TODO: TABLA -->*/
    TableRow trFilas;
    TextView textView;

    /*<!-- TODO: HERRAMIENTAS -->*/
    hProcedimiento hP;
    hMetodos hM;
    hVariables hV;

    /*<!-- TODO: CONEXION -->*/
    dbEstructura dbE;
    ConexionSQLiteHelper conn;
    SQLiteDatabase db;
    String querys;


    public ReporteFomitesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reporte_fomites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bg = FragmentReporteFomitesBinding.bind(view);

        /*<!-- TODO: DATA -->*/
        dbE = new dbEstructura();
        DevDataSeeder.ensureDemoData(requireContext());
        hP = new hProcedimiento(getContext(), dbE.miBaseDatos, null, 1);
        conn = new hProcedimiento(getContext(), dbE.miBaseDatos, null, 1);
        hM = new hMetodos();
        hV = new hVariables();

        /*<!-- TODO: PROCEDEMIENTOS -->*/
        sFechaActual = hM.getfechaActual();
        sNombreUsuario = hP.getNombreUsuario();
        sSerieDispositivo = hP.getSerieDispositivo();

        /*<!-- TODO: ASIGNAR VARIABLES -->*/
        bg.tvFecha.setText(sFechaActual);

        buscarCencos();

        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/
        try {
            String[] cadena = {"FECHA", "GRANJA", "COLABORADOR", "TURNO", "REFERENCIA","PROCESO", "DEPREDADOR","TIPO", "CANTIDAD", "HORA", "¿DENTRO DEL CERCO?"};
            trFilas = new TableRow(getActivity().getBaseContext());
            for (int i = 0; i < 11; i++) {
                textView = new TextView(getActivity().getBaseContext());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextAppearance(getActivity(), R.style.estilo_celda);
                textView.setBackgroundResource(R.drawable.tabla_celda_cabecera);
                textView.setText(cadena[i]);
                trFilas.addView(textView);
            }
            bg.tlRepFomites.addView(trFilas);
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
            toast.show();
        }

        sFecha = bg.tvFecha.getText().toString();
        sCencos = DevDataSeeder.codigoCencos(bg.etCencos.getText().toString());
        mostrarTotales(sFecha, sCencos);

    }


    private void buscarCencos() {
        db=conn.getReadableDatabase();

        try{
            querys="SELECT "+ dbE.c_cCodigo+","+ dbE.c_cNombre+" FROM "+dbE.t_Cencos+" ";
            Cursor cursor=db.rawQuery(querys,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bg.etCencos.setText(cursor.getString(0)+" - " +cursor.getString(1));
                    }
                    cursor.close();
                }else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Código de la planta no existe", Toast.LENGTH_SHORT);
                    toast.show();
                    bg.etCencos.setText("");
                }
            }
        }catch (Exception ex){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),ex.getMessage() ,Toast.LENGTH_SHORT);
            toast.show();
            bg.etCencos.setText("");
        }
    }

    private void mostrarTotales(String xFecha,String xCencos){
        int count = bg.tlRepFomites.getChildCount();
        for (int i=1;i<count;i++) {
            View child = bg.tlRepFomites.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        db=conn.getReadableDatabase();
        try{
            sql = "SELECT " + dbE.c_rfFecha + "," + dbE.c_rfCencos + "," + dbE.c_rfNomColaborador +
                    "," + dbE.c_rfTurno + "," + dbE.c_rfReferencia + "," + dbE.c_rfProceso +
                    "," + dbE.c_rfDepredador + "," + dbE.c_rfTipo + "," + dbE.c_rfCantidad +
                    "," + dbE.c_rfHora + "," + dbE.c_rfDentroCerco +
                    " FROM " + dbE.t_RFomites +
                    " WHERE " + dbE.c_rfFecha + "='" + xFecha + "' AND " + dbE.c_rfCencos + "='" + xCencos +
                    "'  ORDER BY " + dbE.c_rfFecha + "," + dbE.c_rfTurno + "," + dbE.c_rfNomColaborador;
            Cursor cursor = db.rawQuery(sql,null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    while (cursor.moveToNext()) {
                        sFecha = (cursor.getString(0));
                        sCencos = (cursor.getString(1));
                        sColaborador = (cursor.getString(2));
                        sTurno = (cursor.getString(3));
                        sReferencia = (cursor.getString(4));
                        sProceso = (cursor.getString(5));
                        sDepredador = (cursor.getString(6));
                        sTipo = (cursor.getString(7));
                        sCantidad = (cursor.getString(8));
                        sHora = (cursor.getString(9));
                        sEstado = (cursor.getString(10));

                        String[] cadena = {sFecha,sCencos,sColaborador,sTurno,sReferencia,sProceso,sDepredador,sTipo,sCantidad,sHora,sEstado};
                        trFilas = new TableRow(getActivity().getBaseContext());
                        for (int i= 0;i<11;i++) {
                            textView = new TextView(getActivity().getBaseContext());
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextAppearance(getActivity(), R.style.estilo_celda_detalle);
                            textView.setBackgroundResource(R.drawable.tabla_celda);
                            textView.setText(cadena[i]);
                            trFilas.addView(textView);
                        }
                        bg.tlRepFomites.addView(trFilas);
                    }
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"REPORTE GENERADO CON EXITO!!",Toast.LENGTH_SHORT);
                    toast.show();
                    cursor.close();
                    db.close();
                }
            }
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}