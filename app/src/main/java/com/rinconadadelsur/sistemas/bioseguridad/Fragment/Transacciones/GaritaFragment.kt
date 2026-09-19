package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transacciones

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.TableRow
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eAnomalias
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eRegEliminar
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentGaritaBinding
import java.util.Calendar
import kotlin.math.max

class GaritaFragment : Fragment() {
    private var bg: FragmentGaritaBinding? = null

    /*<!-- TODO: VARIABLES G -->*/
    var iHora: Int? = null
    var iMinutos: Int? = null
    var iId: Int? = null
    var iPosiAn: Int = -1
    var iPosiTur: Int = -1
    var iPosiFil: Int = -1
    var iPosEli: Int = -1

    /*<!-- TODO: VARIABLES CABECERA -->*/
    var sFechaActual: String? = null
    var sNombreUsuario: String? = null
    var sSerieDispositivo: String? = null
    var sDoc: String? = null
    var sSerie: String? = null
    var sNumero: String? = null

    var sHora: String? = null
    var sMinutos: String? = null
    var miDate: String? = null
    var miTime: String? = null
    var sDescripcion: String? = null
    var sAgenteCausal: String? = null
    var sValidaReg: String? = null
    var sFecha: String? = null
    var sId: String? = null
    var sCencos: String? = null
    var sColaborador: String? = null
    var sTurno: String? = null
    var sFiltro: String? = null
    var sAnomalia: String? = null


    /*<!-- TODO: VARIABLES G PARA MOSTRAR -->*/ /*<!-- TODO: TABLA -->*/
    var trFilas: TableRow? = null
    var textView: TextView? = null

    /*<!-- TODO: HERRAMIENTAS -->*/
    var hP: hProcedimiento? = null
    var hM: hMetodos? = null
    var hV: hVariables? = null

    /*<!-- TODO: CONEXION -->*/
    var dbE: dbEstructura? = null
    var conn: ConexionSQLiteHelper? = null
    var db: SQLiteDatabase? = null
    var querys: String? = null

    /*<!-- TODO: LISTAS-ENTIDADES -->*/
    var listaAnomalias: ArrayList<String?>? = null
    var anomaliasList: ArrayList<eAnomalias?>? = null

    var listaRegEliminar: ArrayList<String?>? = null
    var regEliminarList: ArrayList<eRegEliminar?>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bg = FragmentGaritaBinding.bind(view)

        /*<!-- TODO: DATA -->*/
        hP = hProcedimiento(getContext(), dbEstructura.miBaseDatos, null, 1)
        conn = hProcedimiento(getContext(), dbEstructura.miBaseDatos, null, 1)
        hM = hMetodos()
        hV = hVariables()

        /*<!-- TODO: PROCEDEMIENTOS -->*/
        sFechaActual = hMetodos.getfechaActual()
        sNombreUsuario = hP!!.getNombreUsuario()
        sSerieDispositivo = hP!!.getSerieDispositivo()

        /*<!-- TODO: ASIGNAR VARIABLES -->*/
        bg!!.tvFecha.setText(sFechaActual)

        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/
        try {
            val cadena = arrayOf<String?>(
                "ID",
                "Fecha",
                "Turno",
                "# Filtro",
                "Anomalía",
                "Hora",
                "Descripción",
                "Agente Causal"
            )
            trFilas = TableRow(getActivity()!!.getBaseContext())
            for (i in 0..7) {
                textView = TextView(getActivity()!!.getBaseContext())
                textView!!.setGravity(Gravity.CENTER_HORIZONTAL)
                textView!!.setTextAppearance(getActivity(), R.style.estilo_celda)
                textView!!.setBackgroundResource(R.drawable.tabla_celda_cabecera)
                textView!!.setText(cadena[i])
                trFilas!!.addView(textView)
            }
            bg!!.tlRegGarita.addView(trFilas)
        } catch (e: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                e.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }



        buscarCencos()
        buscarAnomalias()

        /*<!-- TODO: CARGA DE DATA SPINNER ARRAY -->*/
        val listt: MutableList<String?> = ArrayList<String?>()
        //listt.add("Seleccione");
        listt.add("Dia")
        listt.add("Noche")
        val adaptert = ArrayAdapter<String?>(getContext()!!, R.layout.items_list, listt)
        adaptert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bg!!.spnTurno.setAdapter<ArrayAdapter<String?>?>(adaptert)

