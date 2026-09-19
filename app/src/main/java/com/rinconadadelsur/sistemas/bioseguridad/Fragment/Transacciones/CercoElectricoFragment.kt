package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transacciones

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
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
import androidx.fragment.app.Fragment
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.PhotoUploadHelper
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.RegistroCabeceraHelper
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.TransactionFormHost
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentCercoElectricoBinding
import java.util.Calendar
import kotlin.math.max

class CercoElectricoFragment  /*<!-- TODO: LISTAS-ENTIDADES -->*/
    : Fragment(), View.OnClickListener, TransactionFormHost {
    private var bg: FragmentCercoElectricoBinding? = null
    private lateinit var photoUpload: PhotoUploadHelper

    /*<!-- TODO: VARIABLES G -->*/
    var iHora: Int? = null
    var iMinutos: Int? = null
    var iId: Int? = null
    var iPosiTur: Int = -1
    var iPosEli: Int = -1

    /*<!-- TODO: VARIABLES CABECERA -->*/
    var sFechaActual: String? = null
    var sNombreUsuario: String? = null
    var sSerieDispositivo: String? = null
    var sDoc: String? = null
    var sSerie: String? = null
    var sNumero: String? = null
    var miDate: String? = null
    var miTime: String? = null
    var insertar: String? = null

    var mifectra: String? = null
    var sHora: String? = null
    var sMinutos: String? = null
    var sql: String? = null
    var sEstadoDer: String? = null
    var sEstadoIzq: String? = null
    var sValidaReg: String? = null
    var sId: String? = null
    var sFecha: String? = null
    var sCencos: String? = null
    var sTurno: String? = null
    var sEstadoD: String? = null
    var sEstadoI: String? = null
    var sEvaluador: String? = null
    var sHoraI: String? = null
    var sHoraF: String? = null
    var sEstado: String? = null
    var sComponente: String? = null
    var sDescripcion: String? = null
    var sOtros: String? = null
    var sDescripcionDer: String? = null
    var sDescripcionIzq: String? = null


    /*<!-- TODO: VARIABLES G PARA MOSTRAR -->*/ /*<!-- TODO: TABLA -->*/
    var trFilas: TableRow? = null
    var textView: TextView? = null

    /*<!-- TODO: HERRAMIENTAS -->*/
    var hP: hProcedimiento? = null
    var hM: hMetodos? = null
    var hV: hVariables? = null

    /*<!-- TODO: CONEXION -->*/
var conn: ConexionSQLiteHelper? = null
    var db: SQLiteDatabase? = null
    var querys: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoUpload = PhotoUploadHelper(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cerco_electrico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bg = FragmentCercoElectricoBinding.bind(view)
        photoUpload.bind(view)

        /*<!-- TODO: DATA -->*/
        hP = hProcedimiento(getContext(), dbEstructura.miBaseDatos, null, 1)
        conn = hProcedimiento(getContext(), dbEstructura.miBaseDatos, null, 1)
        hM = hMetodos()
        hV = hVariables()

        /*<!-- TODO: PROCEDEMIENTOS -->*/
        sFechaActual = hMetodos.getfechaActual()
        sSerieDispositivo = hP!!.getSerieDispositivo()

        /*<!-- TODO: ASIGNAR VARIABLES -->*/
        bg!!.registroCabecera.tvFecha.setText(sFechaActual)


        RegistroCabeceraHelper.bindFecha(this, bg!!.registroCabecera.tvFecha)
        RegistroCabeceraHelper.bindCencosDropdown(this, conn!!, bg!!.registroCabecera.etCencos, null)

        /*<!-- TODO: BOTONES -->*/
        //bg.swtEstadoDer.setOnClickListener(this);
        //bg.swtEstadoIzq.setOnClickListener(this);
        bg!!.swtArgolla.setOnClickListener(this)
        bg!!.swtBateria.setOnClickListener(this)
        bg!!.swtFocoTablero.setOnClickListener(this)
        bg!!.swtLineas.setOnClickListener(this)
        bg!!.swtPina.setOnClickListener(this)
        bg!!.swtSirena.setOnClickListener(this)
        bg!!.swtTablero.setOnClickListener(this)
        bg!!.swtTempladorAislador.setOnClickListener(this)
        bg!!.swtTransformador.setOnClickListener(this)
        bg!!.swtOtros.setOnClickListener(this)
        bg!!.btnAgregar.setOnClickListener(this)

        /*<!-- TODO: CARGA DE DATA SPINNER ARRAY -->*/
        val listt: MutableList<String?> = ArrayList<String?>()
        listt.add("Dia")
        listt.add("Noche")
        val adaptert = ArrayAdapter<String?>(getContext()!!, R.layout.items_list, listt)
        adaptert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bg!!.spnTurno.setAdapter<ArrayAdapter<String?>?>(adaptert)

        val listt2: MutableList<String?> = ArrayList<String?>()
        listt2.add("Operativo")
        listt2.add("Inoperativo")
        val adaptert2 = ArrayAdapter<String?>(getContext()!!, R.layout.items_list, listt2)
        adaptert2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bg!!.spnDerecha.setAdapter<ArrayAdapter<String?>?>(adaptert2)
        bg!!.spnIzquierda.setAdapter<ArrayAdapter<String?>?>(adaptert2)

        /*<!-- TODO: CAPTURA DE POSICION EXCLUYENDO LA PRIMERA -->*/
        bg!!.spnTurno.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                iPosiTur = position
                sFecha = bg!!.registroCabecera.tvFecha.getText().toString()
                sCencos = bg!!.registroCabecera.etCencos.getText().toString().substring(0, 6)
                mostrarTotales(sFecha, sCencos)
            }
        })


        /*<!-- TODO: GENERAR FORMATO PARA CAPTURA DE HORA -->*/
        bg!!.etHoraIni.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etHoraIni.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })

        bg!!.etHoraFin.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etHoraFin.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })


        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/
        try {
            val cadena1 = arrayOf<String?>(
                "ID",
                "Fecha",
                "Turno",
                "Est-Derecho",
                "Est-Izquierdo",
                "Evaluador",
                "Componente",
                "Descripción",
                "Falló?",
                "Inicio",
                "Fin"
            )
            trFilas = TableRow(getActivity()!!.getBaseContext())
            var textView: TextView?
            for (i in 0..10) {
                textView = TextView(getActivity()!!.getBaseContext())
                textView.setGravity(Gravity.CENTER_HORIZONTAL)
                textView.setTextAppearance(getActivity(), R.style.estilo_celda)
                textView.setBackgroundResource(R.drawable.tabla_celda_cabecera)
                textView.setText(cadena1[i])
                trFilas!!.addView(textView)
            }
            bg!!.tlRegCercoElectrico.addView(trFilas)
        } catch (e: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                e.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }


        /*<!-- TODO: AL INGRESAR UN DATO -->*/
        bg!!.spnTurno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                bg!!.tvTurno.setErrorEnabled(false)
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })


        /*<!-- TODO: DESACTIVAR LOS CAMPOS CON SWITCHS -->*/
        bg!!.etArgolla.setEnabled(false)
        bg!!.etBateria.setEnabled(false)
        bg!!.etFocoTablero.setEnabled(false)
        bg!!.etLineas.setEnabled(false)
        bg!!.etPina.setEnabled(false)
        bg!!.etSirena.setEnabled(false)
        bg!!.etTablero.setEnabled(false)
        bg!!.etTempladorAislador.setEnabled(false)
        bg!!.etTransformador.setEnabled(false)
        bg!!.etOtros.setEnabled(false)
        bg!!.etDescOtros.setEnabled(false)
    }

    private fun inicializarCampos() {
        bg!!.spnTurno.setSelection(0)
        bg!!.spnTurno.setEnabled(true)
        for (i in 1..12) {
            when (i) {
                3 -> {
                    sComponente = "Argolla"
                    sEstado = bg!!.swtArgolla.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etArgolla.setText("")
                        bg!!.etArgolla.setEnabled(false)
                        bg!!.swtArgolla.setChecked(false)
                        bg!!.swtArgolla.setText("No")
                    }
                }

                4 -> {
                    sComponente = "Bateria"
                    sEstado = bg!!.swtBateria.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etBateria.setText("")
                        bg!!.etBateria.setEnabled(false)
                        bg!!.swtBateria.setChecked(false)
                        bg!!.swtBateria.setText("No")
                    }
                }

                5 -> {
                    sComponente = "Foco de Tablero"
                    sEstado = bg!!.swtFocoTablero.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etFocoTablero.setText("")
                        bg!!.etFocoTablero.setEnabled(false)
                        bg!!.swtFocoTablero.setChecked(false)
                        bg!!.swtFocoTablero.setText("No")
                    }
                }

                6 -> {
                    sComponente = "Lineas"
                    sEstado = bg!!.swtLineas.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etLineas.setText("")
                        bg!!.etLineas.setEnabled(false)
                        bg!!.swtLineas.setChecked(false)
                        bg!!.swtLineas.setText("No")
                    }
                }

                7 -> {
                    sComponente = "Piña"
                    sEstado = bg!!.swtPina.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etPina.setText("")
                        bg!!.etPina.setEnabled(false)
                        bg!!.swtPina.setChecked(false)
                        bg!!.swtPina.setText("No")
                    }
                }

                8 -> {
                    sComponente = "Sirena"
                    sEstado = bg!!.swtSirena.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etSirena.setText("")
                        bg!!.etSirena.setEnabled(false)
                        bg!!.swtSirena.setChecked(false)
                        bg!!.swtSirena.setText("No")
                    }
                }

                9 -> {
                    sComponente = "Tablero"
                    sEstado = bg!!.swtTablero.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etTablero.setText("")
                        bg!!.etTablero.setEnabled(false)
                        bg!!.swtTablero.setChecked(false)
                        bg!!.swtTablero.setText("No")
                    }
                }

                10 -> {
                    sComponente = "Templador Aislador"
                    sEstado = bg!!.swtTempladorAislador.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etTempladorAislador.setText("")
                        bg!!.etTempladorAislador.setEnabled(false)
                        bg!!.swtTempladorAislador.setChecked(false)
                        bg!!.swtTempladorAislador.setText("No")
                    }
                }

                11 -> {
                    sComponente = "Transformador"
                    sEstado = bg!!.swtTransformador.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etTransformador.setText("")
                        bg!!.etTransformador.setEnabled(false)
                        bg!!.swtTransformador.setChecked(false)
                        bg!!.swtTransformador.setText("No")
                    }
                }

                12 -> {
                    sComponente = "Otros"
                    sEstado = bg!!.swtOtros.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etOtros.setText("")
                        bg!!.etOtros.setEnabled(false)
                        bg!!.etDescOtros.setText("")
                        bg!!.etDescOtros.setEnabled(false)
                        bg!!.swtOtros.setChecked(false)
                        bg!!.swtOtros.setText("No")
                    }
                }
            }
        }
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
                        bg!!.registroCabecera.etCencos.setText(cursor.getString(0) + " - " + cursor.getString(1))
                    }
                    cursor.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Código de la planta no existe",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    bg!!.registroCabecera.etCencos.setText("")
                }
            }
        } catch (ex: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                ex.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
            bg!!.registroCabecera.etCencos.setText("")
        }
    }

    private fun guardarDatos() {
        sFecha = bg!!.registroCabecera.tvFecha.getText().toString()
        sCencos = bg!!.registroCabecera.etCencos.getText().toString().substring(0, 6)
        sTurno = bg!!.spnTurno.getText().toString()
        sEvaluador = hP!!.getNombreUsuario() //bg.tvNombreUsuario.getText().toString();

        db = conn!!.getWritableDatabase()
        sql =
            "DELETE FROM " + dbEstructura.t_RCercoElectrico + " WHERE " + dbEstructura.c_rcFecha + "='" + sFecha + "' AND " + dbEstructura.c_rcTurno + "='" + sTurno + "'"
        db!!.execSQL(sql)
        db!!.close()

        db = conn!!.getWritableDatabase()
        //Todo:MAXIMO REGISTRO
        try {
            sql =
                "SELECT MAX(ABS(" + dbEstructura.c_rcId + "))  FROM " + dbEstructura.t_RCercoElectrico + " WHERE " + dbEstructura.c_rcFecha + "='" + sFecha + "';"
            val cursor1 = db!!.rawQuery(sql!!, null)
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


            //Todo:Validamos campos vacios de Estado:


            //Todo:Agregamos campos generales a la Tabla
            sHoraI = bg!!.etHoraIni.getText().toString()
            sHoraF = bg!!.etHoraFin.getText().toString()


            for (i in 1..10) {
                when (i) {
                    1 -> {
                        sComponente = "Argolla"
                        sEstado = bg!!.swtArgolla.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtArgolla.getText().toString()
                            sDescripcion = bg!!.etArgolla.getText().toString()
                        }
                    }

                    2 -> {
                        sComponente = "Bateria"
                        sEstado = bg!!.swtBateria.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtBateria.getText().toString()
                            sDescripcion = bg!!.etBateria.getText().toString()
                        }
                    }

                    3 -> {
                        sComponente = "Foco de Tablero"
                        sEstado = bg!!.swtFocoTablero.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtFocoTablero.getText().toString()
                            sDescripcion = bg!!.etFocoTablero.getText().toString()
                        }
                    }

                    4 -> {
                        sComponente = "Lineas"
                        sEstado = bg!!.swtLineas.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtLineas.getText().toString()
                            sDescripcion = bg!!.etLineas.getText().toString()
                        }
                    }

                    5 -> {
                        sComponente = "Piña"
                        sEstado = bg!!.swtPina.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtPina.getText().toString()
                            sDescripcion = bg!!.etPina.getText().toString()
                        }
                    }

                    6 -> {
                        sComponente = "Sirena"
                        sEstado = bg!!.swtSirena.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtSirena.getText().toString()
                            sDescripcion = bg!!.etSirena.getText().toString()
                        }
                    }

                    7 -> {
                        sComponente = "Tablero"
                        sEstado = bg!!.swtTablero.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtTablero.getText().toString()
                            sDescripcion = bg!!.etTablero.getText().toString()
                        }
                    }

                    8 -> {
                        sComponente = "Templador Aislador"
                        sEstado = bg!!.swtTempladorAislador.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtTempladorAislador.getText().toString()
                            sDescripcion = bg!!.etTempladorAislador.getText().toString()
                        }
                    }

                    9 -> {
                        sComponente = "Transformador"
                        sEstado = bg!!.swtTransformador.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtTransformador.getText().toString()
                            sDescripcion = bg!!.etTransformador.getText().toString()
                        }
                    }

                    10 -> if (bg!!.etOtros.getText().toString() == "") {
                        sValidaReg = "Sin Dato"
                    } else {
                        sOtros = bg!!.etOtros.getText().toString()
                        sComponente = sOtros
                        sEstado = bg!!.swtOtros.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sEstado = bg!!.swtOtros.getText().toString()
                            sDescripcion = bg!!.etDescOtros.getText().toString()
                        }
                    }
                }
                if (sValidaReg == "Dato") {
                    val CodUser = hP!!.getCodigoUsuario()
                    try {
                        iId = iId!! + 1
                        miTime = hMetodos.gethoraActual()
                        miDate = hMetodos.getfechaActual()
                        sDescripcionDer = bg!!.etMOtivoDer.getText().toString()
                        sDescripcionIzq = bg!!.etMOtivoIzq.getText().toString()
                        sEstadoD = bg!!.spnDerecha.getText().toString()
                        sEstadoI = bg!!.spnIzquierda.getText().toString()

                        insertar = "INSERT INTO " + dbEstructura.t_RCercoElectrico +
                                "(" + dbEstructura.c_rcId + "," + dbEstructura.c_rcUsuario + "," + dbEstructura.c_rcDate +
                                "," + dbEstructura.c_rcTime + "," + dbEstructura.c_rcFecha + "," + dbEstructura.c_rcCencos +
                                "," + dbEstructura.c_rcTurno + "," + dbEstructura.c_rcEstadoDerecho + "," + dbEstructura.c_rcDescEstadoDer +
                                "," + dbEstructura.c_rcEstadoIzquierdo + "," + dbEstructura.c_rcDescEstadoIzq + "," + dbEstructura.c_rcNomEvaluador +
                                "," + dbEstructura.c_rcHoraInicio + "," + dbEstructura.c_rcHoraFinal + "," + dbEstructura.c_rcComponente +
                                "," + dbEstructura.c_rcFallo + "," + dbEstructura.c_rcDescripcion + ")" +
                                "VALUES('" + iId.toString() + "','" + CodUser + "','" + miDate +
                                "','" + miTime + "','" + sFecha + "','" + sCencos +
                                "','" + sTurno + "','" + sEstadoD + "','" + sDescripcionDer +
                                "','" + sEstadoI + "','" + sDescripcionIzq + "','" + sEvaluador +
                                "','" + sHoraI + "','" + sHoraF + "','" + sComponente +
                                "','" + sEstado + "','" + sDescripcion + "')"
                        db!!.execSQL(insertar)
                    } catch (ex: Exception) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            ex.message,
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        db!!.close()
                    }
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

    private fun mostrarTotales(xFecha: String?, xCencos: String?) {
        val count = bg!!.tlRegCercoElectrico.getChildCount()
        for (i in 1..<count) {
            val child = bg!!.tlRegCercoElectrico.getChildAt(i)
            if (child is TableRow) (child as ViewGroup).removeAllViews()
        }
        db = conn!!.getReadableDatabase()
        //FILTRAR DATA DE TABLA
        sTurno = bg!!.spnTurno.getText().toString()
        try {
            sql =
                "SELECT " + dbEstructura.c_rcId + "," + dbEstructura.c_rcFecha + "," + dbEstructura.c_rcTurno +
                        "," + dbEstructura.c_rcEstadoDerecho + "," + dbEstructura.c_rcEstadoIzquierdo + "," + dbEstructura.c_rcNomEvaluador +
                        "," + dbEstructura.c_rcComponente + "," + dbEstructura.c_rcDescripcion + "," + dbEstructura.c_rcFallo +
                        "," + dbEstructura.c_rcHoraInicio + "," + dbEstructura.c_rcHoraFinal +
                        " FROM " + dbEstructura.t_RCercoElectrico +
                        " WHERE " + dbEstructura.c_rcFecha + "='" + xFecha + "' AND " + dbEstructura.c_rcCencos + "='" + xCencos + "' AND " + dbEstructura.c_rcTurno + "='" + sTurno +
                        "'  ORDER BY " + dbEstructura.c_rcFecha + "," + dbEstructura.c_rcCencos + "," + dbEstructura.c_rcTurno

            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0))
                        sFecha = (cursor.getString(1))
                        sTurno = (cursor.getString(2))
                        sEstadoD = (cursor.getString(3))
                        sEstadoI = (cursor.getString(4))
                        sEvaluador = (cursor.getString(5))
                        sComponente = (cursor.getString(6))
                        sDescripcion = (cursor.getString(7))
                        sEstado = (cursor.getString(8))
                        sHoraI = (cursor.getString(9))
                        sHoraF = (cursor.getString(10))

                        val cadena = arrayOf<String?>(
                            sId,
                            sFecha,
                            sTurno,
                            sEstadoD,
                            sEstadoI,
                            sEvaluador,
                            sComponente,
                            sDescripcion,
                            sEstado,
                            sHoraI,
                            sHoraF
                        )
                        trFilas = TableRow(getActivity()!!.getBaseContext())
                        for (i in 0..10) {
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
                        bg!!.tlRegCercoElectrico.addView(trFilas)
                    }
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Carga de datos exitosa",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
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


    override fun onClick(v: View) {
        when {
            v.id == R.id.swtArgolla -> if (bg!!.swtArgolla.isChecked()) {
                bg!!.swtArgolla.setText("Si")
                bg!!.etArgolla.setEnabled(true)
            } else {
                bg!!.swtArgolla.setText("No")
                bg!!.etArgolla.setEnabled(false)
                bg!!.etArgolla.setText("")
            }

            v.id == R.id.swtBateria -> if (bg!!.swtBateria.isChecked()) {
                bg!!.swtBateria.setText("Si")
                bg!!.etBateria.setEnabled(true)
            } else {
                bg!!.swtBateria.setText("No")
                bg!!.etBateria.setEnabled(false)
                bg!!.etBateria.setText("")
            }

            v.id == R.id.swtFocoTablero -> if (bg!!.swtFocoTablero.isChecked()) {
                bg!!.swtFocoTablero.setText("Si")
                bg!!.etFocoTablero.setEnabled(true)
            } else {
                bg!!.swtFocoTablero.setText("No")
                bg!!.etFocoTablero.setEnabled(false)
                bg!!.etFocoTablero.setText("")
            }

            v.id == R.id.swtLineas -> if (bg!!.swtLineas.isChecked()) {
                bg!!.swtLineas.setText("Si")
                bg!!.etLineas.setEnabled(true)
            } else {
                bg!!.swtLineas.setText("No")
                bg!!.etLineas.setEnabled(false)
                bg!!.etLineas.setText("")
            }

            v.id == R.id.swtPina -> if (bg!!.swtPina.isChecked()) {
                bg!!.swtPina.setText("Si")
                bg!!.etPina.setEnabled(true)
            } else {
                bg!!.swtPina.setText("No")
                bg!!.etPina.setEnabled(false)
                bg!!.etPina.setText("")
            }

            v.id == R.id.swtSirena -> if (bg!!.swtSirena.isChecked()) {
                bg!!.swtSirena.setText("Si")
                bg!!.etSirena.setEnabled(true)
            } else {
                bg!!.swtSirena.setText("No")
                bg!!.etSirena.setEnabled(false)
                bg!!.etSirena.setText("")
            }

            v.id == R.id.swtTablero -> if (bg!!.swtTablero.isChecked()) {
                bg!!.swtTablero.setText("Si")
                bg!!.etTablero.setEnabled(true)
            } else {
                bg!!.swtTablero.setText("No")
                bg!!.etTablero.setEnabled(false)
                bg!!.etTablero.setText("")
            }

            v.id == R.id.swtTempladorAislador -> if (bg!!.swtTempladorAislador.isChecked()) {
                bg!!.swtTempladorAislador.setText("Si")
                bg!!.etTempladorAislador.setEnabled(true)
            } else {
                bg!!.swtTempladorAislador.setText("No")
                bg!!.etTempladorAislador.setEnabled(false)
                bg!!.etTempladorAislador.setText("")
            }

            v.id == R.id.swtTransformador -> if (bg!!.swtTransformador.isChecked()) {
                bg!!.swtTransformador.setText("Si")
                bg!!.etTransformador.setEnabled(true)
            } else {
                bg!!.swtTransformador.setText("No")
                bg!!.etTransformador.setEnabled(false)
                bg!!.etTransformador.setText("")
            }

            v.id == R.id.swtOtros -> if (bg!!.swtOtros.isChecked()) {
                bg!!.swtOtros.setText("Si")
                bg!!.etOtros.setEnabled(true)
                bg!!.etDescOtros.setEnabled(true)
            } else {
                bg!!.swtOtros.setText("No")
                bg!!.etOtros.setEnabled(false)
                bg!!.etOtros.setText("")
                bg!!.etDescOtros.setEnabled(false)
                bg!!.etDescOtros.setText("")
            }

            v.id == R.id.btnAgregar -> {
                sFecha = bg!!.registroCabecera.tvFecha.getText().toString()
                sCencos = bg!!.registroCabecera.etCencos.getText().toString().substring(0, 6)
                if (iPosiTur == -1) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta seleccionar Turno!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.etHoraIni.getText().toString() == "") {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta elegir la Hora Inicial!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.etHoraFin.getText().toString() == "") {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta elegir la Hora Final!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtArgolla.getText().toString() == "Si" && (bg!!.etArgolla.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtBateria.getText().toString() == "Si" && (bg!!.etBateria.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtFocoTablero.getText()
                        .toString() == "Si" && (bg!!.etFocoTablero.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtLineas.getText().toString() == "Si" && (bg!!.etLineas.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtPina.getText().toString() == "Si" && (bg!!.etPina.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtSirena.getText().toString() == "Si" && (bg!!.etSirena.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtTablero.getText().toString() == "Si" && (bg!!.etTablero.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtTempladorAislador.getText()
                        .toString() == "Si" && (bg!!.etTempladorAislador.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtTransformador.getText()
                        .toString() == "Si" && (bg!!.etTransformador.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtOtros.getText().toString() == "Si" && (bg!!.etDescOtros.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    var sGuardar = "Si"
                    if (bg!!.spnIzquierda.getText().toString() == "Seleccione") {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta Escoger Estado Izquierdo!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        sGuardar = "No"
                    }
                    if (bg!!.spnDerecha.getText().toString() == "Seleccione") {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta Escoger Estado Derecho!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        sGuardar = "No"
                    }
                    if (sGuardar == "Si") {
                        guardarDatos()
                        mostrarTotales(sFecha, sCencos)
                        inicializarCampos()
                    }
                }
            }
        }
    }

    override fun formTitle(): String = "Resumen — Cerco eléctrico"

    override fun summaryLines(): List<Pair<String, String>> {
        val b = bg ?: return emptyList()
        return listOf(
            "Fecha" to b.registroCabecera.tvFecha.text.toString(),
            "Centro de costos" to b.registroCabecera.etCencos.text.toString(),
            "Turno" to b.spnTurno.text.toString(),
            "Estado izquierda" to b.spnIzquierda.text.toString(),
            "Estado derecha" to b.spnDerecha.text.toString(),
            "Hora inicio" to b.etHoraIni.text.toString(),
            "Hora fin" to b.etHoraFin.text.toString()
        )
    }

    override fun performSave(): Boolean {
        bg?.btnAgregar?.performClick()
        return true
    }
}