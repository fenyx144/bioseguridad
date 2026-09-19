package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transacciones;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eRegEliminar;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentMantenimientoBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MantenimientoFragment extends Fragment implements View.OnClickListener {

    private FragmentMantenimientoBinding bg;

    /*<!-- TODO: VARIABLES G -->*/
    Integer iId,iPosiTur=-1,iPosEli = -1;

    /*<!-- TODO: VARIABLES CABECERA -->*/
    String sFechaActual,sNombreUsuario,sSerieDispositivo,sDoc,sSerie,sNumero;

    String sql,sColaborador,miDate,miTime;;
    String sDescripcion,sEstado,sValidaReg,sId,sFecha,sCencos,sElemento,sTurno,sOtros;


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

    ArrayList<String> listaRegEliminar;
    ArrayList<eRegEliminar>regEliminarList;


    /*<!-- TODO: LISTAS-ENTIDADES -->*/


    public MantenimientoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mantenimiento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bg = FragmentMantenimientoBinding.bind(view);

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

        buscarCencos();

        /*<!-- TODO: BOTONES -->*/
        bg.swtCajaFormol.setOnClickListener(this);
        bg.swtComedor.setOnClickListener(this);
        bg.swtDucha.setOnClickListener(this);
        bg.swtEstructuraGarita.setOnClickListener(this);
        bg.swtMedidorAgua.setOnClickListener(this);
        bg.swtPuertaPersonal.setOnClickListener(this);
        bg.swtPuertaPozoSeptico.setOnClickListener(this);
        bg.swtPuertaVehicular.setOnClickListener(this);
        bg.swtSSHH.setOnClickListener(this);
        bg.swtTableroControl.setOnClickListener(this);
        bg.swtTachoRopa.setOnClickListener(this);
        bg.swtTherma.setOnClickListener(this);
        bg.swtTuberiaAgua.setOnClickListener(this);
        bg.swtOtros.setOnClickListener(this);
        bg.btnAgregar.setOnClickListener(this);

        /*<!-- TODO: CARGA DE DATA SPINNER ARRAY -->*/
        List<String> listt = new ArrayList<>();
        //listt.add("Seleccione");
        listt.add("Dia");
        listt.add("Noche");
        ArrayAdapter<String> adaptert = new ArrayAdapter<String>(getContext(), R.layout.items_list, listt);
        adaptert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bg.spnTurno.setAdapter(adaptert);



        /*<!-- TODO: CAPTURA DE POSICION EXCLUYENDO LA PRIMERA -->*/
        bg.spnTurno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosiTur=position;
            }
        });

        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/
        try {
            String[] cadena = {"ID","Fecha","Turno","Colaborador","Elemento","¿Fallo?","Descripcion"};
            trFilas = new TableRow(getActivity().getBaseContext());
            for (int i=0;i<7;i++) {
                textView = new TextView(getActivity().getBaseContext());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextAppearance(getActivity(), R.style.estilo_celda);
                textView.setBackgroundResource(R.drawable.tabla_celda_cabecera);
                textView.setText(cadena[i]);
                trFilas.addView(textView);
            }
            bg.tlRegMantenimiento.addView(trFilas);
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
            toast.show();
        }
        /*<!-- TODO: DESACTIVAR LOS CAMPOS CON SWITCHS -->*/
        bg.etCajaFormol.setEnabled(false);
        bg.etComedor.setEnabled(false);
        bg.etDucha.setEnabled(false);
        bg.etEstructuraGarita.setEnabled(false);
        bg.etMedidorAgua.setEnabled(false);
        bg.etPuertaPersonal.setEnabled(false);
        bg.etPuertaPozoSeptico.setEnabled(false);
        bg.etPuertaVehicular.setEnabled(false);
        bg.etSSHH.setEnabled(false);
        bg.etTableroControl.setEnabled(false);
        bg.etTachoRopa.setEnabled(false);
        bg.etTherma.setEnabled(false);
        bg.etTuberiaAgua.setEnabled(false);
        bg.etOtros.setEnabled(false);
        bg.etDescOtros.setEnabled(false);


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

        bg.spnTurno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                sFecha = bg.tvFecha.getText().toString();
                sCencos = bg.etCencos.getText().toString().substring(0,6);
                iPosiTur=position;
                mostrarTotales(sFecha,sCencos);
                listaItems(sFecha,sCencos);
            }
        });

        bg.spnEliminar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosEli=position;
                bg.btnEliminar.setEnabled(true);
            }
        });

        bg.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg.btnEliminar.setEnabled(false);


                androidx.appcompat.app.AlertDialog.Builder dialogo1 = new AlertDialog.Builder(view.getContext());//this
                dialogo1.setTitle("Eliminar");
                dialogo1.setMessage("¿ Desea Eliminar dicho registro ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast toast;
                        if (iPosEli == -1|| bg.spnEliminar.getText().toString().equals("")) {
                            toast = Toast.makeText(getActivity().getApplicationContext(), "Item Seleccionado No valido", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            //Eliminar Registro
                            String slFecha=bg.tvFecha.getText().toString();
                            String slCencos=bg.etCencos.getText().toString().substring(0,6);
                            String slId=regEliminarList.get(iPosEli).get_id();

                            String rMensaje=hP.getElimarRegistro(dbE.t_RMantenimiento,dbE.c_rmFecha,slFecha,dbE.c_rmCencos,slCencos,dbE.c_rmId,slId);
                            toast = Toast.makeText(getActivity().getApplicationContext(), rMensaje, Toast.LENGTH_SHORT);
                            toast.show();


                            sFecha = bg.tvFecha.getText().toString();
                            sCencos = bg.etCencos.getText().toString().substring(0, 6);
                            mostrarTotales(sFecha, sCencos);
                            listaItems(sFecha, sCencos);


                        }
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //cancelar();
                    }
                });
                dialogo1.show();
            }
        });

    }

    private void inicializarCampos() {
        bg.spnTurno.setText("Seleccione");
        for (int i=1;i<=14; i++) {
            switch (i) {
                case 1:
                    sElemento = "Caja de Formol";
                    sEstado = bg.swtCajaFormol.getText().toString();
                    if (sEstado.equals("Si")) {
                        bg.etCajaFormol.setText("");
                        bg.etCajaFormol.setEnabled(false);
                        bg.swtCajaFormol.setChecked(false);
                        bg.swtCajaFormol.setText("No");
                    }
                    break;
                case 2:
                    sElemento = "Comedor";
                    sEstado = bg.swtComedor.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etComedor.setText("");
                        bg.etComedor.setEnabled(false);
                        bg.swtComedor.setChecked(false);
                        bg.swtComedor.setText("No");
                    }
                    break;
                case 3:
                    sElemento = "Ducha";
                    sEstado = bg.swtDucha.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etDucha.setText("");
                        bg.etDucha.setEnabled(false);
                        bg.swtDucha.setChecked(false);
                        bg.swtDucha.setText("No");
                    }
                    break;
                case 4:
                    sElemento = "Estructura de Garita";
                    sEstado = bg.swtEstructuraGarita.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etEstructuraGarita.setText("");
                        bg.etEstructuraGarita.setEnabled(false);
                        bg.swtEstructuraGarita.setChecked(false);
                        bg.swtEstructuraGarita.setText("No");
                    }
                    break;
                case 5:
                    sElemento = "Medidor de Agua";
                    sEstado = bg.swtMedidorAgua.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etMedidorAgua.setText("");
                        bg.etMedidorAgua.setEnabled(false);
                        bg.swtMedidorAgua.setChecked(false);
                        bg.swtMedidorAgua.setText("No");
                    }
                    break;
                case 6:
                    sElemento = "Puerta de Personal";
                    sEstado = bg.swtPuertaPersonal.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etPuertaPersonal.setText("");
                        bg.etPuertaPersonal.setEnabled(false);
                        bg.swtPuertaPersonal.setChecked(false);
                        bg.swtPuertaPersonal.setText("No");
                    }
                    break;
                case 7:
                    sElemento = "Puerta Pozo Septico";
                    sEstado = bg.swtPuertaPozoSeptico.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etPuertaPozoSeptico.setText("");
                        bg.etPuertaPozoSeptico.setEnabled(false);
                        bg.swtPuertaPozoSeptico.setChecked(false);
                        bg.swtPuertaPozoSeptico.setText("No");
                    }
                    break;
                case 8:
                    sElemento = "Puerta Vehicular";
                    sEstado = bg.swtPuertaVehicular.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etPuertaVehicular.setText("");
                        bg.swtPuertaVehicular.setEnabled(false);
                        bg.swtPuertaVehicular.setChecked(false);
                        bg.swtPuertaVehicular.setText("No");
                    }
                    break;
                case 9:
                    sElemento = "SS.HH";
                    sEstado = bg.swtSSHH.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etSSHH.setText("");
                        bg.etSSHH.setEnabled(false);
                        bg.swtSSHH.setChecked(false);
                        bg.swtSSHH.setText("No");
                    }
                    break;
                case 10:
                    sElemento = "Tablero de Control";
                    sEstado = bg.swtTableroControl.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etTableroControl.setText("");
                        bg.etTableroControl.setEnabled(false);
                        bg.swtTableroControl.setChecked(false);
                        bg.swtTableroControl.setText("No");
                    }
                    break;
                case 11:
                    sElemento = "Tachos de Ropa";
                    sEstado = bg.swtTachoRopa.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etTachoRopa.setText("");
                        bg.etTachoRopa.setEnabled(false);
                        bg.swtTachoRopa.setChecked(false);
                        bg.swtTachoRopa.setText("No");
                    }
                    break;
                case 12:
                    sElemento = "Therma";
                    sEstado = bg.swtTherma.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etTherma.setText("");
                        bg.etTherma.setEnabled(false);
                        bg.swtTherma.setChecked(false);
                        bg.swtTherma.setText("No");
                    }
                    break;
                case 13:
                    sElemento = "Tuberia de Agua";
                    sEstado = bg.swtTuberiaAgua.getText().toString();
                    if (sEstado.equals("Si")){
                        bg.etTuberiaAgua.setText("");
                        bg.etTuberiaAgua.setEnabled(false);
                        bg.swtTuberiaAgua.setChecked(false);
                        bg.swtTuberiaAgua.setText("No");
                    }
                    break;
                case 14:
                    sElemento = "Otros";
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
        db = conn.getWritableDatabase();

        //Todo:MAXIMO REGISTRO
        try{
            sql = "SELECT MAX(ABS(" + dbE.c_rmId + "))  FROM " + dbE.t_RMantenimiento + " WHERE " + dbE.c_rmFecha + "='" + bg.tvFecha.getText().toString() + "';";
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

            //Todo:Agregamos campos generales a la Tabla

            sFecha = bg.tvFecha.getText().toString();
            sCencos = bg.etCencos.getText().toString().substring(0,6);
            sTurno = bg.spnTurno.getText().toString();
            sColaborador = hP.getNombreUsuario();
            for (int i=1;i<=14; i++) {
                switch (i) {
                    case 1:
                        sElemento = "Caja de Formol";
                        sEstado = bg.swtCajaFormol.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etCajaFormol.getText().toString();
                        }
                        break;
                    case 2:
                        sElemento = "Comedor";
                        sEstado = bg.swtComedor.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etComedor.getText().toString();
                        }
                        break;
                    case 3:
                        sElemento = "Ducha";
                        sEstado = bg.swtDucha.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etDucha.getText().toString();
                        }
                        break;
                    case 4:
                        sElemento = "Estructura de Garita";
                        sEstado = bg.swtEstructuraGarita.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etEstructuraGarita.getText().toString();
                        }
                        break;
                    case 5:
                        sElemento = "Medidor de Agua";
                        sEstado = bg.swtMedidorAgua.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etMedidorAgua.getText().toString();
                        }
                        break;
                    case 6:
                        sElemento = "Puerta de Personal";
                        sEstado = bg.swtPuertaPersonal.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etPuertaPersonal.getText().toString();
                        }
                        break;
                    case 7:
                        sElemento = "Puerta Pozo Septico";
                        sEstado = bg.swtPuertaPozoSeptico.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etPuertaPozoSeptico.getText().toString();
                        }
                        break;
                    case 8:
                        sElemento = "Puerta Vehicular";
                        sEstado = bg.swtPuertaVehicular.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etPuertaVehicular.getText().toString();
                        }
                        break;
                    case 9:
                        sElemento = "SS.HH";
                        sEstado = bg.swtSSHH.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etSSHH.getText().toString();
                        }
                        break;
                    case 10:
                        sElemento = "Tablero de Control";
                        sEstado = bg.swtTableroControl.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etTableroControl.getText().toString();
                        }
                        break;
                    case 11:
                        sElemento = "Tachos de Ropa";
                        sEstado = bg.swtTachoRopa.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etTachoRopa.getText().toString();
                        }
                        break;
                    case 12:
                        sElemento = "Therma";
                        sEstado = bg.swtTherma.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etTherma.getText().toString();
                        }
                        break;
                    case 13:
                        sElemento = "Tuberia de Agua";
                        sEstado = bg.swtTuberiaAgua.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etTuberiaAgua.getText().toString();
                        }
                        break;
                    case 14:
                        sOtros = bg.etOtros.getText().toString();
                        sElemento = sOtros;
                        sEstado = bg.swtOtros.getText().toString();
                        sValidaReg = "Dato";
                        if (sEstado.equals("No")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sDescripcion = bg.etDescOtros.getText().toString();
                        }
                        break;
                }
                String CodUser=hP.getCodigoUsuario();
                if (sValidaReg.equals("Dato")){
                    try {
                        iId = iId + 1;
                        miTime= hM.gethoraActual();
                        miDate = hM.getfechaActual();

                        querys = "INSERT INTO " + dbE.t_RMantenimiento +
                                "(" + dbE.c_rmId + "," + dbE.c_rmUsuario + "," + dbE.c_rmDate +
                                "," + dbE.c_rmTime + "," + dbE.c_rmFecha + "," + dbE.c_rmCencos +
                                "," + dbE.c_rmTurno + "," + dbE.c_rmElemento + "," + dbE.c_rmFallo +
                                "," + dbE.c_rmDescripcion + "," + dbE.c_rmNomColaborador + ")" +
                                "VALUES('" + iId.toString() +  "','" + CodUser  +  "','" + miDate +
                                "','" + miTime + "','" + sFecha + "','" + sCencos +
                                "','" + sTurno + "','" + sElemento + "','" + sEstado +
                                "','"+ sDescripcion + "','"+ sColaborador + "')";
                        db.execSQL(querys);
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
        int count = bg.tlRegMantenimiento.getChildCount();
        for (int i=1;i<count;i++) {
            View child = bg.tlRegMantenimiento.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        db = conn.getReadableDatabase();
        sTurno = bg.spnTurno.getText().toString();
        try{
            sql = "SELECT " + dbEstructura.c_rmId + "," + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmTurno +
                    "," + dbEstructura.c_rmNomColaborador + "," + dbEstructura.c_rmElemento + "," + dbEstructura.c_rmFallo +
                    "," + dbEstructura.c_rmDescripcion +
                    " FROM " + dbEstructura.t_RMantenimiento +
                    " WHERE " + dbEstructura.c_rmFecha + "='" + xFecha + "' AND " + dbEstructura.c_rmCencos + "='" + xCencos + "'AND  "  + dbEstructura.c_rgTurno + "='" + sTurno +
                    "'  ORDER BY " + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmCencos+ "," + dbEstructura.c_rmTurno;
            Cursor cursor = db.rawQuery(sql,null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    while (cursor.moveToNext()) {
                        sId= (cursor.getString(0));
                        sFecha = (cursor.getString(1));
                        sTurno = (cursor.getString(2));
                        sColaborador = (cursor.getString(3));
                        sElemento = (cursor.getString(4));
                        sEstado = (cursor.getString(5));
                        sDescripcion = (cursor.getString(6));

                        String[] cadena = {sId,sFecha,sTurno,sColaborador,sElemento,sEstado,sDescripcion};
                        trFilas = new TableRow(getActivity().getBaseContext());
                        for (int i= 0;i<7;i++) {
                            textView = new TextView(getActivity().getBaseContext());
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextAppearance(getActivity(), R.style.estilo_celda_detalle);
                            textView.setBackgroundResource(R.drawable.tabla_celda);
                            textView.setText(cadena[i]);
                            trFilas.addView(textView);
                        }
                        bg.tlRegMantenimiento.addView(trFilas);
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

    private void listaItems(String xFecha,String xCencos){

        db = conn.getReadableDatabase();
        //PARA FILTRAR TABLA
        sTurno = bg.spnTurno.getText().toString();


        eRegEliminar regEliminar = null;
        regEliminarList = new ArrayList<eRegEliminar>();
        bg.spnEliminar.setAdapter(null);
        bg.spnEliminar.setText("Seleccione");
        bg.btnEliminar.setEnabled(false);
        iPosEli=-1;
        try{
            sql = "SELECT " + dbEstructura.c_rmId + "," + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmTurno +
                    "," + dbEstructura.c_rmNomColaborador + "," + dbEstructura.c_rmElemento + "," + dbEstructura.c_rmFallo +
                    "," + dbEstructura.c_rmDescripcion +
                    " FROM " + dbEstructura.t_RMantenimiento +
                    " WHERE " + dbEstructura.c_rmFecha + "='" + xFecha + "' AND " + dbEstructura.c_rmCencos + "='" + xCencos + "'AND  "  + dbEstructura.c_rgTurno + "='" + sTurno +
                    "'  ORDER BY " + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmCencos+ "," + dbEstructura.c_rmTurno;
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    listaRegEliminar = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        sId= (cursor.getString(0));
                        sFecha = (cursor.getString(1));
                        sTurno = (cursor.getString(2));
                        sColaborador = (cursor.getString(3));
                        sElemento = (cursor.getString(4));
                        sEstado = (cursor.getString(5));
                        sDescripcion = (cursor.getString(6));

                        regEliminar = new eRegEliminar();
                        regEliminar.set_id(sId);
                        regEliminar.set_descripcion(sTurno+"|"+sTurno+"|"+sElemento+"|"+sEstado);
                        regEliminarList.add(regEliminar);
                    }
                    for(int i=0;i<regEliminarList.size();i++){
                        listaRegEliminar.add(regEliminarList.get(i).get_id()+"|"+regEliminarList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> adaptador= new ArrayAdapter(getContext(),R.layout.items_list,listaRegEliminar);
                    bg.spnEliminar.setAdapter(adaptador);
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
            case R.id.swtCajaFormol:
                if (bg.swtCajaFormol.isChecked()) {bg.swtCajaFormol.setText("Si");bg.etCajaFormol.setEnabled(true);}else{bg.swtCajaFormol.setText("No");bg.etCajaFormol.setEnabled(false);bg.etCajaFormol.setText("");}
                break;
            case R.id.swtComedor:
                if (bg.swtComedor.isChecked()) {bg.swtComedor.setText("Si");bg.etComedor.setEnabled(true);}else{bg.swtComedor.setText("No");bg.etComedor.setEnabled(false);bg.etComedor.setText("");}
                break;
            case R.id.swtDucha:
                if (bg.swtDucha.isChecked()) {bg.swtDucha.setText("Si");bg.etDucha.setEnabled(true);}else{bg.swtDucha.setText("No");bg.etDucha.setEnabled(false);bg.etDucha.setText("");}
                break;
            case R.id.swtEstructuraGarita:
                if (bg.swtEstructuraGarita.isChecked()) {bg.swtEstructuraGarita.setText("Si");bg.etEstructuraGarita.setEnabled(true);}else{bg.swtEstructuraGarita.setText("No");bg.etEstructuraGarita.setEnabled(false);bg.etEstructuraGarita.setText("");}
                break;
            case R.id.swtMedidorAgua:
                if (bg.swtMedidorAgua.isChecked()) {bg.swtMedidorAgua.setText("Si");bg.etMedidorAgua.setEnabled(true);}else{bg.swtMedidorAgua.setText("No");bg.etMedidorAgua.setEnabled(false);bg.etMedidorAgua.setText("");}
                break;
            case R.id.swtPuertaPersonal:
                if (bg.swtPuertaPersonal.isChecked()) {bg.swtPuertaPersonal.setText("Si");bg.etPuertaPersonal.setEnabled(true);}else{bg.swtPuertaPersonal.setText("No");bg.etPuertaPersonal.setEnabled(false);bg.etPuertaPersonal.setText("");}
                break;
            case R.id.swtPuertaPozoSeptico:
                if (bg.swtPuertaPozoSeptico.isChecked()) {bg.swtPuertaPozoSeptico.setText("Si");bg.etPuertaPozoSeptico.setEnabled(true);}else{bg.swtPuertaPozoSeptico.setText("No");bg.etPuertaPozoSeptico.setEnabled(false);bg.etPuertaPozoSeptico.setText("");}
                break;
            case R.id.swtPuertaVehicular:
                if (bg.swtPuertaVehicular.isChecked()) {bg.swtPuertaVehicular.setText("Si");bg.etPuertaVehicular.setEnabled(true);}else{bg.swtPuertaVehicular.setText("No");bg.etPuertaVehicular.setEnabled(false);bg.etPuertaVehicular.setText("");}
                break;
            case R.id.swtSSHH:
                if (bg.swtSSHH.isChecked()) {bg.swtSSHH.setText("Si");bg.etSSHH.setEnabled(true);}else{bg.swtSSHH.setText("No");bg.etSSHH.setEnabled(false);bg.etSSHH.setText("");}
                break;
            case R.id.swtTableroControl:
                if (bg.swtTableroControl.isChecked()) {bg.swtTableroControl.setText("Si");bg.etTableroControl.setEnabled(true);}else{bg.swtTableroControl.setText("No");bg.etTableroControl.setEnabled(false);bg.etTableroControl.setText("");}
                break;
            case R.id.swtTachoRopa:
                if (bg.swtTachoRopa.isChecked()) {bg.swtTachoRopa.setText("Si");bg.etTachoRopa.setEnabled(true);}else{bg.swtTachoRopa.setText("No");bg.etTachoRopa.setEnabled(false);bg.etTachoRopa.setText("");}
                break;
            case R.id.swtTherma:
                if (bg.swtTherma.isChecked()) {bg.swtTherma.setText("Si");bg.etTherma.setEnabled(true);}else{bg.swtTherma.setText("No");bg.etTherma.setEnabled(false);bg.etTherma.setText("");}
                break;
            case R.id.swtTuberiaAgua:
                if (bg.swtTuberiaAgua.isChecked()) {bg.swtTuberiaAgua.setText("Si");bg.etTuberiaAgua.setEnabled(true);}else{bg.swtTuberiaAgua.setText("No");bg.etTuberiaAgua.setEnabled(false);bg.etTuberiaAgua.setText("");}
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
                }else if(bg.swtCajaFormol.getText().toString().equals("Si")&&(bg.etCajaFormol.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtComedor.getText().toString().equals("Si")&&(bg.etComedor.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtDucha.getText().toString().equals("Si")&&(bg.etDucha.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtEstructuraGarita.getText().toString().equals("Si")&&(bg.etEstructuraGarita.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtMedidorAgua.getText().toString().equals("Si")&&(bg.etMedidorAgua.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtPuertaPersonal.getText().toString().equals("Si")&&(bg.etPuertaPersonal.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtPuertaPozoSeptico.getText().toString().equals("Si")&&(bg.etPuertaPozoSeptico.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtPuertaVehicular.getText().toString().equals("Si")&&(bg.etPuertaVehicular.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtSSHH.getText().toString().equals("Si")&&(bg.etSSHH.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtTableroControl.getText().toString().equals("Si")&&(bg.etTableroControl.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtTachoRopa.getText().toString().equals("Si")&&(bg.etTachoRopa.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtTherma.getText().toString().equals("Si")&&(bg.etTherma.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if(bg.swtTuberiaAgua.getText().toString().equals("Si")&&(bg.etTuberiaAgua.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(bg.swtOtros.getText().toString().equals("Si")&&(bg.etDescOtros.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta llenar descripción!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    guardarDatos();
                    mostrarTotales(sFecha,sCencos);
                    listaItems(sFecha,sCencos);
                    inicializarCampos();
                }
        }
    }

}