        val listf: MutableList<String?> = ArrayList<String?>()
        //listf.add("Seleccione");
        listf.add("1")
        listf.add("2")
        listf.add("3")
        val adapterf = ArrayAdapter<String?>(getContext()!!, R.layout.items_list, listf)
        adapterf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bg!!.spnNumFiltro.setAdapter<ArrayAdapter<String?>?>(adapterf)

        /*<!-- TODO: CAPTURA DE POSICION EXCLUYENDO LA PRIMERA -->*/
        bg!!.spnTurno.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                iPosiTur = position
            }
        })
        bg!!.spnNumFiltro.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                iPosiFil = position
            }
        })

        bg!!.spnEliminar.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                iPosEli = position
                bg!!.btnEliminar.setEnabled(true)
            }
        })



        bg!!.spnAnomalia.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                iPosiAn = position
            }
        })

        bg!!.spnTurno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                bg!!.tvTurno.setErrorEnabled(false)

                sFecha = bg!!.tvFecha.getText().toString()
                sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
                mostrarTotales(sFecha, sCencos)
                listaItems(sFecha, sCencos)
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
        bg!!.spnNumFiltro.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                bg!!.tvNumFiltro.setErrorEnabled(false)

                sFecha = bg!!.tvFecha.getText().toString()
                sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
                mostrarTotales(sFecha, sCencos)
                listaItems(sFecha, sCencos)
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
        bg!!.spnAnomalia.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                bg!!.tvAnomalia.setErrorEnabled(false)
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
        bg!!.etHora.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                bg!!.tvHora.setErrorEnabled(false)
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        /*<!-- TODO: GENERAR FORMATO PARA CAPTURA DE HORA -->*/
        bg!!.etHora.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val c = Calendar.getInstance()
                iHora = c.get(Calendar.HOUR_OF_DAY)
                iMinutos = c.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(getContext(), object : OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        sHora = "00" + hourOfDay
                        sHora = sHora!!.substring(max(0, sHora!!.length - 2))
                        sMinutos = "00" + minute
                        sMinutos = sMinutos!!.substring(max(0, sMinutos!!.length - 2))
                        bg!!.etHora.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })


        /*<!-- TODO: CLICK AL BOTON AGREGAR -->*/
        bg!!.btnAgregar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                sFecha = bg!!.tvFecha.getText().toString()
                sCencos = bg!!.etCencos.getText().toString().substring(0, 6)


                var bValidaCampos = true
                if (iPosiTur == -1) {
                    bValidaCampos = false
                    bg!!.tvTurno.setError("Turno Ivalido!")
                    bg!!.spnTurno.setError(null)
                } else {
                    bg!!.tvTurno.setError(null)
                }

                if (iPosiFil == -1) {
                    bValidaCampos = false
                    bg!!.tvNumFiltro.setError("N°Filtro Ivalido!")
                    bg!!.spnNumFiltro.setError(null)
                } else {
                    bg!!.tvNumFiltro.setError(null)
                }

                if (iPosiAn == -1) {
                    bValidaCampos = false
                    bg!!.tvAnomalia.setError("Anomalía Ivalida!")
                    bg!!.spnAnomalia.setError(null)
                } else {
                    bg!!.tvAnomalia.setError(null)
                }

                if (bg!!.etHora.getText().toString().trim { it <= ' ' }
                        .equals("00:00", ignoreCase = true)) {
                    bValidaCampos = false
                    bg!!.tvHora.setError("Ingrese Hora!")
                    bg!!.etHora.setError(null)
                } else {
                    bg!!.tvHora.setError(null)
                }

                if (bg!!.etDescripcion.getText().toString() == "") {
                    bValidaCampos = false
                    //bg.tvDescripcion.setError("Ingrese la descripción!");
                    bg!!.etDescripcion.setError("Ingrese la descripción!")
                }
                if (bg!!.etAgenteCausal.getText().toString() == "") {
                    bValidaCampos = false
                    //bg.tvAgenteCausal.setError("Ingrese el Agente Causal!");
                    bg!!.etAgenteCausal.setError("Ingrese el Agente Causal!")
                }

                if (bValidaCampos == true) {
                    guardarDatos()
                    mostrarTotales(sFecha, sCencos)
                    listaItems(sFecha, sCencos)
                    inicializarCampos()
                }
            }
        })

        bg!!.btnEliminar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                bg!!.btnEliminar.setEnabled(false)


                val dialogo1 = AlertDialog.Builder(view.getContext()) //this
                dialogo1.setTitle("Eliminar")
                dialogo1.setMessage("¿ Desea Eliminar dicho registro ?")
                dialogo1.setCancelable(false)
                dialogo1.setPositiveButton("Confirmar", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogo1: DialogInterface?, id: Int) {
                        val toast: Toast
                        if (iPosEli == -1 || bg!!.spnEliminar.getText().toString() == "") {
                            toast = Toast.makeText(
                                getActivity()!!.getApplicationContext(),
                                "Item Seleccionado No valido",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        } else {
                            //Eliminar Registro
                            val slFecha = bg!!.tvFecha.getText().toString()
                            val slCencos = bg!!.etCencos.getText().toString().substring(0, 6)
                            val slId = regEliminarList!!.get(iPosEli)!!.get_id()

                            val rMensaje = hP!!.getElimarRegistro(
                                dbEstructura.t_RGarita,
                                dbEstructura.c_rgFecha,
                                slFecha,
                                dbEstructura.c_rgCencos,
                                slCencos,
                                dbEstructura.c_rgId,
                                slId
                            )
                            toast = Toast.makeText(
                                getActivity()!!.getApplicationContext(),
                                rMensaje,
                                Toast.LENGTH_SHORT
                            )
                            toast.show()


                            sFecha = bg!!.tvFecha.getText().toString()
                            sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
                            mostrarTotales(sFecha, sCencos)
                            listaItems(sFecha, sCencos)
                        }
                    }
                })
                dialogo1.setNegativeButton("Cancelar", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogo1: DialogInterface?, id: Int) {
                        //cancelar();
                    }
                })
                dialogo1.show()
            }
        })
    }

    private fun inicializarCampos() {
        //bg.spnTurno.setText("");
        //bg.spnNumFiltro.setText("");
        //bg.spnAnomalia.setText("");
        //bg.etHora.setText("00:00");

        bg!!.etDescripcion.setText("")
        bg!!.etAgenteCausal.setText("")
    }

    private fun buscarCencos() {
        db = conn!!.getReadableDatabase()

        try {
            querys =
                "SELECT " + dbEstructura.c_cCodigo + "," + dbEstructura.c_cNombre + " FROM " + dbEstructura.t_Cencos + " "
            val cursor = db!!.rawQuery(querys!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bg!!.etCencos.setText(cursor.getString(0) + "|" + cursor.getString(1))
                    }
                    cursor.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Cencos no existe",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    bg!!.etCencos.setText("")
                }
            }
        } catch (ex: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                ex.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
            bg!!.etCencos.setText("")
        }
    }

    private fun buscarAnomalias() {
        db = conn!!.getReadableDatabase()
        var anomalias: eAnomalias? = null
        anomaliasList = ArrayList<eAnomalias?>()
        try {
            querys =
                "SELECT * FROM " + dbEstructura.t_AnomaliaGarita + "  ORDER BY " + dbEstructura.c_anDescripcion + ";"
            val cursor = db!!.rawQuery(querys!!, null)
            if (cursor != null) {
                listaAnomalias = ArrayList<String?>()
                //listaAnomalias.add("Seleccione");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        anomalias = eAnomalias()
                        anomalias.set_codigo(cursor.getString(0))
                        anomalias.set_descripcion(cursor.getString(1))
                        anomaliasList!!.add(anomalias)
                    }

                    for (i in anomaliasList!!.indices) {
                        listaAnomalias!!.add(anomaliasList!!.get(i)!!.get_descripcion())
                    }
                    val adaptador: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listaAnomalias)
                    bg!!.spnAnomalia.setAdapter<ArrayAdapter<CharSequence?>?>(adaptador)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "No existen anomalías",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
        } catch (ex: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                ex.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    private fun guardarDatos() {
        db = conn!!.getWritableDatabase()

        //Todo:MAXIMO REGISTRO
        try {
            querys =
                "SELECT MAX(ABS(" + dbEstructura.c_rgId + "))  FROM " + dbEstructura.t_RGarita + " WHERE " + dbEstructura.c_rgFecha + "='" + bg!!.tvFecha.getText()
                    .toString() + "';"
            val cursor1 = db!!.rawQuery(querys!!, null)
            if (cursor1 != null) {
                if (cursor1.getCount() > 0) {
                    while (cursor1.moveToNext()) {
                        iId = cursor1.getInt(0)
                    }
                    cursor1.close()
                } else {
                    iId = 0
                }
            }

            //Todo:Validamos campos vacios del fragment:
            sDescripcion = bg!!.etDescripcion.getText().toString()
            sAgenteCausal = bg!!.etAgenteCausal.getText().toString()


            //Todo:Agregamos campos generales a la Tabla
            sFecha = bg!!.tvFecha.getText().toString()
            sColaborador = hP!!.getNombreUsuario() //bg.tvNombreUsuario.getText().toString();
            sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
            sTurno = bg!!.spnTurno.getText().toString()
            sFiltro = bg!!.spnNumFiltro.getText().toString()
            sAnomalia = bg!!.spnAnomalia.getText()
                .toString() //anomaliasList.get(iPosiAn).get_descripcion();
            sHora = bg!!.etHora.getText().toString()

            val CodUser = hP!!.getCodigoUsuario()
            try {
                iId = iId!! + 1
                miTime = hMetodos.gethoraActual()
                miDate = hMetodos.getfechaActual()

                querys = "INSERT INTO " + dbEstructura.t_RGarita +
                        "(" + dbEstructura.c_rgId + "," + dbEstructura.c_rgUsuario + "," + dbEstructura.c_rgDate +
                        "," + dbEstructura.c_rgTime + "," + dbEstructura.c_rgFecha + "," + dbEstructura.c_rgCencos +
                        "," + dbEstructura.c_rgNomColaborador + "," + dbEstructura.c_rgTurno + "," + dbEstructura.c_rgNumFiltro +
                        "," + dbEstructura.c_rgAnomalia + "," + dbEstructura.c_rgHora + "," + dbEstructura.c_rgDescripcion +
                        "," + dbEstructura.c_rgAgenteCausal + ")" +
                        "VALUES('" + iId.toString() + "','" + CodUser + "','" + miDate +
                        "','" + miTime + "','" + sFecha + "','" + sCencos +
                        "','" + sColaborador + "','" + sTurno + "','" + sFiltro +
                        "','" + sAnomalia + "','" + sHora + "','" + sDescripcion +
                        "','" + sAgenteCausal + "')"
                db!!.execSQL(querys)
            } catch (ex: Exception) {
                val toast = Toast.makeText(
                    getActivity()!!.getApplicationContext(),
                    ex.message,
                    Toast.LENGTH_SHORT
                )
                toast.show()
                db!!.close()
            }
        } catch (e: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                e.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    private fun mostrarTotales(xFecha: String?, xCencos: String?) {
        val count = bg!!.tlRegGarita.getChildCount()
        for (i in 1..<count) {
            val child = bg!!.tlRegGarita.getChildAt(i)
            if (child is TableRow) (child as ViewGroup).removeAllViews()
        }
        db = conn!!.getReadableDatabase()
        //PARA FILTRAR TABLA
        sTurno = bg!!.spnTurno.getText().toString()
        sFiltro = bg!!.spnNumFiltro.getText().toString()
        iPosEli = -1
        try {
            querys =
                "SELECT " + dbEstructura.c_rgId + "," + dbEstructura.c_rgFecha + "," + dbEstructura.c_rgNomColaborador +
                        "," + dbEstructura.c_rgTurno + "," + dbEstructura.c_rgNumFiltro + "," + dbEstructura.c_rgAnomalia +
                        "," + dbEstructura.c_rgHora + "," + dbEstructura.c_rgDescripcion + "," + dbEstructura.c_rgAgenteCausal +
                        " FROM " + dbEstructura.t_RGarita +
                        " WHERE " + dbEstructura.c_rgFecha + "='" + xFecha + "' AND " + dbEstructura.c_rgCencos + "='" + xCencos +
                        "' AND  " + dbEstructura.c_rgTurno + "='" + sTurno +
                        "' AND  " + dbEstructura.c_rgNumFiltro + "='" + sFiltro +
                        "'  ORDER BY " + dbEstructura.c_rgFecha + "," + dbEstructura.c_rgTurno + "," + dbEstructura.c_rgNumFiltro + "," + dbEstructura.c_rgAnomalia

            val cursor = db!!.rawQuery(querys!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0))
                        sFecha = (cursor.getString(1))
                        sColaborador = (cursor.getString(2))
                        sTurno = (cursor.getString(3))
                        sFiltro = (cursor.getString(4))
                        sAnomalia = (cursor.getString(5))
                        sHora = (cursor.getString(6))
                        sDescripcion = (cursor.getString(7))
                        sAgenteCausal = (cursor.getString(8))

                        val cadena = arrayOf<String?>(
                            sId,
                            sFecha,
                            sTurno,
                            sFiltro,
                            sAnomalia,
                            sHora,
                            sDescripcion,
                            sAgenteCausal
                        )
                        trFilas = TableRow(getActivity()!!.getBaseContext())
                        for (i in 0..7) {
                            textView = TextView(getActivity()!!.getBaseContext())
                            textView!!.setGravity(Gravity.CENTER_HORIZONTAL)
                            textView!!.setTextAppearance(
                                getActivity(),
                                R.style.estilo_celda_detalle
                            )
                            textView!!.setBackgroundResource(R.drawable.tabla_celda)
                            textView!!.setText(cadena[i])
                            trFilas!!.addView(textView)
                        }
                        bg!!.tlRegGarita.addView(trFilas)
                    }
                    cursor.close()
                    db!!.close()
                }
            }
        } catch (e: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                e.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    private fun listaItems(xFecha: String?, xCencos: String?) {
        db = conn!!.getReadableDatabase()
        //PARA FILTRAR TABLA
        sTurno = bg!!.spnTurno.getText().toString()
        sFiltro = bg!!.spnNumFiltro.getText().toString()

        var regEliminar: eRegEliminar? = null
        regEliminarList = ArrayList<eRegEliminar?>()
        bg!!.spnEliminar.setAdapter(null)
        bg!!.spnEliminar.setText("Seleccione")
        bg!!.btnEliminar.setEnabled(false)
        iPosEli = -1
        try {
            querys =
                "SELECT " + dbEstructura.c_rgId + "," + dbEstructura.c_rgFecha + "," + dbEstructura.c_rgNomColaborador +
                        "," + dbEstructura.c_rgTurno + "," + dbEstructura.c_rgNumFiltro + "," + dbEstructura.c_rgAnomalia +
                        "," + dbEstructura.c_rgHora + "," + dbEstructura.c_rgDescripcion + "," + dbEstructura.c_rgAgenteCausal +
                        " FROM " + dbEstructura.t_RGarita +
                        " WHERE " + dbEstructura.c_rgFecha + "='" + xFecha + "' AND " + dbEstructura.c_rgCencos + "='" + xCencos +
                        "' AND  " + dbEstructura.c_rgTurno + "='" + sTurno +
                        "' AND  " + dbEstructura.c_rgNumFiltro + "='" + sFiltro +
                        "'  ORDER BY " + dbEstructura.c_rgFecha + "," + dbEstructura.c_rgTurno + "," + dbEstructura.c_rgNumFiltro + "," + dbEstructura.c_rgAnomalia

            val cursor = db!!.rawQuery(querys!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    listaRegEliminar = ArrayList<String?>()
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0))
                        sFecha = (cursor.getString(1))
                        sColaborador = (cursor.getString(2))
                        sTurno = (cursor.getString(3))
                        sFiltro = (cursor.getString(4))
                        sAnomalia = (cursor.getString(5))

                        regEliminar = eRegEliminar()
                        regEliminar.set_id(sId)
                        regEliminar.set_descripcion(sTurno + "|" + sFiltro + "|" + sAnomalia)
                        regEliminarList!!.add(regEliminar)
                    }
                    for (i in regEliminarList!!.indices) {
                        listaRegEliminar!!.add(
                            regEliminarList!!.get(i)!!.get_id() + "|" + regEliminarList!!.get(i)!!
                                .get_descripcion()
                        )
                    }
                    val adaptador: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listaRegEliminar)
                    bg!!.spnEliminar.setAdapter<ArrayAdapter<CharSequence?>?>(adaptador)
                    cursor.close()
                    db!!.close()
                }
            }
        } catch (e: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                e.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }
}