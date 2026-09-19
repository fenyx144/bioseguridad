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
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eAnomalias;
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eRegEliminar;
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eRegistroGarita;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentGaritaBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GaritaFragment extends Fragment {

    private FragmentGaritaBinding bg;

    /*<!-- TODO: VARIABLES G -->*/
    Integer iHora,iMinutos;
    Integer iId,iPosiAn = -1,iPosiTur = -1,iPosiFil = -1,iPosEli=-1;

    /*<!-- TODO: VARIABLES CABECERA -->*/
    String sFechaActual,sNombreUsuario,sSerieDispositivo,sDoc,sSerie,sNumero;

    String sHora,sMinutos,miDate,miTime;
    String sDescripcion,sAgenteCausal,sValidaReg,sFecha,sId,sCencos,sColaborador,sTurno,sFiltro,sAnomalia;


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

    ArrayList<String> listaAnomalias;
    ArrayList<eAnomalias>anomaliasList;

    ArrayList<String> listaRegEliminar;
    ArrayList<eRegEliminar>regEliminarList;



    public GaritaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bg = FragmentGaritaBinding.bind(view);

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

        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/

        try {
            String[] cadena = {"ID", "Fecha",  "Turno", "# Filtro", "Anomalía", "Hora", "Descripción", "Agente Causal"};
            trFilas = new TableRow(getActivity().getBaseContext());
            for (int i = 0; i < 8; i++) {
                textView = new TextView(getActivity().getBaseContext());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextAppearance(getActivity(), R.style.estilo_celda);
                textView.setBackgroundResource(R.drawable.tabla_celda_cabecera);
                textView.setText(cadena[i]);
                trFilas.addView(textView);
            }
            bg.tlRegGarita.addView(trFilas);
        } catch (Exception e) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }



        buscarCencos();
        buscarAnomalias();

        /*<!-- TODO: CARGA DE DATA SPINNER ARRAY -->*/
        List<String> listt = new ArrayList<>();
        //listt.add("Seleccione");
        listt.add("Dia");
        listt.add("Noche");
        ArrayAdapter<String> adaptert = new ArrayAdapter<String>(getContext(), R.layout.items_list, listt);
        adaptert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bg.spnTurno.setAdapter(adaptert);

        List<String> listf = new ArrayList<>();
        //listf.add("Seleccione");
        listf.add("1");
        listf.add("2");
        listf.add("3");
        ArrayAdapter<String> adapterf = new ArrayAdapter<String>(getContext(), R.layout.items_list, listf);
        adapterf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bg.spnNumFiltro.setAdapter(adapterf);

        /*<!-- TODO: CAPTURA DE POSICION EXCLUYENDO LA PRIMERA -->*/

        bg.spnTurno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosiTur=position;
            }
        });
        bg.spnNumFiltro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosiFil=position;
            }
        });

        bg.spnEliminar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosEli=position;
                bg.btnEliminar.setEnabled(true);
            }
        });



        bg.spnAnomalia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosiAn=position;
            }
        });

        bg.spnTurno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bg.tvTurno.setErrorEnabled(false);

                sFecha = bg.tvFecha.getText().toString();
                sCencos = bg.etCencos.getText().toString().substring(0, 6);
                mostrarTotales(sFecha, sCencos);
                listaItems(sFecha, sCencos);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bg.spnNumFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bg.tvNumFiltro.setErrorEnabled(false);

                sFecha = bg.tvFecha.getText().toString();
                sCencos = bg.etCencos.getText().toString().substring(0, 6);
                mostrarTotales(sFecha, sCencos);
                listaItems(sFecha, sCencos);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bg.spnAnomalia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bg.tvAnomalia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bg.etHora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bg.tvHora.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*<!-- TODO: GENERAR FORMATO PARA CAPTURA DE HORA -->*/
        bg.etHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                iHora = c.get(Calendar.HOUR_OF_DAY);
                iMinutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sHora = "00" + hourOfDay;
                        sHora = sHora.substring(Math.max(0, sHora.length() - 2));
                        sMinutos = "00" + minute;
                        sMinutos = sMinutos.substring(Math.max(0, sMinutos.length() - 2));
                        bg.etHora.setText(sHora + ":" + sMinutos);
                    }
                }, iHora, iMinutos, true);
                timePickerDialog.show();
            }
        });





        /*<!-- TODO: CLICK AL BOTON AGREGAR -->*/

        bg.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sFecha = bg.tvFecha.getText().toString();
                sCencos = bg.etCencos.getText().toString().substring(0, 6);


                    Boolean bValidaCampos = true;
                    if (iPosiTur==-1) {
                        bValidaCampos = false;
                        bg.tvTurno.setError("Turno Ivalido!");
                        bg.spnTurno.setError(null);
                    }else{
                        bg.tvTurno.setError(null);
                    }

                    if (iPosiFil==-1) {
                        bValidaCampos = false;
                        bg.tvNumFiltro.setError("N°Filtro Ivalido!");
                        bg.spnNumFiltro.setError(null);
                    }else{
                        bg.tvNumFiltro.setError(null);
                    }

                    if (iPosiAn==-1) {
                        bValidaCampos = false;
                        bg.tvAnomalia.setError("Anomalía Ivalida!");
                        bg.spnAnomalia.setError(null);
                    }else{
                        bg.tvAnomalia.setError(null);
                    }

                    if (bg.etHora.getText().toString().trim().equalsIgnoreCase("00:00")) {
                        bValidaCampos = false;
                        bg.tvHora.setError("Ingrese Hora!");
                        bg.etHora.setError(null);
                    }else{
                        bg.tvHora.setError(null);
                    }

                if (bg.etDescripcion.getText().toString().equals("")) {
                    bValidaCampos = false;
                    //bg.tvDescripcion.setError("Ingrese la descripción!");
                    bg.etDescripcion.setError("Ingrese la descripción!");
                }
                if (bg.etAgenteCausal.getText().toString().equals("")) {
                    bValidaCampos = false;
                    //bg.tvAgenteCausal.setError("Ingrese el Agente Causal!");
                    bg.etAgenteCausal.setError("Ingrese el Agente Causal!");
                }

                    if (bValidaCampos == true) {
                        guardarDatos();
                        mostrarTotales(sFecha, sCencos);
                        listaItems(sFecha, sCencos);
                        inicializarCampos();
                    }
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

                            String rMensaje=hP.getElimarRegistro(dbE.t_RGarita,dbE.c_rgFecha,slFecha,dbE.c_rgCencos,slCencos,dbE.c_rgId,slId);
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

        //bg.spnTurno.setText("");
        //bg.spnNumFiltro.setText("");
        //bg.spnAnomalia.setText("");
        //bg.etHora.setText("00:00");
        bg.etDescripcion.setText("");
        bg.etAgenteCausal.setText("");
    }

    private void buscarCencos() {
        db=conn.getReadableDatabase();

        try{
            querys="SELECT "+ dbE.c_cCodigo+","+ dbE.c_cNombre+" FROM "+dbE.t_Cencos+" ";
            Cursor cursor=db.rawQuery(querys,null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bg.etCencos.setText(cursor.getString(0)+"|" +cursor.getString(1));
                    }
                    cursor.close();
                }else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Cencos no existe", Toast.LENGTH_SHORT);
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

    private void buscarAnomalias() {
        db = conn.getReadableDatabase();
        eAnomalias anomalias = null;
        anomaliasList = new ArrayList<eAnomalias>();
        try{
            querys = "SELECT * FROM " + dbE.t_AnomaliaGarita + "  ORDER BY " + dbE.c_anDescripcion  + ";";
            Cursor cursor = db.rawQuery(querys,null);
            if (cursor!=null) {
                listaAnomalias = new ArrayList<String>();
                //listaAnomalias.add("Seleccione");
                if (cursor.getCount()>0) {
                    while (cursor.moveToNext()) {
                        anomalias = new eAnomalias();
                        anomalias.set_codigo(cursor.getString(0));
                        anomalias.set_descripcion(cursor.getString(1));
                        anomaliasList.add(anomalias);
                    }

                    for(int i=0;i<anomaliasList.size();i++){
                        listaAnomalias.add(anomaliasList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> adaptador= new ArrayAdapter(getContext(),R.layout.items_list,listaAnomalias);
                    bg.spnAnomalia.setAdapter(adaptador);
                    cursor.close();
                    db.close();
                }else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"No existen anomalías" ,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }catch (Exception ex){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void guardarDatos(){
        db = conn.getWritableDatabase();

        //Todo:MAXIMO REGISTRO
        try{
            querys = "SELECT MAX(ABS(" + dbE.c_rgId + "))  FROM " + dbE.t_RGarita + " WHERE " + dbE.c_rgFecha + "='" + bg.tvFecha.getText().toString() + "';";
            Cursor cursor1 = db.rawQuery(querys,null);
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

            //Todo:Validamos campos vacios del fragment:

            sDescripcion = bg.etDescripcion.getText().toString();
            sAgenteCausal = bg.etAgenteCausal.getText().toString();



            //Todo:Agregamos campos generales a la Tabla
            sFecha = bg.tvFecha.getText().toString();
            sColaborador = hP.getNombreUsuario();//bg.tvNombreUsuario.getText().toString();
            sCencos = bg.etCencos.getText().toString().substring(0,6);
            sTurno = bg.spnTurno.getText().toString();
            sFiltro = bg.spnNumFiltro.getText().toString();
            sAnomalia = bg.spnAnomalia.getText().toString();//anomaliasList.get(iPosiAn).get_descripcion();
            sHora = bg.etHora.getText().toString();

            String CodUser=hP.getCodigoUsuario();
                try {
                    iId = iId + 1;
                     miTime= hM.gethoraActual();
                     miDate = hM.getfechaActual();

                     querys = "INSERT INTO " + dbE.t_RGarita +
                            "(" + dbE.c_rgId + "," + dbE.c_rgUsuario + "," + dbE.c_rgDate +
                            "," + dbE.c_rgTime + "," + dbE.c_rgFecha + "," + dbE.c_rgCencos +
                            "," + dbE.c_rgNomColaborador + "," + dbE.c_rgTurno + "," + dbE.c_rgNumFiltro +
                            "," + dbE.c_rgAnomalia + "," + dbE.c_rgHora + "," + dbE.c_rgDescripcion +
                            "," + dbE.c_rgAgenteCausal + ")" +
                            "VALUES('" + iId.toString() + "','" + CodUser  + "','" + miDate +
                            "','" + miTime + "','" + sFecha + "','" + sCencos +
                            "','" + sColaborador + "','" + sTurno + "','" + sFiltro +
                            "','"+ sAnomalia + "','" + sHora + "','" + sDescripcion +
                            "','" + sAgenteCausal + "')";
                    db.execSQL(querys);
                } catch (Exception ex) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    db.close();
                }


        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),e.getMessage() ,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void mostrarTotales(String xFecha,String xCencos){
        int count = bg.tlRegGarita.getChildCount();
        for (int i=1;i<count;i++) {
            View child = bg.tlRegGarita.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        db = conn.getReadableDatabase();
        //PARA FILTRAR TABLA
        sTurno = bg.spnTurno.getText().toString();
        sFiltro = bg.spnNumFiltro.getText().toString();
        iPosEli=-1;
        try{
            querys = "SELECT " + dbE.c_rgId + "," + dbE.c_rgFecha + "," + dbE.c_rgNomColaborador +
                    "," + dbE.c_rgTurno + "," + dbE.c_rgNumFiltro + "," + dbE.c_rgAnomalia +
                    "," + dbE.c_rgHora + "," + dbE.c_rgDescripcion + "," + dbE.c_rgAgenteCausal +
                    " FROM " + dbE.t_RGarita +
                    " WHERE " + dbE.c_rgFecha + "='" + xFecha + "' AND " + dbE.c_rgCencos + "='" + xCencos +
                    "' AND  "  + dbE.c_rgTurno + "='" + sTurno +
                    "' AND  " + dbE.c_rgNumFiltro + "='" + sFiltro +
                    "'  ORDER BY " + dbE.c_rgFecha + "," + dbE.c_rgTurno + "," + dbE.c_rgNumFiltro + "," + dbE.c_rgAnomalia;

            Cursor cursor = db.rawQuery(querys,null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0));
                        sFecha = (cursor.getString(1));
                        sColaborador = (cursor.getString(2));
                        sTurno = (cursor.getString(3));
                        sFiltro = (cursor.getString(4));
                        sAnomalia = (cursor.getString(5));
                        sHora = (cursor.getString(6));
                        sDescripcion = (cursor.getString(7));
                        sAgenteCausal = (cursor.getString(8));

                        String[] cadena = {sId,sFecha,sTurno,sFiltro,sAnomalia,sHora,sDescripcion,sAgenteCausal};
                        trFilas = new TableRow(getActivity().getBaseContext());
                        for (int i= 0;i<8;i++) {
                            textView = new TextView(getActivity().getBaseContext());
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextAppearance(getActivity(), R.style.estilo_celda_detalle);
                            textView.setBackgroundResource(R.drawable.tabla_celda);
                            textView.setText(cadena[i]);
                            trFilas.addView(textView);
                        }
                        bg.tlRegGarita.addView(trFilas);
                    }
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
        sFiltro = bg.spnNumFiltro.getText().toString();

        eRegEliminar regEliminar = null;
        regEliminarList = new ArrayList<eRegEliminar>();
        bg.spnEliminar.setAdapter(null);
        bg.spnEliminar.setText("Seleccione");
        bg.btnEliminar.setEnabled(false);
        iPosEli=-1;
        try{
            querys = "SELECT " + dbE.c_rgId + "," + dbE.c_rgFecha + "," + dbE.c_rgNomColaborador +
                    "," + dbE.c_rgTurno + "," + dbE.c_rgNumFiltro + "," + dbE.c_rgAnomalia +
                    "," + dbE.c_rgHora + "," + dbE.c_rgDescripcion + "," + dbE.c_rgAgenteCausal +
                    " FROM " + dbE.t_RGarita +
                    " WHERE " + dbE.c_rgFecha + "='" + xFecha + "' AND " + dbE.c_rgCencos + "='" + xCencos +
                    "' AND  "  + dbE.c_rgTurno + "='" + sTurno +
                    "' AND  " + dbE.c_rgNumFiltro + "='" + sFiltro +
                    "'  ORDER BY " + dbE.c_rgFecha + "," + dbE.c_rgTurno + "," + dbE.c_rgNumFiltro + "," + dbE.c_rgAnomalia;

            Cursor cursor = db.rawQuery(querys,null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    listaRegEliminar = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0));
                        sFecha = (cursor.getString(1));
                        sColaborador = (cursor.getString(2));
                        sTurno = (cursor.getString(3));
                        sFiltro = (cursor.getString(4));
                        sAnomalia = (cursor.getString(5));

                        regEliminar = new eRegEliminar();
                        regEliminar.set_id(sId);
                        regEliminar.set_descripcion(sTurno+"|"+sFiltro+"|"+sAnomalia);
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

}