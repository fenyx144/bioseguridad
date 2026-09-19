package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transacciones

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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eRegEliminar
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentMantenimientoBinding

class MantenimientoFragment  /*<!-- TODO: LISTAS-ENTIDADES -->*/
    : Fragment(), View.OnClickListener {
    private var bg: FragmentMantenimientoBinding? = null

    /*<!-- TODO: VARIABLES G -->*/
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

    var sql: String? = null
    var sColaborador: String? = null
    var miDate: String? = null
    var miTime: String? = null
    var sDescripcion: String? = null
    var sEstado: String? = null
    var sValidaReg: String? = null
    var sId: String? = null
    var sFecha: String? = null
    var sCencos: String? = null
    var sElemento: String? = null
    var sTurno: String? = null
    var sOtros: String? = null


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

    var listaRegEliminar: ArrayList<String?>? = null
    var regEliminarList: ArrayList<eRegEliminar?>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mantenimiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bg = FragmentMantenimientoBinding.bind(view)

        /*<!-- TODO: DATA -->*/
        hP = hProcedimiento(getContext(), dbEstructura.miBaseDatos, null, 1)
        conn = hProcedimiento(getContext(), dbEstructura.miBaseDatos, null, 1)
        hM = hMetodos()
        hV = hVariables()

        /*<!-- TODO: PROCEDEMIENTOS -->*/
        sFechaActual = hMetodos.getfechaActual()
        sSerieDispositivo = hP!!.getSerieDispositivo()

        /*<!-- TODO: ASIGNAR VARIABLES -->*/
        bg!!.tvFecha.setText(sFechaActual)

        buscarCencos()

        /*<!-- TODO: BOTONES -->*/
        bg!!.swtCajaFormol.setOnClickListener(this)
        bg!!.swtComedor.setOnClickListener(this)
        bg!!.swtDucha.setOnClickListener(this)
        bg!!.swtEstructuraGarita.setOnClickListener(this)
        bg!!.swtMedidorAgua.setOnClickListener(this)
        bg!!.swtPuertaPersonal.setOnClickListener(this)
        bg!!.swtPuertaPozoSeptico.setOnClickListener(this)
        bg!!.swtPuertaVehicular.setOnClickListener(this)
        bg!!.swtSSHH.setOnClickListener(this)
        bg!!.swtTableroControl.setOnClickListener(this)
        bg!!.swtTachoRopa.setOnClickListener(this)
        bg!!.swtTherma.setOnClickListener(this)
        bg!!.swtTuberiaAgua.setOnClickListener(this)
        bg!!.swtOtros.setOnClickListener(this)
        bg!!.btnAgregar.setOnClickListener(this)

        /*<!-- TODO: CARGA DE DATA SPINNER ARRAY -->*/
        val listt: MutableList<String?> = ArrayList<String?>()
        //listt.add("Seleccione");
        listt.add("Dia")
        listt.add("Noche")
        val adaptert = ArrayAdapter<String?>(getContext()!!, R.layout.items_list, listt)
        adaptert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bg!!.spnTurno.setAdapter<ArrayAdapter<String?>?>(adaptert)


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

        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/
        try {
            val cadena = arrayOf<String?>(
                "ID",
                "Fecha",
                "Turno",
                "Colaborador",
                "Elemento",
                "¿Fallo?",
                "Descripcion"
            )
            trFilas = TableRow(getActivity()!!.getBaseContext())
            for (i in 0..6) {
                textView = TextView(getActivity()!!.getBaseContext())
                textView!!.setGravity(Gravity.CENTER_HORIZONTAL)
                textView!!.setTextAppearance(getActivity(), R.style.estilo_celda)
                textView!!.setBackgroundResource(R.drawable.tabla_celda_cabecera)
                textView!!.setText(cadena[i])
                trFilas!!.addView(textView)
            }
            bg!!.tlRegMantenimiento.addView(trFilas)
        } catch (e: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                e.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
        /*<!-- TODO: DESACTIVAR LOS CAMPOS CON SWITCHS -->*/
        bg!!.etCajaFormol.setEnabled(false)
        bg!!.etComedor.setEnabled(false)
        bg!!.etDucha.setEnabled(false)
        bg!!.etEstructuraGarita.setEnabled(false)
        bg!!.etMedidorAgua.setEnabled(false)
        bg!!.etPuertaPersonal.setEnabled(false)
        bg!!.etPuertaPozoSeptico.setEnabled(false)
        bg!!.etPuertaVehicular.setEnabled(false)
        bg!!.etSSHH.setEnabled(false)
        bg!!.etTableroControl.setEnabled(false)
        bg!!.etTachoRopa.setEnabled(false)
        bg!!.etTherma.setEnabled(false)
        bg!!.etTuberiaAgua.setEnabled(false)
        bg!!.etOtros.setEnabled(false)
        bg!!.etDescOtros.setEnabled(false)


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

        bg!!.spnTurno.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                sFecha = bg!!.tvFecha.getText().toString()
                sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
                iPosiTur = position
                mostrarTotales(sFecha, sCencos)
                listaItems(sFecha, sCencos)
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
                                dbEstructura.t_RMantenimiento,
                                dbEstructura.c_rmFecha,
                                slFecha,
                                dbEstructura.c_rmCencos,
                                slCencos,
                                dbEstructura.c_rmId,
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
        bg!!.spnTurno.setText("Seleccione")
        for (i in 1..14) {
            when (i) {
                1 -> {
                    sElemento = "Caja de Formol"
                    sEstado = bg!!.swtCajaFormol.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etCajaFormol.setText("")
                        bg!!.etCajaFormol.setEnabled(false)
                        bg!!.swtCajaFormol.setChecked(false)
                        bg!!.swtCajaFormol.setText("No")
                    }
                }

                2 -> {
                    sElemento = "Comedor"
                    sEstado = bg!!.swtComedor.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etComedor.setText("")
                        bg!!.etComedor.setEnabled(false)
                        bg!!.swtComedor.setChecked(false)
                        bg!!.swtComedor.setText("No")
                    }
                }

                3 -> {
                    sElemento = "Ducha"
                    sEstado = bg!!.swtDucha.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etDucha.setText("")
                        bg!!.etDucha.setEnabled(false)
                        bg!!.swtDucha.setChecked(false)
                        bg!!.swtDucha.setText("No")
                    }
                }

                4 -> {
                    sElemento = "Estructura de Garita"
                    sEstado = bg!!.swtEstructuraGarita.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etEstructuraGarita.setText("")
                        bg!!.etEstructuraGarita.setEnabled(false)
                        bg!!.swtEstructuraGarita.setChecked(false)
                        bg!!.swtEstructuraGarita.setText("No")
                    }
                }

                5 -> {
                    sElemento = "Medidor de Agua"
                    sEstado = bg!!.swtMedidorAgua.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etMedidorAgua.setText("")
                        bg!!.etMedidorAgua.setEnabled(false)
                        bg!!.swtMedidorAgua.setChecked(false)
                        bg!!.swtMedidorAgua.setText("No")
                    }
                }

                6 -> {
                    sElemento = "Puerta de Personal"
                    sEstado = bg!!.swtPuertaPersonal.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etPuertaPersonal.setText("")
                        bg!!.etPuertaPersonal.setEnabled(false)
                        bg!!.swtPuertaPersonal.setChecked(false)
                        bg!!.swtPuertaPersonal.setText("No")
                    }
                }

                7 -> {
                    sElemento = "Puerta Pozo Septico"
                    sEstado = bg!!.swtPuertaPozoSeptico.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etPuertaPozoSeptico.setText("")
                        bg!!.etPuertaPozoSeptico.setEnabled(false)
                        bg!!.swtPuertaPozoSeptico.setChecked(false)
                        bg!!.swtPuertaPozoSeptico.setText("No")
                    }
                }

                8 -> {
                    sElemento = "Puerta Vehicular"
                    sEstado = bg!!.swtPuertaVehicular.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etPuertaVehicular.setText("")
                        bg!!.swtPuertaVehicular.setEnabled(false)
                        bg!!.swtPuertaVehicular.setChecked(false)
                        bg!!.swtPuertaVehicular.setText("No")
                    }
                }

                9 -> {
                    sElemento = "SS.HH"
                    sEstado = bg!!.swtSSHH.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etSSHH.setText("")
                        bg!!.etSSHH.setEnabled(false)
                        bg!!.swtSSHH.setChecked(false)
                        bg!!.swtSSHH.setText("No")
                    }
                }

                10 -> {
                    sElemento = "Tablero de Control"
                    sEstado = bg!!.swtTableroControl.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etTableroControl.setText("")
                        bg!!.etTableroControl.setEnabled(false)
                        bg!!.swtTableroControl.setChecked(false)
                        bg!!.swtTableroControl.setText("No")
                    }
                }

                11 -> {
                    sElemento = "Tachos de Ropa"
                    sEstado = bg!!.swtTachoRopa.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etTachoRopa.setText("")
                        bg!!.etTachoRopa.setEnabled(false)
                        bg!!.swtTachoRopa.setChecked(false)
                        bg!!.swtTachoRopa.setText("No")
                    }
                }

                12 -> {
                    sElemento = "Therma"
                    sEstado = bg!!.swtTherma.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etTherma.setText("")
                        bg!!.etTherma.setEnabled(false)
                        bg!!.swtTherma.setChecked(false)
                        bg!!.swtTherma.setText("No")
                    }
                }

                13 -> {
                    sElemento = "Tuberia de Agua"
                    sEstado = bg!!.swtTuberiaAgua.getText().toString()
                    if (sEstado == "Si") {
                        bg!!.etTuberiaAgua.setText("")
                        bg!!.etTuberiaAgua.setEnabled(false)
                        bg!!.swtTuberiaAgua.setChecked(false)
                        bg!!.swtTuberiaAgua.setText("No")
                    }
                }

                14 -> {
                    sElemento = "Otros"
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
                        bg!!.etCencos.setText(cursor.getString(0) + " - " + cursor.getString(1))
                    }
                    cursor.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Código de la planta no existe",
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

    private fun guardarDatos() {
        db = conn!!.getWritableDatabase()

        //Todo:MAXIMO REGISTRO
        try {
            sql =
                "SELECT MAX(ABS(" + dbEstructura.c_rmId + "))  FROM " + dbEstructura.t_RMantenimiento + " WHERE " + dbEstructura.c_rmFecha + "='" + bg!!.tvFecha.getText()
                    .toString() + "';"
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

            //Todo:Agregamos campos generales a la Tabla
            sFecha = bg!!.tvFecha.getText().toString()
            sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
            sTurno = bg!!.spnTurno.getText().toString()
            sColaborador = hP!!.getNombreUsuario()
            for (i in 1..14) {
                when (i) {
                    1 -> {
                        sElemento = "Caja de Formol"
                        sEstado = bg!!.swtCajaFormol.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etCajaFormol.getText().toString()
                        }
                    }

                    2 -> {
                        sElemento = "Comedor"
                        sEstado = bg!!.swtComedor.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etComedor.getText().toString()
                        }
                    }

                    3 -> {
                        sElemento = "Ducha"
                        sEstado = bg!!.swtDucha.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etDucha.getText().toString()
                        }
                    }

                    4 -> {
                        sElemento = "Estructura de Garita"
                        sEstado = bg!!.swtEstructuraGarita.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etEstructuraGarita.getText().toString()
                        }
                    }

                    5 -> {
                        sElemento = "Medidor de Agua"
                        sEstado = bg!!.swtMedidorAgua.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etMedidorAgua.getText().toString()
                        }
                    }

                    6 -> {
                        sElemento = "Puerta de Personal"
                        sEstado = bg!!.swtPuertaPersonal.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etPuertaPersonal.getText().toString()
                        }
                    }

                    7 -> {
                        sElemento = "Puerta Pozo Septico"
                        sEstado = bg!!.swtPuertaPozoSeptico.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etPuertaPozoSeptico.getText().toString()
                        }
                    }

                    8 -> {
                        sElemento = "Puerta Vehicular"
                        sEstado = bg!!.swtPuertaVehicular.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etPuertaVehicular.getText().toString()
                        }
                    }

                    9 -> {
                        sElemento = "SS.HH"
                        sEstado = bg!!.swtSSHH.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etSSHH.getText().toString()
                        }
                    }

                    10 -> {
                        sElemento = "Tablero de Control"
                        sEstado = bg!!.swtTableroControl.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etTableroControl.getText().toString()
                        }
                    }

                    11 -> {
                        sElemento = "Tachos de Ropa"
                        sEstado = bg!!.swtTachoRopa.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etTachoRopa.getText().toString()
                        }
                    }

                    12 -> {
                        sElemento = "Therma"
                        sEstado = bg!!.swtTherma.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etTherma.getText().toString()
                        }
                    }

                    13 -> {
                        sElemento = "Tuberia de Agua"
                        sEstado = bg!!.swtTuberiaAgua.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etTuberiaAgua.getText().toString()
                        }
                    }

                    14 -> {
                        sOtros = bg!!.etOtros.getText().toString()
                        sElemento = sOtros
                        sEstado = bg!!.swtOtros.getText().toString()
                        sValidaReg = "Dato"
                        if (sEstado == "No") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sDescripcion = bg!!.etDescOtros.getText().toString()
                        }
                    }
                }
                val CodUser = hP!!.getCodigoUsuario()
                if (sValidaReg == "Dato") {
                    try {
                        iId = iId!! + 1
                        miTime = hMetodos.gethoraActual()
                        miDate = hMetodos.getfechaActual()

                        querys = "INSERT INTO " + dbEstructura.t_RMantenimiento +
                                "(" + dbEstructura.c_rmId + "," + dbEstructura.c_rmUsuario + "," + dbEstructura.c_rmDate +
                                "," + dbEstructura.c_rmTime + "," + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmCencos +
                                "," + dbEstructura.c_rmTurno + "," + dbEstructura.c_rmElemento + "," + dbEstructura.c_rmFallo +
                                "," + dbEstructura.c_rmDescripcion + "," + dbEstructura.c_rmNomColaborador + ")" +
                                "VALUES('" + iId.toString() + "','" + CodUser + "','" + miDate +
                                "','" + miTime + "','" + sFecha + "','" + sCencos +
                                "','" + sTurno + "','" + sElemento + "','" + sEstado +
                                "','" + sDescripcion + "','" + sColaborador + "')"
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
        val count = bg!!.tlRegMantenimiento.getChildCount()
        for (i in 1..<count) {
            val child = bg!!.tlRegMantenimiento.getChildAt(i)
            if (child is TableRow) (child as ViewGroup).removeAllViews()
        }
        db = conn!!.getReadableDatabase()
        sTurno = bg!!.spnTurno.getText().toString()
        try {
            sql =
                "SELECT " + dbEstructura.c_rmId + "," + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmTurno +
                        "," + dbEstructura.c_rmNomColaborador + "," + dbEstructura.c_rmElemento + "," + dbEstructura.c_rmFallo +
                        "," + dbEstructura.c_rmDescripcion +
                        " FROM " + dbEstructura.t_RMantenimiento +
                        " WHERE " + dbEstructura.c_rmFecha + "='" + xFecha + "' AND " + dbEstructura.c_rmCencos + "='" + xCencos + "'AND  " + dbEstructura.c_rgTurno + "='" + sTurno +
                        "'  ORDER BY " + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmCencos + "," + dbEstructura.c_rmTurno
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0))
                        sFecha = (cursor.getString(1))
                        sTurno = (cursor.getString(2))
                        sColaborador = (cursor.getString(3))
                        sElemento = (cursor.getString(4))
                        sEstado = (cursor.getString(5))
                        sDescripcion = (cursor.getString(6))

                        val cadena = arrayOf<String?>(
                            sId,
                            sFecha,
                            sTurno,
                            sColaborador,
                            sElemento,
                            sEstado,
                            sDescripcion
                        )
                        trFilas = TableRow(getActivity()!!.getBaseContext())
                        for (i in 0..6) {
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
                        bg!!.tlRegMantenimiento.addView(trFilas)
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

    private fun listaItems(xFecha: String?, xCencos: String?) {
        db = conn!!.getReadableDatabase()
        //PARA FILTRAR TABLA
        sTurno = bg!!.spnTurno.getText().toString()


        var regEliminar: eRegEliminar? = null
        regEliminarList = ArrayList<eRegEliminar?>()
        bg!!.spnEliminar.setAdapter(null)
        bg!!.spnEliminar.setText("Seleccione")
        bg!!.btnEliminar.setEnabled(false)
        iPosEli = -1
        try {
            sql =
                "SELECT " + dbEstructura.c_rmId + "," + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmTurno +
                        "," + dbEstructura.c_rmNomColaborador + "," + dbEstructura.c_rmElemento + "," + dbEstructura.c_rmFallo +
                        "," + dbEstructura.c_rmDescripcion +
                        " FROM " + dbEstructura.t_RMantenimiento +
                        " WHERE " + dbEstructura.c_rmFecha + "='" + xFecha + "' AND " + dbEstructura.c_rmCencos + "='" + xCencos + "'AND  " + dbEstructura.c_rgTurno + "='" + sTurno +
                        "'  ORDER BY " + dbEstructura.c_rmFecha + "," + dbEstructura.c_rmCencos + "," + dbEstructura.c_rmTurno
            val cursor = db!!.rawQuery(sql!!, null)

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    listaRegEliminar = ArrayList<String?>()
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0))
                        sFecha = (cursor.getString(1))
                        sTurno = (cursor.getString(2))
                        sColaborador = (cursor.getString(3))
                        sElemento = (cursor.getString(4))
                        sEstado = (cursor.getString(5))
                        sDescripcion = (cursor.getString(6))

                        regEliminar = eRegEliminar()
                        regEliminar.set_id(sId)
                        regEliminar.set_descripcion(sTurno + "|" + sTurno + "|" + sElemento + "|" + sEstado)
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

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.swtCajaFormol -> if (bg!!.swtCajaFormol.isChecked()) {
                bg!!.swtCajaFormol.setText("Si")
                bg!!.etCajaFormol.setEnabled(true)
            } else {
                bg!!.swtCajaFormol.setText("No")
                bg!!.etCajaFormol.setEnabled(false)
                bg!!.etCajaFormol.setText("")
            }

            R.id.swtComedor -> if (bg!!.swtComedor.isChecked()) {
                bg!!.swtComedor.setText("Si")
                bg!!.etComedor.setEnabled(true)
            } else {
                bg!!.swtComedor.setText("No")
                bg!!.etComedor.setEnabled(false)
                bg!!.etComedor.setText("")
            }

            R.id.swtDucha -> if (bg!!.swtDucha.isChecked()) {
                bg!!.swtDucha.setText("Si")
                bg!!.etDucha.setEnabled(true)
            } else {
                bg!!.swtDucha.setText("No")
                bg!!.etDucha.setEnabled(false)
                bg!!.etDucha.setText("")
            }

            R.id.swtEstructuraGarita -> if (bg!!.swtEstructuraGarita.isChecked()) {
                bg!!.swtEstructuraGarita.setText("Si")
                bg!!.etEstructuraGarita.setEnabled(true)
            } else {
                bg!!.swtEstructuraGarita.setText("No")
                bg!!.etEstructuraGarita.setEnabled(false)
                bg!!.etEstructuraGarita.setText("")
            }

            R.id.swtMedidorAgua -> if (bg!!.swtMedidorAgua.isChecked()) {
                bg!!.swtMedidorAgua.setText("Si")
                bg!!.etMedidorAgua.setEnabled(true)
            } else {
                bg!!.swtMedidorAgua.setText("No")
                bg!!.etMedidorAgua.setEnabled(false)
                bg!!.etMedidorAgua.setText("")
            }

            R.id.swtPuertaPersonal -> if (bg!!.swtPuertaPersonal.isChecked()) {
                bg!!.swtPuertaPersonal.setText("Si")
                bg!!.etPuertaPersonal.setEnabled(true)
            } else {
                bg!!.swtPuertaPersonal.setText("No")
                bg!!.etPuertaPersonal.setEnabled(false)
                bg!!.etPuertaPersonal.setText("")
            }

            R.id.swtPuertaPozoSeptico -> if (bg!!.swtPuertaPozoSeptico.isChecked()) {
                bg!!.swtPuertaPozoSeptico.setText("Si")
                bg!!.etPuertaPozoSeptico.setEnabled(true)
            } else {
                bg!!.swtPuertaPozoSeptico.setText("No")
                bg!!.etPuertaPozoSeptico.setEnabled(false)
                bg!!.etPuertaPozoSeptico.setText("")
            }

            R.id.swtPuertaVehicular -> if (bg!!.swtPuertaVehicular.isChecked()) {
                bg!!.swtPuertaVehicular.setText("Si")
                bg!!.etPuertaVehicular.setEnabled(true)
            } else {
                bg!!.swtPuertaVehicular.setText("No")
                bg!!.etPuertaVehicular.setEnabled(false)
                bg!!.etPuertaVehicular.setText("")
            }

            R.id.swtSSHH -> if (bg!!.swtSSHH.isChecked()) {
                bg!!.swtSSHH.setText("Si")
                bg!!.etSSHH.setEnabled(true)
            } else {
                bg!!.swtSSHH.setText("No")
                bg!!.etSSHH.setEnabled(false)
                bg!!.etSSHH.setText("")
            }

            R.id.swtTableroControl -> if (bg!!.swtTableroControl.isChecked()) {
                bg!!.swtTableroControl.setText("Si")
                bg!!.etTableroControl.setEnabled(true)
            } else {
                bg!!.swtTableroControl.setText("No")
                bg!!.etTableroControl.setEnabled(false)
                bg!!.etTableroControl.setText("")
            }

            R.id.swtTachoRopa -> if (bg!!.swtTachoRopa.isChecked()) {
                bg!!.swtTachoRopa.setText("Si")
                bg!!.etTachoRopa.setEnabled(true)
            } else {
                bg!!.swtTachoRopa.setText("No")
                bg!!.etTachoRopa.setEnabled(false)
                bg!!.etTachoRopa.setText("")
            }

            R.id.swtTherma -> if (bg!!.swtTherma.isChecked()) {
                bg!!.swtTherma.setText("Si")
                bg!!.etTherma.setEnabled(true)
            } else {
                bg!!.swtTherma.setText("No")
                bg!!.etTherma.setEnabled(false)
                bg!!.etTherma.setText("")
            }

            R.id.swtTuberiaAgua -> if (bg!!.swtTuberiaAgua.isChecked()) {
                bg!!.swtTuberiaAgua.setText("Si")
                bg!!.etTuberiaAgua.setEnabled(true)
            } else {
                bg!!.swtTuberiaAgua.setText("No")
                bg!!.etTuberiaAgua.setEnabled(false)
                bg!!.etTuberiaAgua.setText("")
            }

            R.id.swtOtros -> if (bg!!.swtOtros.isChecked()) {
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

            R.id.btnAgregar -> {
                sFecha = bg!!.tvFecha.getText().toString()
                sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
                if (iPosiTur == -1) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta seleccionar Turno!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtCajaFormol.getText()
                        .toString() == "Si" && (bg!!.etCajaFormol.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtComedor.getText().toString() == "Si" && (bg!!.etComedor.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtDucha.getText().toString() == "Si" && (bg!!.etDucha.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtEstructuraGarita.getText()
                        .toString() == "Si" && (bg!!.etEstructuraGarita.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtMedidorAgua.getText()
                        .toString() == "Si" && (bg!!.etMedidorAgua.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtPuertaPersonal.getText()
                        .toString() == "Si" && (bg!!.etPuertaPersonal.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtPuertaPozoSeptico.getText()
                        .toString() == "Si" && (bg!!.etPuertaPozoSeptico.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtPuertaVehicular.getText()
                        .toString() == "Si" && (bg!!.etPuertaVehicular.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtSSHH.getText().toString() == "Si" && (bg!!.etSSHH.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtTableroControl.getText()
                        .toString() == "Si" && (bg!!.etTableroControl.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtTachoRopa.getText()
                        .toString() == "Si" && (bg!!.etTachoRopa.getText().toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtTherma.getText().toString() == "Si" && (bg!!.etTherma.getText()
                        .toString() == "")
                ) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta llenar descripción!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (bg!!.swtTuberiaAgua.getText()
                        .toString() == "Si" && (bg!!.etTuberiaAgua.getText().toString() == "")
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
                    guardarDatos()
                    mostrarTotales(sFecha, sCencos)
                    listaItems(sFecha, sCencos)
                    inicializarCampos()
                }
            }
        }
    }
}