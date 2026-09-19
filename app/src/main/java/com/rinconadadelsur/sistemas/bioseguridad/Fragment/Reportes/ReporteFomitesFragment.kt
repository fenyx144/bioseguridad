package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Reportes

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.DevDataSeeder.codigoCencos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.DevDataSeeder.ensureDemoData
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentReporteFomitesBinding

class ReporteFomitesFragment : Fragment() {
    private var bg: FragmentReporteFomitesBinding? = null

    /*<!-- TODO: VARIABLES G -->*/
    var mifectra: String? = null
    var sHora: String? = null
    var sql: String? = null
    var sObservacion: String? = null
    var sDepredador: String? = null
    var sTipo: String? = null
    var sCantidad: String? = null
    var sEstado: String? = null
    var sFecha: String? = null
    var sCencos: String? = null
    var sTurno: String? = null
    var sReferencia: String? = null
    var sProceso: String? = null
    var sColaborador: String? = null

    /*<!-- TODO: VARIABLES CABECERA -->*/
    var sFechaActual: String? = null
    var sNombreUsuario: String? = null
    var sSerieDispositivo: String? = null
    var sDoc: String? = null
    var sSerie: String? = null
    var sNumero: String? = null

    /*<!-- TODO: TABLA -->*/
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reporte_fomites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bg = FragmentReporteFomitesBinding.bind(view)

        /*<!-- TODO: DATA -->*/
        dbE = dbEstructura()
        ensureDemoData(requireContext())
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

        buscarCencos()

        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/
        try {
            val cadena = arrayOf<String?>(
                "FECHA",
                "GRANJA",
                "COLABORADOR",
                "TURNO",
                "REFERENCIA",
                "PROCESO",
                "DEPREDADOR",
                "TIPO",
                "CANTIDAD",
                "HORA",
                "¿DENTRO DEL CERCO?"
            )
            trFilas = TableRow(getActivity()!!.getBaseContext())
            for (i in 0..10) {
                textView = TextView(getActivity()!!.getBaseContext())
                textView!!.setGravity(Gravity.CENTER_HORIZONTAL)
                textView!!.setTextAppearance(getActivity(), R.style.estilo_celda)
                textView!!.setBackgroundResource(R.drawable.tabla_celda_cabecera)
                textView!!.setText(cadena[i])
                trFilas!!.addView(textView)
            }
            bg!!.tlRepFomites.addView(trFilas)
        } catch (e: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                e.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }

        sFecha = bg!!.tvFecha.getText().toString()
        sCencos = codigoCencos(bg!!.etCencos.getText().toString())
        mostrarTotales(sFecha, sCencos)
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

    private fun mostrarTotales(xFecha: String?, xCencos: String?) {
        val count = bg!!.tlRepFomites.getChildCount()
        for (i in 1..<count) {
            val child = bg!!.tlRepFomites.getChildAt(i)
            if (child is TableRow) (child as ViewGroup).removeAllViews()
        }
        db = conn!!.getReadableDatabase()
        try {
            sql =
                "SELECT " + dbEstructura.c_rfFecha + "," + dbEstructura.c_rfCencos + "," + dbEstructura.c_rfNomColaborador +
                        "," + dbEstructura.c_rfTurno + "," + dbEstructura.c_rfReferencia + "," + dbEstructura.c_rfProceso +
                        "," + dbEstructura.c_rfDepredador + "," + dbEstructura.c_rfTipo + "," + dbEstructura.c_rfCantidad +
                        "," + dbEstructura.c_rfHora + "," + dbEstructura.c_rfDentroCerco +
                        " FROM " + dbEstructura.t_RFomites +
                        " WHERE " + dbEstructura.c_rfFecha + "='" + xFecha + "' AND " + dbEstructura.c_rfCencos + "='" + xCencos +
                        "'  ORDER BY " + dbEstructura.c_rfFecha + "," + dbEstructura.c_rfTurno + "," + dbEstructura.c_rfNomColaborador
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sFecha = (cursor.getString(0))
                        sCencos = (cursor.getString(1))
                        sColaborador = (cursor.getString(2))
                        sTurno = (cursor.getString(3))
                        sReferencia = (cursor.getString(4))
                        sProceso = (cursor.getString(5))
                        sDepredador = (cursor.getString(6))
                        sTipo = (cursor.getString(7))
                        sCantidad = (cursor.getString(8))
                        sHora = (cursor.getString(9))
                        sEstado = (cursor.getString(10))

                        val cadena = arrayOf<String?>(
                            sFecha,
                            sCencos,
                            sColaborador,
                            sTurno,
                            sReferencia,
                            sProceso,
                            sDepredador,
                            sTipo,
                            sCantidad,
                            sHora,
                            sEstado
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
                        bg!!.tlRepFomites.addView(trFilas)
                    }
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "REPORTE GENERADO CON EXITO!!",
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
}