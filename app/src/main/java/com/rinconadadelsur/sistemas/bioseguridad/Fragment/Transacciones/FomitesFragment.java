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
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eProcesos;
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eReferencias;
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eRegEliminar;
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eTipo;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentFomitesBinding;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentMantenimientoBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FomitesFragment extends Fragment implements View.OnClickListener {

    private FragmentFomitesBinding bg;

    /*<!-- TODO: VARIABLES G -->*/
    Integer iPosiRef = -1,iPosiTur = -1,iPosiPro = -1,iId,iPosEli = -1;
    Integer iHora,iMinutos;

    /*<!-- TODO: VARIABLES CABECERA -->*/
    String sFechaActual,sNombreUsuario,sSerieDispositivo,sDoc,sSerie,sNumero;

    String sHora,sMinutos,sql,sObservacion,sValidaReg,sDepredador,sTipo,sCantidad,sEstado,sId,sFecha,sCencos,sTurno;
    String miDate,miTime,sReferencia,sProceso,sColaborador;


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
    ArrayList<String> listReferencia;
    ArrayList<eReferencias> ReferenciaList;
    ArrayList<String> listProceso;
    ArrayList<eProcesos> ProcesoList;
    ArrayList<String> listResponsable;
    ArrayList<String> listZ;
    ArrayList<eTipo> ZList;
    ArrayList<String> listP;
    ArrayList<eTipo> PList;
    ArrayList<String> listG;
    ArrayList<eTipo> GList;
    ArrayList<String> listL;
    ArrayList<eTipo> LList;
    ArrayList<String> listGa;
    ArrayList<eTipo> GaList;
    ArrayList<String> listC;
    ArrayList<eTipo> CList;
    ArrayList<String> listPa;
    ArrayList<eTipo> PaList;

    ArrayList<String> listaRegEliminar;
    ArrayList<eRegEliminar>regEliminarList;

    public FomitesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fomites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bg = FragmentFomitesBinding.bind(view);

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
        buscarReferencia();
        buscarProceso();
        buscarZ();
        buscarP();
        buscarG();
        buscarL();
        buscarGa();
        buscarC();
        buscarPa();

        /*<!-- TODO: BOTONES -->*/
        bg.swtZorro.setOnClickListener(this);
        bg.swtPerro.setOnClickListener(this);
        bg.swtGallinazo.setOnClickListener(this);
        bg.swtLechuza.setOnClickListener(this);
        bg.swtGato.setOnClickListener(this);
        bg.swtConejo.setOnClickListener(this);
        bg.swtPaloma.setOnClickListener(this);
        bg.btnAgregar.setOnClickListener(this);

        /*<!-- TODO: GENERAR FORMATO PARA CAPTURA DE HORA -->*/
        bg.etZorroHora.setOnClickListener(new View.OnClickListener() {
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
                        bg.etZorroHora.setText(sHora + ":" + sMinutos);
                    }
                }, iHora, iMinutos, true);
                timePickerDialog.show();
            }
        });
        bg.etPerroHora.setOnClickListener(new View.OnClickListener() {
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
                        bg.etPerroHora.setText(sHora + ":" + sMinutos);
                    }
                }, iHora, iMinutos, true);
                timePickerDialog.show();
            }
        });
        bg.etGallinazoHora.setOnClickListener(new View.OnClickListener() {
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
                        bg.etGallinazoHora.setText(sHora + ":" + sMinutos);
                    }
                }, iHora, iMinutos, true);
                timePickerDialog.show();
            }
        });
        bg.etLechuzaHora.setOnClickListener(new View.OnClickListener() {
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
                        bg.etLechuzaHora.setText(sHora + ":" + sMinutos);
                    }
                }, iHora, iMinutos, true);
                timePickerDialog.show();
            }
        });
        bg.etGatoHora.setOnClickListener(new View.OnClickListener() {
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
                        bg.etGatoHora.setText(sHora + ":" + sMinutos);
                    }
                }, iHora, iMinutos, true);
                timePickerDialog.show();
            }
        });
        bg.etConejoHora.setOnClickListener(new View.OnClickListener() {
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
                        bg.etConejoHora.setText(sHora + ":" + sMinutos);
                    }
                }, iHora, iMinutos, true);
                timePickerDialog.show();
            }
        });
        bg.etPalomaHora.setOnClickListener(new View.OnClickListener() {
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
                        bg.etPalomaHora.setText(sHora + ":" + sMinutos);
                    }
                }, iHora, iMinutos, true);
                timePickerDialog.show();
            }
        });

        /*<!-- TODO: CARGA DE DATA SPINNER ARRAY -->*/
        List<String> listt = new ArrayList<>();

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
                sFecha = bg.tvFecha.getText().toString();
                sCencos = bg.etCencos.getText().toString().substring(0, 6);
                mostrarTotales(sFecha, sCencos);
                listaItems(sFecha, sCencos);
            }
        });
        bg.spnReferencia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosiRef=position;
            }
        });
        bg.spnProceso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosiPro=position;
            }
        });

        /*<!-- TODO: HABILITAR CAMPOS CUANDO HAY SELECCION -->*/
        bg.spnZorro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    bg.etZorro.setEnabled(false);
                    bg.etZorroHora.setEnabled(false);
                    bg.swtZorro.setEnabled(false);
                    bg.etZorro.setText("");
                    bg.etZorroHora.setText("");
                    if (bg.swtZorro.getText().toString().equals("Si")) {
                        bg.swtZorro.setChecked(false);
                        bg.swtZorro.setText("No");
                    }
                } else {
                    bg.etZorro.setText("");
                    bg.etZorroHora.setText("");
                    bg.etZorro.setEnabled(true);
                    bg.etZorroHora.setEnabled(true);
                    bg.swtZorro.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        bg.spnPerro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    bg.etPerro.setEnabled(false);
                    bg.etPerroHora.setEnabled(false);
                    bg.swtPerro.setEnabled(false);
                    bg.etPerro.setText("");
                    bg.etPerroHora.setText("");
                    if (bg.swtPerro.getText().toString().equals("Si")) {
                        bg.swtPerro.setChecked(false);
                        bg.swtPerro.setText("No");
                    }
                } else {
                    bg.etPerro.setText("");
                    bg.etPerroHora.setText("");
                    bg.etPerro.setEnabled(true);
                    bg.etPerroHora.setEnabled(true);
                    bg.swtPerro.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        bg.spnGallinazo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    bg.etGallinazo.setEnabled(false);
                    bg.etGallinazoHora.setEnabled(false);
                    bg.swtGallinazo.setEnabled(false);
                    bg.etGallinazo.setText("");
                    bg.etGallinazoHora.setText("");
                    if (bg.swtGallinazo.getText().toString().equals("Si")) {
                        bg.swtGallinazo.setChecked(false);
                        bg.swtGallinazo.setText("No");
                    }
                } else {
                    bg.etGallinazo.setText("");
                    bg.etGallinazoHora.setText("");
                    bg.etGallinazo.setEnabled(true);
                    bg.etGallinazoHora.setEnabled(true);
                    bg.swtGallinazo.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        bg.spnLechuza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    bg.etLechuza.setEnabled(false);
                    bg.etLechuzaHora.setEnabled(false);
                    bg.swtLechuza.setEnabled(false);
                    bg.etLechuza.setText("");
                    bg.etLechuzaHora.setText("");
                    if (bg.swtLechuza.getText().toString().equals("Si")) {
                        bg.swtLechuza.setChecked(false);
                        bg.swtLechuza.setText("No");
                    }
                } else {
                    bg.etLechuza.setText("");
                    bg.etLechuzaHora.setText("");
                    bg.etLechuza.setEnabled(true);
                    bg.etLechuzaHora.setEnabled(true);
                    bg.swtLechuza.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        bg.spnGato.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    bg.etGato.setEnabled(false);
                    bg.etGatoHora.setEnabled(false);
                    bg.swtGato.setEnabled(false);
                    bg.etGato.setText("");
                    bg.etGatoHora.setText("");
                    if (bg.swtGato.getText().toString().equals("Si")) {
                        bg.swtGato.setChecked(false);
                        bg.swtGato.setText("No");
                    }
                } else {
                    bg.etGato.setText("");
                    bg.etGatoHora.setText("");
                    bg.etGato.setEnabled(true);
                    bg.etGatoHora.setEnabled(true);
                    bg.swtGato.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        bg.spnConejo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    bg.etConejo.setEnabled(false);
                    bg.etConejoHora.setEnabled(false);
                    bg.swtConejo.setEnabled(false);
                    bg.etConejo.setText("");
                    bg.etConejoHora.setText("");
                    if (bg.swtConejo.getText().toString().equals("Si")) {
                        bg.swtConejo.setChecked(false);
                        bg.swtConejo.setText("No");
                    }
                } else {
                    bg.etConejo.setText("");
                    bg.etConejoHora.setText("");
                    bg.etConejo.setEnabled(true);
                    bg.etConejoHora.setEnabled(true);
                    bg.swtConejo.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        bg.spnPaloma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    bg.etPaloma.setEnabled(false);
                    bg.etPalomaHora.setEnabled(false);
                    bg.swtPaloma.setEnabled(false);
                    bg.etPaloma.setText("");
                    bg.etPalomaHora.setText("");
                    if (bg.swtPaloma.getText().toString().equals("Si")) {
                        bg.swtPaloma.setChecked(false);
                        bg.swtPaloma.setText("No");
                    }
                } else {
                    bg.etPaloma.setText("");
                    bg.etPalomaHora.setText("");
                    bg.etPaloma.setEnabled(true);
                    bg.etPalomaHora.setEnabled(true);
                    bg.swtPaloma.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/
        try {
            String[] cadena = {"ID","Fecha","Turno", "Referencia", "Proceso", "Colaborador", "Depredador", "Tipo", "Cantidad", "Hora", "Estado", "Observacion"};
            trFilas = new TableRow(getActivity().getBaseContext());
            for (int i = 0; i < 12; i++) {
                textView = new TextView(getActivity().getBaseContext());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextAppearance(getActivity(), R.style.estilo_celda);
                textView.setBackgroundResource(R.drawable.tabla_celda_cabecera);
                textView.setText(cadena[i]);
                trFilas.addView(textView);
            }
            bg.tlRegFomites.addView(trFilas);
        } catch (Exception e) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

        /*<!-- TODO: DESACTIVAR LOS CAMPOS CON SWITCHS -->*/
        bg.etZorro.setEnabled(false);
        bg.etPerro.setEnabled(false);
        bg.etGallinazo.setEnabled(false);
        bg.etLechuza.setEnabled(false);
        bg.etGato.setEnabled(false);
        bg.etConejo.setEnabled(false);
        bg.etPaloma.setEnabled(false);
        bg.etZorroHora.setEnabled(false);
        bg.etPerroHora.setEnabled(false);
        bg.etGallinazoHora.setEnabled(false);
        bg.etLechuzaHora.setEnabled(false);
        bg.etGatoHora.setEnabled(false);
        bg.etConejoHora.setEnabled(false);
        bg.etPalomaHora.setEnabled(false);
        bg.swtZorro.setEnabled(false);
        bg.swtPerro.setEnabled(false);
        bg.swtGallinazo.setEnabled(false);
        bg.swtLechuza.setEnabled(false);
        bg.swtGato.setEnabled(false);
        bg.swtConejo.setEnabled(false);
        bg.swtPaloma.setEnabled(false);



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

        sFecha = bg.tvFecha.getText().toString();
        sCencos = bg.etCencos.getText().toString().substring(0, 6);
        mostrarTotales(sFecha, sCencos);
        listaItems(sFecha, sCencos);

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

                            String rMensaje=hP.getElimarRegistro(dbE.t_RFomites,dbE.c_rfFecha,slFecha,dbE.c_rfCencos,slCencos,dbE.c_rfId,slId);
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

        bg.spnEliminar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                iPosEli=position;
                bg.btnEliminar.setEnabled(true);
            }
        });

    }

    private void inicializarCampos() {
        ArrayAdapter<CharSequence> r = new ArrayAdapter(getContext(), R.layout.items_list, listReferencia);
        ArrayAdapter<CharSequence> p = new ArrayAdapter(getContext(), R.layout.items_list, listProceso);
        ArrayAdapter<CharSequence> rb = new ArrayAdapter(getContext(), R.layout.items_list, listResponsable);
        ArrayAdapter<CharSequence> zo = new ArrayAdapter(getContext(), R.layout.items_list, listZ);
        ArrayAdapter<CharSequence> pe = new ArrayAdapter(getContext(), R.layout.items_list, listP);
        ArrayAdapter<CharSequence> ga = new ArrayAdapter(getContext(), R.layout.items_list, listG);
        ArrayAdapter<CharSequence> le = new ArrayAdapter(getContext(), R.layout.items_list, listL);
        ArrayAdapter<CharSequence> gat = new ArrayAdapter(getContext(), R.layout.items_list, listGa);
        ArrayAdapter<CharSequence> co = new ArrayAdapter(getContext(), R.layout.items_list, listC);
        ArrayAdapter<CharSequence> pal = new ArrayAdapter(getContext(), R.layout.items_list, listPa);


        bg.spnTurno.setSelection(0);
        bg.spnTurno.setEnabled(true);

        bg.spnReferencia.setAdapter(r);
        bg.spnReferencia.setEnabled(true);

        bg.spnProceso.setAdapter(p);
        bg.spnProceso.setEnabled(true);


        for (int i = 1; i <= 7; i++) {
            switch (i) {
                case 1:
                    sDepredador = "Zorro";
                    sTipo = bg.spnZorro.getSelectedItem().toString();
                    sValidaReg = "Dato";
                    if (sTipo.equals("Selec")) {
                        sValidaReg = "Sin Dato";
                    } else {
                        bg.spnZorro.setAdapter(zo);
                        bg.spnZorro.setEnabled(true);
                        bg.etZorro.setText("");
                        bg.etZorroHora.setText("");
                        if (bg.swtZorro.getText().toString().equals("Si")) {
                            bg.swtZorro.setChecked(false);
                            bg.swtZorro.setText("No");
                        }
                    }
                    break;
                case 2:
                    sDepredador = "Perro";
                    sTipo = bg.spnPerro.getSelectedItem().toString();
                    sValidaReg = "Dato";
                    if (sTipo.equals("Selec")) {
                        sValidaReg = "Sin Dato";
                    } else {
                        bg.spnPerro.setAdapter(pe);
                        bg.spnPerro.setEnabled(true);
                        bg.etPerro.setText("");
                        bg.etPerroHora.setText("");
                        if (bg.swtPerro.getText().toString().equals("Si")) {
                            bg.swtPerro.setChecked(false);
                            bg.swtPerro.setText("No");
                        }
                    }
                    break;
                case 3:
                    sDepredador = "Gallinazo";
                    sTipo = bg.spnGallinazo.getSelectedItem().toString();
                    sValidaReg = "Dato";
                    if (sTipo.equals("Selec")) {
                        sValidaReg = "Sin Dato";
                    } else {
                        bg.spnGallinazo.setAdapter(ga);
                        bg.spnGallinazo.setEnabled(true);
                        bg.etGallinazo.setText("");
                        bg.etGallinazoHora.setText("");
                        if (bg.swtGallinazo.getText().toString().equals("Si")) {
                            bg.swtGallinazo.setChecked(false);
                            bg.swtGallinazo.setText("No");
                        }
                    }
                    break;
                case 4:
                    sDepredador = "Lechuza";
                    sTipo = bg.spnLechuza.getSelectedItem().toString();
                    sValidaReg = "Dato";
                    if (sTipo.equals("Selec")) {
                        sValidaReg = "Sin Dato";
                    } else {
                        bg.spnLechuza.setAdapter(le);
                        bg.spnLechuza.setEnabled(true);
                        bg.etLechuza.setText("");
                        bg.etLechuzaHora.setText("");
                        if (bg.swtLechuza.getText().toString().equals("Si")) {
                            bg.swtLechuza.setChecked(false);
                            bg.swtLechuza.setText("No");
                        }
                    }
                    break;
                case 5:
                    sDepredador = "Gato";
                    sTipo = bg.spnGato.getSelectedItem().toString();
                    sValidaReg = "Dato";
                    if (sTipo.equals("Selec")) {
                        sValidaReg = "Sin Dato";
                    } else {
                        bg.spnGato.setAdapter(gat);
                        bg.spnGato.setEnabled(true);
                        bg.etGato.setText("");
                        bg.etGatoHora.setText("");
                        if (bg.swtGato.getText().toString().equals("Si")) {
                            bg.swtGato.setChecked(false);
                            bg.swtGato.setText("No");
                        }
                    }
                    break;
                case 6:
                    sDepredador = "Conejo";
                    sTipo = bg.spnConejo.getSelectedItem().toString();
                    sValidaReg = "Dato";
                    if (sTipo.equals("Selec")) {
                        sValidaReg = "Sin Dato";
                    } else {
                        bg.spnConejo.setAdapter(co);
                        bg.spnConejo.setEnabled(true);
                        bg.etConejo.setText("");
                        bg. etConejoHora.setText("");
                        if (bg.swtConejo.getText().toString().equals("Si")) {
                            bg.swtConejo.setChecked(false);
                            bg.swtConejo.setText("No");
                        }
                    }
                    break;
                case 7:
                    sDepredador = "Paloma";
                    sTipo = bg.spnPaloma.getSelectedItem().toString();
                    sValidaReg = "Dato";
                    if (sTipo.equals("Selec")) {
                        sValidaReg = "Sin Dato";
                    } else {
                        bg.spnPaloma.setAdapter(pal);
                        bg.spnPaloma.setEnabled(true);
                        bg.etPaloma.setText("");
                        bg.etPalomaHora.setText("");
                        if (bg.swtPaloma.getText().toString().equals("Si")) {
                            bg.swtPaloma.setChecked(false);
                            bg.swtPaloma.setText("No");
                        }
                    }
                    break;
            }
        }
        bg.etObservacion.setText("");
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

    private void buscarReferencia() {
        db = conn.getReadableDatabase();
        eReferencias referencias = null;
        ReferenciaList = new ArrayList<eReferencias>();
        try {
            sql = "SELECT cod,des from(SELECT " + dbE.c_rfCodigo + " as cod," + dbE.c_rfDescripcion + " as des FROM " + dbE.t_RefFomites +
                    " UNION" +
                    " SELECT " + dbE.c_cgGalpon + " as cod," + dbE.c_cgGalpon + " as des FROM " + dbE.t_CencosGalpon + ") as tab ORDER BY des";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listReferencia = new ArrayList<String>();

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        referencias = new eReferencias();
                        referencias.set_codigo(cursor.getString(0));
                        referencias.set_descripcion(cursor.getString(1));
                        ReferenciaList.add(referencias);
                    }

                    for (int i = 0; i < ReferenciaList.size(); i++) {
                        listReferencia.add(ReferenciaList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> r = new ArrayAdapter(getContext(), R.layout.items_list, listReferencia);
                    bg.spnReferencia.setAdapter(r);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Referencia no existe!!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void buscarProceso() {
        db = conn.getReadableDatabase();
        eProcesos procesos = null;
        ProcesoList = new ArrayList<eProcesos>();
        try {
            sql = "SELECT * FROM " + dbE.t_ProcesoFomites + "  ORDER BY " + dbE.c_pDescripcion + ";";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listProceso = new ArrayList<String>();

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        procesos = new eProcesos();
                        procesos.set_codigo(cursor.getString(0));
                        procesos.set_descripcion(cursor.getString(1));
                        ProcesoList.add(procesos);
                    }

                    for (int i = 0; i < ProcesoList.size(); i++) {
                        listProceso.add(ProcesoList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> p = new ArrayAdapter(getContext(), R.layout.items_list, listProceso);
                    bg.spnProceso.setAdapter(p);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Proceso no existe!!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void buscarZ() {
        db = conn.getReadableDatabase();
        eTipo z = null;
        ZList = new ArrayList<eTipo>();
        try {
            sql = "SELECT * FROM " + dbE.t_TipoFomites + "  ORDER BY " + dbE.c_tDescripcion + ";";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listZ = new ArrayList<String>();
                listZ.add("Selec");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        z = new eTipo();
                        z.set_codigo(cursor.getString(0));
                        z.set_descripcion(cursor.getString(1));
                        ZList.add(z);
                    }

                    for (int i = 0; i < ZList.size(); i++) {
                        listZ.add(ZList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> zo = new ArrayAdapter(getContext(), R.layout.items_list, listZ);
                    bg.spnZorro.setAdapter(zo);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tipo no Existe", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void buscarP() {
        db = conn.getReadableDatabase();
        eTipo p = null;
        PList = new ArrayList<eTipo>();
        try {
            sql = "SELECT * FROM " + dbE.t_TipoFomites + "  ORDER BY " + dbE.c_tDescripcion + ";";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listP = new ArrayList<String>();
                listP.add("Selec");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        p = new eTipo();
                        p.set_codigo(cursor.getString(0));
                        p.set_descripcion(cursor.getString(1));
                        PList.add(p);
                    }

                    for (int i = 0; i < PList.size(); i++) {
                        listP.add(PList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> pe = new ArrayAdapter(getContext(), R.layout.items_list, listP);
                    bg.spnPerro.setAdapter(pe);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tipo no Existe", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void buscarG() {
        db = conn.getReadableDatabase();
        eTipo g = null;
        GList = new ArrayList<eTipo>();
        try {
            sql = "SELECT * FROM " + dbE.t_TipoFomites + "  ORDER BY " + dbE.c_tDescripcion + ";";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listG = new ArrayList<String>();
                listG.add("Selec");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        g = new eTipo();
                        g.set_codigo(cursor.getString(0));
                        g.set_descripcion(cursor.getString(1));
                        GList.add(g);
                    }

                    for (int i = 0; i < GList.size(); i++) {
                        listG.add(GList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> ga = new ArrayAdapter(getContext(), R.layout.items_list, listG);
                    bg.spnGallinazo.setAdapter(ga);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tipo no Existe", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void buscarL() {
        db = conn.getReadableDatabase();
        eTipo l = null;
        LList = new ArrayList<eTipo>();
        try {
            sql = "SELECT * FROM " + dbE.t_TipoFomites + "  ORDER BY " + dbE.c_tDescripcion + ";";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listL = new ArrayList<String>();
                listL.add("Selec");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        l = new eTipo();
                        l.set_codigo(cursor.getString(0));
                        l.set_descripcion(cursor.getString(1));
                        LList.add(l);
                    }

                    for (int i = 0; i < LList.size(); i++) {
                        listL.add(LList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> le = new ArrayAdapter(getContext(), R.layout.items_list, listL);
                    bg.spnLechuza.setAdapter(le);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tipo no Existe", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void buscarGa() {
        db = conn.getReadableDatabase();
        eTipo ga = null;
        GaList = new ArrayList<eTipo>();
        try {
            sql = "SELECT * FROM " + dbE.t_TipoFomites + "  ORDER BY " + dbE.c_tDescripcion + ";";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listGa = new ArrayList<String>();
                listGa.add("Selec");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        ga = new eTipo();
                        ga.set_codigo(cursor.getString(0));
                        ga.set_descripcion(cursor.getString(1));
                        GaList.add(ga);
                    }

                    for (int i = 0; i < GaList.size(); i++) {
                        listGa.add(GaList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> gat = new ArrayAdapter(getContext(), R.layout.items_list, listGa);
                    bg.spnGato.setAdapter(gat);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tipo no Existe", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void buscarC() {
        db = conn.getReadableDatabase();
        eTipo c = null;
        CList = new ArrayList<eTipo>();
        try {
            sql = "SELECT * FROM " + dbE.t_TipoFomites + "  ORDER BY " + dbE.c_tDescripcion + ";";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listC = new ArrayList<String>();
                listC.add("Selec");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        c = new eTipo();
                        c.set_codigo(cursor.getString(0));
                        c.set_descripcion(cursor.getString(1));
                        CList.add(c);
                    }

                    for (int i = 0; i < CList.size(); i++) {
                        listC.add(CList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> co = new ArrayAdapter(getContext(), R.layout.items_list, listC);
                    bg.spnConejo.setAdapter(co);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tipo no Existe", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void buscarPa() {
        db = conn.getReadableDatabase();
        eTipo pa = null;
        PaList = new ArrayList<eTipo>();
        try {
            sql = "SELECT * FROM " + dbE.t_TipoFomites + "  ORDER BY " + dbE.c_tDescripcion + ";";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                listPa = new ArrayList<String>();
                listPa.add("Selec");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        pa = new eTipo();
                        pa.set_codigo(cursor.getString(0));
                        pa.set_descripcion(cursor.getString(1));
                        PaList.add(pa);
                    }

                    for (int i = 0; i < PaList.size(); i++) {
                        listPa.add(PaList.get(i).get_descripcion());
                    }
                    ArrayAdapter<CharSequence> pal = new ArrayAdapter(getContext(), R.layout.items_list, listPa);
                    bg.spnPaloma.setAdapter(pal);
                    cursor.close();
                    db.close();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tipo no Existe", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void guardarDatos(){
        sColaborador =  hP.getNombreUsuario();//bg.tvNombreUsuario.getText().toString();
        db = conn.getWritableDatabase();

        //Todo:MAXIMO REGISTRO
        try{
            sql = "SELECT MAX(ABS(" + dbE.c_rfId + "))  FROM " + dbE.t_RFomites + " WHERE " + dbE.c_rfFecha + "='" + bg.tvFecha.getText().toString() + "';";
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

            //Todo:Validamos campo vacio de Observacion:
            sObservacion = bg.etObservacion.getText().toString();
            if (sObservacion.equals("")){
                sValidaReg = "Sin Dato";
            }else{
                sValidaReg = "Dato";
            }
            //Todo:Agregamos campos generales a la Tabla

            sFecha = bg.tvFecha.getText().toString();
            sCencos = bg.etCencos.getText().toString().substring(0,6);
            sTurno = bg.spnTurno.getText().toString();
            sReferencia = ReferenciaList.get(iPosiRef).get_descripcion();
            sProceso = ProcesoList.get(iPosiPro).get_descripcion();

            for (int i=1;i<=7; i++) {
                switch (i) {
                    case 1:
                        sDepredador = "Zorro";
                        sTipo = bg.spnZorro.getSelectedItem().toString();
                        sValidaReg = "Dato";
                        if (sTipo.equals("Selec")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sCantidad = bg.etZorro.getText().toString();
                            sHora = bg.etZorroHora.getText().toString();
                            sEstado = bg.swtZorro.getText().toString();
                        }
                        break;
                    case 2:
                        sDepredador = "Perro";
                        sTipo = bg.spnPerro.getSelectedItem().toString();
                        sValidaReg = "Dato";
                        if (sTipo.equals("Selec")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sCantidad = bg.etPerro.getText().toString();
                            sHora = bg.etPerroHora.getText().toString();
                            sEstado = bg.swtPerro.getText().toString();
                        }
                        break;
                    case 3:
                        sDepredador = "Gallinazo";
                        sTipo = bg.spnGallinazo.getSelectedItem().toString();
                        sValidaReg = "Dato";
                        if (sTipo.equals("Selec")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sCantidad = bg.etGallinazo.getText().toString();
                            sHora = bg.etGallinazoHora.getText().toString();
                            sEstado = bg.swtGallinazo.getText().toString();
                        }
                        break;
                    case 4:
                        sDepredador = "Lechuza";
                        sTipo = bg.spnLechuza.getSelectedItem().toString();
                        sValidaReg = "Dato";
                        if (sTipo.equals("Selec")){
                            sValidaReg="Sin Dato";
                        }else{
                            sCantidad = bg.etLechuza.getText().toString();
                            sHora = bg.etLechuzaHora.getText().toString();
                            sEstado = bg.swtLechuza.getText().toString();
                        }
                        break;
                    case 5:
                        sDepredador = "Gato";
                        sTipo = bg.spnGato.getSelectedItem().toString();
                        sValidaReg = "Dato";
                        if (sTipo.equals("Selec")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sCantidad = bg.etGato.getText().toString();
                            sHora = bg.etGatoHora.getText().toString();
                            sEstado = bg.swtGato.getText().toString();
                        }
                        break;
                    case 6:
                        sDepredador = "Conejo";
                        sTipo = bg.spnConejo.getSelectedItem().toString();
                        sValidaReg = "Dato";
                        if (sTipo.equals("Selec")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sCantidad = bg.etConejo.getText().toString();
                            sHora = bg.etConejoHora.getText().toString();
                            sEstado = bg.swtConejo.getText().toString();
                        }
                        break;
                    case 7:
                        sDepredador = "Paloma";
                        sTipo = bg.spnPaloma.getSelectedItem().toString();
                        sValidaReg = "Dato";
                        if (sTipo.equals("Selec")){
                            sValidaReg = "Sin Dato";
                        }else{
                            sCantidad = bg.etPaloma.getText().toString();
                            sHora = bg.etPalomaHora.getText().toString();
                            sEstado = bg.swtPaloma.getText().toString();
                        }
                        break;
                }
                if (sValidaReg.equals("Dato")){
                    String CodUser=hP.getCodigoUsuario();
                    try {
                        iId = iId + 1;
                       miTime= hM.gethoraActual();
                       miDate = hM.getfechaActual();

                        querys = "INSERT INTO " + dbE.t_RFomites +
                                "(" + dbE.c_rfId + "," + dbE.c_rfUsuario + "," + dbE.c_rfDate +
                                "," + dbE.c_rfTime + "," + dbE.c_rfFecha + "," + dbE.c_rfCencos +
                                "," + dbE.c_rfTurno + "," + dbE.c_rfReferencia + "," + dbE.c_rfProceso +
                                "," + dbE.c_rfNomColaborador+ "," + dbE.c_rfDepredador + "," + dbE.c_rfTipo +
                                "," + dbE.c_rfCantidad + "," + dbE.c_rfHora + "," + dbE.c_rfDentroCerco +
                                "," + dbE.c_rfObservacion + ")" +
                                "VALUES('" + iId.toString() + "','" + CodUser  + "','" + miDate +
                                "','" + miTime + "','" + sFecha + "','" + sCencos +
                                "','" + sTurno + "','" + sReferencia + "','" + sProceso +
                                "','"+ sColaborador + "','" + sDepredador + "','" + sTipo +
                                "','" + sCantidad + "','"+ sHora+"','" + sEstado +
                                "','"+sObservacion + "')";
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
        int count = bg.tlRegFomites.getChildCount();
        for (int i=1;i<count;i++) {
            View child = bg.tlRegFomites.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        db=conn.getReadableDatabase();
        //PARA FILTRAR TABLA
        sTurno = bg.spnTurno.getText().toString();
        sFecha = bg.tvFecha.getText().toString();
        try{
            sql = "SELECT " + dbE.c_rfId + "," + dbE.c_rfFecha + "," + dbE.c_rfTurno +
                    "," + dbE.c_rfReferencia + "," + dbE.c_rfProceso + "," + dbE.c_rfNomColaborador +
                    "," + dbE.c_rfDepredador + "," + dbE.c_rfTipo + "," + dbE.c_rfCantidad +
                    "," + dbE.c_rfHora + "," + dbE.c_rfDentroCerco + "," + dbE.c_rfObservacion +
                    " FROM " + dbE.t_RFomites +
                    " WHERE " + dbE.c_rfFecha + "='" + xFecha + "' AND " + dbE.c_rfCencos + "='" + xCencos + "'AND  "  + dbE.c_rfTurno + "='" + sTurno +
                    "'  ORDER BY " + dbE.c_rfFecha + "," + dbE.c_rfCencos + "," + dbE.c_rfTurno;
            Cursor cursor = db.rawQuery(sql,null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0));
                        sFecha = (cursor.getString(1));
                        sTurno = (cursor.getString(2));
                        sReferencia = (cursor.getString(3));
                        sProceso = (cursor.getString(4));
                        sColaborador = (cursor.getString(5));
                        sDepredador = (cursor.getString(6));
                        sTipo = (cursor.getString(7));
                        sCantidad = (cursor.getString(8));
                        sHora = (cursor.getString(9));
                        sEstado = (cursor.getString(10));
                        sObservacion = (cursor.getString(11));

                        String[] cadena = {sId,sFecha,sTurno,sReferencia, sProceso,sColaborador,sDepredador,sTipo,sCantidad,sHora,sEstado,sObservacion};
                        trFilas = new TableRow(getActivity().getBaseContext());
                        for (int i= 0;i<12;i++) {
                            textView = new TextView(getActivity().getBaseContext());
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextAppearance(getActivity(), R.style.estilo_celda_detalle);
                            textView.setBackgroundResource(R.drawable.tabla_celda);
                            textView.setText(cadena[i]);
                            trFilas.addView(textView);
                        }
                        bg.tlRegFomites.addView(trFilas);
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
        sFecha = bg.tvFecha.getText().toString();

        eRegEliminar regEliminar = null;
        regEliminarList = new ArrayList<eRegEliminar>();
        bg.spnEliminar.setAdapter(null);
        bg.spnEliminar.setText("Seleccione");
        bg.btnEliminar.setEnabled(false);
        iPosEli=-1;
        try{
            sql = "SELECT " + dbE.c_rfId + "," + dbE.c_rfFecha + "," + dbE.c_rfTurno +
                    "," + dbE.c_rfReferencia + "," + dbE.c_rfProceso + "," + dbE.c_rfNomColaborador +
                    "," + dbE.c_rfDepredador + "," + dbE.c_rfTipo + "," + dbE.c_rfCantidad +
                    "," + dbE.c_rfHora + "," + dbE.c_rfDentroCerco + "," + dbE.c_rfObservacion +
                    " FROM " + dbE.t_RFomites +
                    " WHERE " + dbE.c_rfFecha + "='" + xFecha + "' AND " + dbE.c_rfCencos + "='" + xCencos + "'AND  "  + dbE.c_rfTurno + "='" + sTurno +
                    "'  ORDER BY " + dbE.c_rfFecha + "," + dbE.c_rfCencos + "," + dbE.c_rfTurno;
            Cursor cursor = db.rawQuery(sql,null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    listaRegEliminar = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0));
                        sFecha = (cursor.getString(1));
                        sTurno = (cursor.getString(2));
                        sReferencia = (cursor.getString(3));
                        sProceso = (cursor.getString(4));
                        sColaborador = (cursor.getString(5));
                        sDepredador = (cursor.getString(6));
                        sTipo = (cursor.getString(7));
                        sCantidad = (cursor.getString(8));
                        sHora = (cursor.getString(9));
                        sEstado = (cursor.getString(10));
                        sObservacion = (cursor.getString(11));

                        regEliminar = new eRegEliminar();
                        regEliminar.set_id(sId);
                        regEliminar.set_descripcion(sTurno+"|"+sReferencia+"|"+sProceso);
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
            case R.id.swtZorro:
                if (bg.swtZorro.isChecked()) {
                    bg.swtZorro.setText("Si");
                } else {
                    bg.swtZorro.setText("No");
                }
                break;
            case R.id.swtPerro:
                if (bg.swtPerro.isChecked()) {
                    bg.swtPerro.setText("Si");
                } else {
                    bg.swtPerro.setText("No");
                }
                break;
            case R.id.swtGallinazo:
                if (bg.swtGallinazo.isChecked()) {
                    bg.swtGallinazo.setText("Si");
                } else {
                    bg.swtGallinazo.setText("No");
                }
                break;
            case R.id.swtLechuza:
                if (bg.swtLechuza.isChecked()) {
                    bg.swtLechuza.setText("Si");
                } else {
                    bg.swtLechuza.setText("No");
                }
                break;
            case R.id.swtGato:
                if (bg.swtGato.isChecked()) {
                    bg.swtGato.setText("Si");
                } else {
                    bg.swtGato.setText("No");
                }
                break;
            case R.id.swtConejo:
                if (bg.swtConejo.isChecked()) {
                    bg.swtConejo.setText("Si");
                } else {
                    bg.swtConejo.setText("No");
                }
                break;
            case R.id.swtPaloma:
                if (bg.swtPaloma.isChecked()) {
                    bg.swtPaloma.setText("Si");
                } else {
                    bg.swtPaloma.setText("No");
                }
                break;
            case R.id.btnAgregar:
                sFecha = bg.tvFecha.getText().toString();
                sCencos = bg.etCencos.getText().toString().substring(0, 6);
                if (iPosiTur == -1) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta seleccionar Turno!!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (iPosiRef == -1) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta seleccionar una Referencia!!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (iPosiPro == -1) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta seleccionar un Proceso!!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if ((bg.spnZorro.getSelectedItemPosition() > 0)) {
                    if (bg.etZorro.getText().toString().equals("") || bg.etZorroHora.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta completar campos!!", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{guardarDatos(); mostrarTotales(sFecha, sCencos); listaItems(sFecha, sCencos);inicializarCampos();}
                }else if ((bg.spnPerro.getSelectedItemPosition() > 0)) {
                    if (bg.etPerro.getText().toString().equals("") || bg.etPerroHora.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta completar campos!!", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{guardarDatos(); mostrarTotales(sFecha, sCencos); listaItems(sFecha, sCencos);inicializarCampos();}
                }else if ((bg.spnGallinazo.getSelectedItemPosition() > 0)) {
                    if (bg.etGallinazo.getText().toString().equals("") || bg.etGallinazoHora.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta completar campos!!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else { guardarDatos(); mostrarTotales(sFecha, sCencos); listaItems(sFecha, sCencos);inicializarCampos();}
                }else if ((bg.spnGallinazo.getSelectedItemPosition() > 0)) {
                    if (bg.etGallinazo.getText().toString().equals("") || bg.etGallinazoHora.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta completar campos!!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else { guardarDatos(); mostrarTotales(sFecha, sCencos); listaItems(sFecha, sCencos);inicializarCampos();}
                }else if ((bg.spnLechuza.getSelectedItemPosition() > 0)) {
                    if (bg.etLechuza.getText().toString().equals("") || bg.etLechuzaHora.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta completar campos!!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else { guardarDatos(); mostrarTotales(sFecha, sCencos); listaItems(sFecha, sCencos);inicializarCampos();}
                }else if ((bg.spnGato.getSelectedItemPosition() > 0)) {
                    if (bg.etGato.getText().toString().equals("") || bg.etGatoHora.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta completar campos!!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else { guardarDatos(); mostrarTotales(sFecha, sCencos); listaItems(sFecha, sCencos);inicializarCampos();}
                }else if ((bg.spnConejo.getSelectedItemPosition() > 0)) {
                    if (bg.etConejo.getText().toString().equals("") || bg.etConejoHora.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta completar campos!!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else { guardarDatos(); mostrarTotales(sFecha, sCencos);listaItems(sFecha, sCencos); inicializarCampos();}
                }else if ((bg.spnPaloma.getSelectedItemPosition() > 0)) {
                    if (bg.etPaloma.getText().toString().equals("") || bg.etPalomaHora.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Falta completar campos!!", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        guardarDatos();
                        inicializarCampos();
                        mostrarTotales(sFecha, sCencos);
                        listaItems(sFecha, sCencos);
                    }
                }
        }
    }




}