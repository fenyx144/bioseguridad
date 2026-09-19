package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Reportes;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper;
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentFomitesBinding;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentReporteGaritaBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReporteGaritaFragment extends Fragment {

    private FragmentReporteGaritaBinding bg;

    /*<!-- TODO: VARIABLES G -->*/
    String mifectra,sql,sFecha,sColaborador,sTurno,sFiltro,sAnomalia,sHora,sDescripcion,sAgenteCausal,sCencos;

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

    public ReporteGaritaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reporte_garita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bg = FragmentReporteGaritaBinding.bind(view);

        /*<!-- TODO: DATA -->*/
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
            String[] cadena = {"FECHA", "GRANJA", "COLABORADOR", "TURNO", "N°FILTRO", "TIPO DE ANOMALIA", "HORA", "DESCRIPCION", "AGENTE CAUSAL"};
            trFilas = new TableRow(getActivity().getBaseContext());
            for (int i = 0; i < 9; i++) {
                textView = new TextView(getActivity().getBaseContext());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextAppearance(getActivity(), R.style.estilo_celda);
                textView.setBackgroundResource(R.drawable.tabla_celda_cabecera);
                textView.setText(cadena[i]);
                trFilas.addView(textView);
            }
            bg.tlRepGarita.addView(trFilas);
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
            toast.show();
        }

        sFecha = bg.tvFecha.getText().toString();
        sCencos = bg.etCencos.getText().toString().substring(0, 6);
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
        int count = bg.tlRepGarita.getChildCount();
        for (int i=1;i<count;i++) {
            View child = bg.tlRepGarita.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        db = conn.getReadableDatabase();
        try{
            sql = "SELECT " + dbE.c_rgFecha + "," + dbE.c_rgCencos + "," + dbE.c_rgNomColaborador +
                    "," + dbE.c_rgTurno + "," + dbE.c_rgNumFiltro + "," + dbE.c_rgAnomalia +
                    "," + dbE.c_rgHora + "," + dbE.c_rgDescripcion + "," + dbE.c_rgAgenteCausal +
                    " FROM " + dbE.t_RGarita +
                    " WHERE " + dbE.c_rgFecha + "='" + xFecha + "' AND " + dbE.c_rgCencos + "='" + xCencos +
                    "'  ORDER BY " + dbE.c_rgFecha + "," + dbE.c_rgTurno;

            Cursor cursor = db.rawQuery(sql,null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    while (cursor.moveToNext()) {
                        sFecha = (cursor.getString(0));
                        sCencos = (cursor.getString(1));
                        sColaborador = (cursor.getString(2));
                        sTurno = (cursor.getString(3));
                        sFiltro = (cursor.getString(4));
                        sAnomalia = (cursor.getString(5));
                        sHora = (cursor.getString(6));
                        sDescripcion = (cursor.getString(7));
                        sAgenteCausal = (cursor.getString(8));

                        String[] cadena = {sFecha,sCencos,sColaborador,sTurno,sFiltro,sAnomalia,sHora,sDescripcion,sAgenteCausal};
                        trFilas = new TableRow(getActivity().getBaseContext());
                        for (int i= 0;i<9;i++) {
                            textView = new TextView(getActivity().getBaseContext());
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextAppearance(getActivity(), R.style.estilo_celda_detalle);
                            textView.setBackgroundResource(R.drawable.tabla_celda);
                            textView.setText(cadena[i]);
                            trFilas.addView(textView);
                        }
                        bg.tlRepGarita.addView(trFilas);
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