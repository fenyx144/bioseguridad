package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transacciones;

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
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eAnomalias;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentCercoElectricoBinding;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentGaritaBinding;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentReporteCercoBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CercoElectricoFragment extends Fragment implements View.OnClickListener {

    private FragmentCercoElectricoBinding bg;

    /*<!-- TODO: VARIABLES G -->*/
    Integer iHora,iMinutos;
    Integer iId,iPosiTur = -1,iPosEli = -1;

    /*<!-- TODO: VARIABLES CABECERA -->*/
    String sFechaActual,sNombreUsuario,sSerieDispositivo,sDoc,sSerie,sNumero,miDate,miTime,insertar;

    String mifectra,sHora,sMinutos,sql,sEstadoDer,sEstadoIzq,sValidaReg,sId,sFecha,sCencos,sTurno,sEstadoD,sEstadoI,sEvaluador,sHoraI,sHoraF;
    String sEstado,sComponente,sDescripcion,sOtros,sDescripcionDer,sDescripcionIzq;


    /*<!-- TODO: VARIABLES G PARA MOSTRAR -->*/

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

    /*<!-- TODO: LISTAS-ENTIDADES -->*/


    public CercoElectricoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cerco_electrico, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bg = FragmentCercoElectricoBinding.bind(view);

        /*<!-- TODO: DATA -->*/
        hP = new hProcedimiento(getContext(), dbE.miBaseDatos, null, 1);
        conn = new hProcedimiento(getContext(), dbE.miBaseDatos, null, 1);
        hM = new hMetodos();
        hV = new hVariables();

        /*<!-- TODO: PROCEDEMIENTOS -->*/
        sFechaActual = hM.getfechaActual();
        sSerieDispositivo = hP.getSerieDispositivo();

        /*<!-- TODO: ASIGNAR VARIABLES -->*/
        bg.tvFecha.setText(sFechaActual);


        /*<!-- TODO: OCULTAR TABLAS -->*/



        buscarCencos();

        /*<!-- TODO: BOTONES -->*/
        //bg.swtEstadoDer.setOnClickListener(this);
        //bg.swtEstadoIzq.setOnClickListener(this);
        bg.swtArgolla.setOnClickListener(this);
        bg.swtBateria.setOnClickListener(this);
        bg.swtFocoTablero.setOnClickListener(this);
        bg.swtLineas.setOnClickListener(this);
        bg.swtPina.setOnClickListener(this);
        bg.swtSirena.setOnClickListener(this);
        bg.swtTablero.setOnClickListener(this);
        bg.swtTempladorAislador.setOnClickListener(this);
        bg.swtTransformador.setOnClickListener(this);
        bg.swtOtros.setOnClickListener(this);
        bg.btnAgregar.setOnClickListener(this);

        /*<!-- TODO: CARGA DE DATA SPINNER ARRAY -->*/
        List<String> listt = new ArrayList<>();
        listt.add("Dia");
        listt.add("Noche");
        ArrayAdapter<String> adaptert = new ArrayAdapter<String>(getContext(), R.layout.items_list, listt);
        adaptert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bg.spnTurno.setAdapter(adaptert);

        List<String> listt2 = new ArrayList<>();
        listt2.add("Operativo");
        listt2.add("Inoperativo");
        ArrayAdapter<String> adaptert2 = new ArrayAdapter<String>(getContext(), R.layout.items_list, listt2);
        adaptert2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bg.spnDerecha.setAdapter(adaptert2);
        bg.spnIzquierda.setAdapter(adaptert2);

        /*<!-- TODO: CAPTURA DE POSICION EXCLUYENDO LA PRIMERA -->*/
        bg.spnTurno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosiTur=position;
                sFecha = bg.tvFecha.getText().toString();
                sCencos = bg.etCencos.getText().toString().substring(0,6);
                mostrarTotales(sFecha, sCencos);
            }
        });


        /*<!-- TODO: GENERAR FORMATO PARA CAPTURA DE HORA -->*/
        bg.etHoraIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                iHora = c.get(Calendar.HOUR_OF_DAY);
                iMinutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sHora = "00"+hourOfDay;
                        sHora = sHora.substring(Math.max(0, sHora.length() - 2));
                        sMinutos = "00"+minute;
                        sMinutos = sMinutos.substring(Math.max(0, sMinutos.length() - 2));
                        bg.etHoraIni.setText(sHora+":"+sMinutos);
                    }
                },iHora,iMinutos,true);
                timePickerDialog.show();
            }
        });

        bg.etHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                iHora = c.get(Calendar.HOUR_OF_DAY);
                iMinutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sHora = "00"+hourOfDay;
                        sHora = sHora.substring(Math.max(0, sHora.length() - 2));
                        sMinutos = "00"+minute;
                        sMinutos = sMinutos.substring(Math.max(0, sMinutos.length() - 2));
                        bg.etHoraFin.setText(sHora+":"+sMinutos);
                    }
                },iHora,iMinutos,true);
                timePickerDialog.show();
            }
        });


        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/

        try {
            String[] cadena1 = {"ID","Fecha","Turno","Est-Derecho","Est-Izquierdo","Evaluador","Componente","Descripción","Falló?","Inicio","Fin"};
            trFilas = new TableRow(getActivity().getBaseContext());
            TextView textView;
            for (int i=0;i<11;i++) {
                textView = new TextView(getActivity().getBaseContext());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextAppearance(getActivity(), R.style.estilo_celda);
                textView.setBackgroundResource(R.drawable.tabla_celda_cabecera);
                textView.setText(cadena1[i]);
                trFilas.addView(textView);
            }
            bg.tlRegCercoElectrico.addView(trFilas);
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
            toast.show();
        }


        /*<!-- TODO: AL INGRESAR UN DATO -->*/

        bg.spnTurno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bg.tvTurno.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /*<!-- TODO: DESACTIVAR LOS CAMPOS CON SWITCHS -->*/
        bg.etArgolla.setEnabled(false);
        bg.etBateria.setEnabled(false);
        bg.etFocoTablero.setEnabled(false);
        bg.etLineas.setEnabled(false);
        bg.etPina.setEnabled(false);
        bg.etSirena.setEnabled(false);
        bg.etTablero.setEnabled(false);
        bg.etTempladorAislador.setEnabled(false);
        bg.etTransformador.setEnabled(false);
        bg.etOtros.setEnabled(false);
        bg.etDescOtros.setEnabled(false);

    }

    private void inicializarCampos() {
        bg.spnTurno.setSelection(0);
        bg.spnTurno.setEnabled(true);
        for (int i=1;i<=12; i++) {
            switch (i) {
                case 3:
                    sComponente = "Argolla";
                    sEstado = bg.swtArgolla.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etArgolla.setText("");
                        bg.etArgolla.setEnabled(false);
                        bg.swtArgolla.setChecked(false);
                        bg.swtArgolla.setText("No");
                    }
                    break;
                case 4:
                    sComponente = "Bateria";
                    sEstado = bg.swtBateria.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etBateria.setText("");
                        bg.etBateria.setEnabled(false);
                        bg.swtBateria.setChecked(false);
                        bg.swtBateria.setText("No");
                    }
                    break;
                case 5:
                    sComponente = "Foco de Tablero";
                    sEstado = bg.swtFocoTablero.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etFocoTablero.setText("");
                        bg.etFocoTablero.setEnabled(false);
                        bg.swtFocoTablero.setChecked(false);
                        bg.swtFocoTablero.setText("No");
                    }
                    break;
                case 6:
                    sComponente = "Lineas";
                    sEstado = bg.swtLineas.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etLineas.setText("");
                        bg.etLineas.setEnabled(false);
                        bg.swtLineas.setChecked(false);
                        bg.swtLineas.setText("No");
                    }
                    break;
                case 7:
                    sComponente = "Piña";
                    sEstado = bg.swtPina.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etPina.setText("");
                        bg.etPina.setEnabled(false);
                        bg.swtPina.setChecked(false);
                        bg.swtPina.setText("No");
                    }
                    break;
                case 8:
                    sComponente = "Sirena";
                    sEstado = bg.swtSirena.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etSirena.setText("");
                        bg.etSirena.setEnabled(false);
                        bg.swtSirena.setChecked(false);
                        bg.swtSirena.setText("No");
                    }
                    break;
                case 9:
                    sComponente = "Tablero";
                    sEstado = bg.swtTablero.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etTablero.setText("");
                        bg.etTablero.setEnabled(false);
                        bg.swtTablero.setChecked(false);
                        bg.swtTablero.setText("No");
                    }
                    break;
                case 10:
                    sComponente = "Templador Aislador";
                    sEstado = bg.swtTempladorAislador.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etTempladorAislador.setText("");
                        bg.etTempladorAislador.setEnabled(false);
                        bg.swtTempladorAislador.setChecked(false);
                        bg.swtTempladorAislador.setText("No");
                    }
                    break;
                case 11:
                    sComponente = "Transformador";
                    sEstado = bg.swtTransformador.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etTransformador.setText("");
                        bg.etTransformador.setEnabled(false);
                        bg.swtTransformador.setChecked(false);
                        bg.swtTransformador.setText("No");
                    }
                    break;
                case 12:
                    sComponente = "Otros";
                    sEstado = bg.swtOtros.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etOtros.setText("");
                        bg.etOtros.setEnabled(false);
                        bg.etDescOtros.setText("");
                        bg.etDescOtros.setEnabled(false);
                        bg.swtOtros.setChecked(false);
                        bg.swtOtros.setText("No");
                    }
                    break;
            }
        }

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

    private void guardarDatos(){
        sFecha = bg.tvFecha.getText().toString();
        sCencos = bg.etCencos.getText().toString().substring(0,6);
        sTurno = bg.spnTurno.getText().toString();
        sEvaluador = hP.getNombreUsuario();//bg.tvNombreUsuario.getText().toString();

        db = conn.getWritableDatabase();
        sql = "DELETE FROM " + dbE.t_RCercoElectrico+ " WHERE "+ dbE.c_rcFecha + "='" + sFecha+ "' AND "+ dbE.c_rcTurno + "='"+sTurno+"'";
        db.execSQL(sql);
        db.close();

        db = conn.getWritableDatabase();
        //Todo:MAXIMO REGISTRO
        try{
            sql = "SELECT MAX(ABS(" + dbE.c_rcId + "))  FROM " + dbE.t_RCercoElectrico + " WHERE " + dbE.c_rcFecha + "='" + sFecha + "';";
            Cursor cursor1 = db.rawQuery(sql,null);
            if (cursor1!=null) {
                if (cursor1.getCount()>0) {
                    while (cursor1.moveToNext()) {
                        iId = cursor1.getInt(0);
                    }
                    cursor1.close();
                }else{
                    iId = 0;
                }
            }

            //Todo:Validamos campos vacios de Estado:


            //Todo:Agregamos campos generales a la Tabla




            sHoraI = bg.etHoraIni.getText().toString();
            sHoraF = bg.etHoraFin.getText().toString();


            for (int i=1;i<=10; i++) {
                switch (i) {
                    case 1:
                        sComponente = "Argolla";
                        sEstado = bg.swtArgolla.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtArgolla.getText().toString();
                            sDescripcion = bg.etArgolla.getText().toString();
                        }
                        break;
                    case 2:
                        sComponente = "Bateria";
                        sEstado = bg.swtBateria.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtBateria.getText().toString();
                            sDescripcion = bg.etBateria.getText().toString();
                        }
                        break;
                    case 3:
                        sComponente = "Foco de Tablero";
                        sEstado = bg.swtFocoTablero.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtFocoTablero.getText().toString();
                            sDescripcion = bg.etFocoTablero.getText().toString();
                        }
                        break;
                    case 4:
                        sComponente = "Lineas";
                        sEstado = bg.swtLineas.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtLineas.getText().toString();
                            sDescripcion = bg.etLineas.getText().toString();
                        }
                        break;
                    case 5:
                        sComponente = "Piña";
                        sEstado = bg.swtPina.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtPina.getText().toString();
                            sDescripcion = bg.etPina.getText().toString();
                        }
                        break;
                    case 6:
                        sComponente = "Sirena";
                        sEstado = bg.swtSirena.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtSirena.getText().toString();
                            sDescripcion = bg.etSirena.getText().toString();
                        }
                        break;
                    case 7:
                        sComponente = "Tablero";
                        sEstado = bg.swtTablero.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtTablero.getText().toString();
                            sDescripcion = bg.etTablero.getText().toString();
                        }
                        break;
                    case 8:
                        sComponente = "Templador Aislador";
                        sEstado = bg.swtTempladorAislador.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtTempladorAislador.getText().toString();
                            sDescripcion = bg.etTempladorAislador.getText().toString();
                        }
                        break;
                    case 9:
                        sComponente = "Transformador";
                        sEstado = bg.swtTransformador.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sEstado = bg.swtTransformador.getText().toString();
                            sDescripcion = bg.etTransformador.getText().toString();
                        }
                        break;
                    case 10:
                        if(bg.etOtros.getText().toString().equals("")){
                            sValidaReg = "Sin Dato";
                        }else {
                            sOtros = bg.etOtros.getText().toString();
                            sComponente = sOtros;
                            sEstado = bg.swtOtros.getText().toString();
                            sValidaReg = "Dato";
                            if (sEstado.equals("")) {
                                sValidaReg = "Sin Dato";
                            } else {
                                sEstado = bg.swtOtros.getText().toString();
                                sDescripcion = bg.etDescOtros.getText().toString();
                            }
                        }
                        break;
                }
                if (sValidaReg.equals("Dato")){
                    String CodUser=hP.getCodigoUsuario();
                    try {
                        iId = iId + 1;
                        miTime= hM.gethoraActual();
                        miDate = hM.getfechaActual();
                        sDescripcionDer = bg.etMOtivoDer.getText().toString();
                        sDescripcionIzq = bg.etMOtivoIzq.getText().toString();
                        sEstadoD= bg.spnDerecha.getText().toString();
                        sEstadoI= bg.spnIzquierda.getText().toString();

                        insertar = "INSERT INTO " + dbE.t_RCercoElectrico +
                                "(" + dbE.c_rcId + "," + dbE.c_rcUsuario + "," + dbE.c_rcDate +
                                "," + dbE.c_rcTime + "," + dbE.c_rcFecha + "," + dbE.c_rcCencos +
                                "," + dbE.c_rcTurno + "," + dbE.c_rcEstadoDerecho + "," + dbE.c_rcDescEstadoDer +
                                "," + dbE.c_rcEstadoIzquierdo + "," + dbE.c_rcDescEstadoIzq + "," + dbE.c_rcNomEvaluador +
                                "," + dbE.c_rcHoraInicio + "," + dbE.c_rcHoraFinal + "," + dbE.c_rcComponente +
                                "," + dbE.c_rcFallo + "," + dbE.c_rcDescripcion + ")" +
                                "VALUES('" + iId.toString() + "','" + CodUser  + "','" + miDate +
                                "','" + miTime + "','" + sFecha + "','" + sCencos +
                                "','" + sTurno + "','" + sEstadoD + "','" + sDescripcionDer +
                                "','" + sEstadoI + "','" + sDescripcionIzq + "','"+ sEvaluador +
                                "','" + sHoraI + "','" + sHoraF + "','" + sComponente +
                                "','" + sEstado + "','" + sDescripcion + "')";
                        db.execSQL(insertar);
                    } catch (Exception ex) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                        db.close();
                    }
                }
            }
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),e.getMessage() ,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void mostrarTotales(String xFecha,String xCencos){
        int count = bg.tlRegCercoElectrico.getChildCount();
        for (int i=1;i<count;i++) {
            View child = bg.tlRegCercoElectrico.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        db = conn.getReadableDatabase();
        //FILTRAR DATA DE TABLA
        sTurno = bg.spnTurno.getText().toString();
        try{
            sql = "SELECT " + dbE.c_rcId + "," + dbE.c_rcFecha + "," + dbE.c_rcTurno +
                    "," + dbE.c_rcEstadoDerecho + "," + dbE.c_rcEstadoIzquierdo + "," + dbE.c_rcNomEvaluador +
                    "," + dbE.c_rcComponente + "," + dbE.c_rcDescripcion + "," + dbE.c_rcFallo +
                    "," + dbE.c_rcHoraInicio + "," + dbE.c_rcHoraFinal +
                    " FROM " + dbE.t_RCercoElectrico +
                    " WHERE " + dbE.c_rcFecha + "='" + xFecha + "' AND " + dbE.c_rcCencos + "='" + xCencos +"' AND " + dbE.c_rcTurno + "='" + sTurno +
                    "'  ORDER BY " + dbE.c_rcFecha + "," + dbE.c_rcCencos + "," + dbE.c_rcTurno;

            Cursor cursor = db.rawQuery(sql,null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0));
                        sFecha = (cursor.getString(1));
                        sTurno = (cursor.getString(2));
                        sEstadoD = (cursor.getString(3));
                        sEstadoI = (cursor.getString(4));
                        sEvaluador = (cursor.getString(5));
                        sComponente = (cursor.getString(6));
                        sDescripcion = (cursor.getString(7));
                        sEstado = (cursor.getString(8));
                        sHoraI = (cursor.getString(9));
                        sHoraF = (cursor.getString(10));

                        String[] cadena = {sId,sFecha,sTurno, sEstadoD,sEstadoI,sEvaluador,sComponente,sDescripcion,sEstado,sHoraI,sHoraF};
                        trFilas = new TableRow(getActivity().getBaseContext());
                        for (int i= 0;i<11;i++) {
                            textView = new TextView(getActivity().getBaseContext());
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextAppearance(getActivity(), R.style.estilo_celda_detalle);
                            textView.setBackgroundResource(R.drawable.tabla_celda);
                            textView.setText(cadena[i]);
                            trFilas.addView(textView);
                        }
                        bg.tlRegCercoElectrico.addView(trFilas);
                    }
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Carga de datos exitosa",Toast.LENGTH_SHORT);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

              
            case R.id.swtArgolla:
                if (bg.swtArgolla.isChecked()) {bg.swtArgolla.setText("Si");bg.etArgolla.setEnabled(true);}else{bg.swtArgolla.setText("No");bg.etArgolla.setEnabled(false);bg.etArgolla.setText("");}
                break;
            case R.id.swtBateria:
                if (bg.swtBateria.isChecked()) {bg.swtBateria.setText("Si");bg.etBateria.setEnabled(true);}else{bg.swtBateria.setText("No");bg.etBateria.setEnabled(false);bg.etBateria.setText("");}
                break;
            case R.id.swtFocoTablero:
                if (bg.swtFocoTablero.isChecked()) {bg.swtFocoTablero.setText("Si");bg.etFocoTablero.setEnabled(true);}else{bg.swtFocoTablero.setText("No");bg.etFocoTablero.setEnabled(false);bg.etFocoTablero.setText("");}
                break;
            case R.id.swtLineas:
                if (bg.swtLineas.isChecked()) {bg.swtLineas.setText("Si");bg.etLineas.setEnabled(true);}else{bg.swtLineas.setText("No");bg.etLineas.setEnabled(false);bg.etLineas.setText("");}
                break;
            case R.id.swtPina:
                if (bg.swtPina.isChecked()) {bg.swtPina.setText("Si");bg.etPina.setEnabled(true);}else{bg.swtPina.setText("No");bg.etPina.setEnabled(false);bg.etPina.setText("");}
                break;
            case R.id.swtSirena:
                if (bg.swtSirena.isChecked()) {bg.swtSirena.setText("Si");bg.etSirena.setEnabled(true);}else{bg.swtSirena.setText("No");bg.etSirena.setEnabled(false);bg.etSirena.setText("");}
                break;
            case R.id.swtTablero:
                if (bg.swtTablero.isChecked()) {bg.swtTablero.setText("Si");bg.etTablero.setEnabled(true);}else{bg.swtTablero.setText("No");bg.etTablero.setEnabled(false);bg.etTablero.setText("");}
                break;
            case R.id.swtTempladorAislador:
                if (bg.swtTempladorAislador.isChecked()) {bg.swtTempladorAislador.setText("Si");bg.etTempladorAislador.setEnabled(true);}else{bg.swtTempladorAislador.setText("No");bg.etTempladorAislador.setEnabled(false);bg.etTempladorAislador.setText("");}
                break;
            case R.id.swtTransformador:
                if (bg.swtTransformador.isChecked()) {bg.swtTransformador.setText("Si");bg.etTransformador.setEnabled(true);}else{bg.swtTransformador.setText("No");bg.etTransformador.setEnabled(false);bg.etTransformador.setText("");}
                break;
            case R.id.swtOtros:
                if (bg.swtOtros.isChecked()) {bg.swtOtros.setText("Si");bg.etOtros.setEnabled(true);bg.etDescOtros.setEnabled(true);}else{bg.swtOtros.setText("No");bg.etOtros.setEnabled(false);bg.etOtros.setText("");bg.etDescOtros.setEnabled(false);bg.etDescOtros.setText("");}
                break;
            case R.id.btnAgregar:
                sFecha = bg.tvFecha.getText().toString();
                sCencos = bg.etCencos.getText().toString().substring(0,6);
                if (iPosiTur==-1 ){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Falta seleccionar Turno!!" ,Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.etHoraIni.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta elegir la Hora Inicial!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.etHoraFin.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta elegir la Hora Final!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtArgolla.getText().toString().equals("Si")&&(bg.etArgolla.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtBateria.getText().toString().equals("Si")&&(bg.etBateria.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtFocoTablero.getText().toString().equals("Si")&&(bg.etFocoTablero.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtLineas.getText().toString().equals("Si")&&(bg.etLineas.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtPina.getText().toString().equals("Si")&&(bg.etPina.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtSirena.getText().toString().equals("Si")&&(bg.etSirena.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtTablero.getText().toString().equals("Si")&&(bg.etTablero.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtTempladorAislador.getText().toString().equals("Si")&&(bg.etTempladorAislador.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtTransformador.getText().toString().equals("Si")&&(bg.etTransformador.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtOtros.getText().toString().equals("Si")&&(bg.etDescOtros.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    String sGuardar="Si";
                    if (bg.spnIzquierda.getText().toString().equals("Seleccione")){
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta Escoger Estado Izquierdo!!", Toast.LENGTH_SHORT);
                        toast.show();
                        sGuardar="No";
                    }
                    if (bg.spnDerecha.getText().toString().equals("Seleccione")){
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta Escoger Estado Derecho!!", Toast.LENGTH_SHORT);
                        toast.show();
                        sGuardar="No";
                    }
                    if (sGuardar.equals("Si")) {
                        guardarDatos();
                        mostrarTotales(sFecha, sCencos);
                        inicializarCampos();
                    }
                }
        }
    }



}