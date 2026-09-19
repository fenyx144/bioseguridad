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
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentReporteCercoBinding

class ReporteCercoFragment : Fragment() {
    private var bg: FragmentReporteCercoBinding? = null

    /*<!-- TODO: VARIABLES G -->*/
    var mifectra: String? = null
    var sql: String? = null
    var sFecha: String? = null
    var sColaborador: String? = null
    var sTurno: String? = null
    var sComponente: String? = null
    var sDescripcion: String? = null
    var sCencos: String? = null
    var sEstadoD: String? = null
    var sDescripcionDer: String? = null
    var sEstadoI: String? = null
    var sDescripcionIzq: String? = null
    var sHoraI: String? = null
    var sHoraF: String? = null


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
var conn: ConexionSQLiteHelper? = null
    var db: SQLiteDatabase? = null
    var querys: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reporte_cerco, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bg = FragmentReporteCercoBinding.bind(view)

        /*<!-- TODO: DATA -->*/
        
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
                "ESTADO(Derecho)",
                "¿POR QUÉ NO ESTÁ OPERATIVO?",
                "ESTADO(Izquierdo)",
                "¿POR QUÉ NO ESTÁ OPERATIVO?",
                "HORA INICIO",
                "HORA FIN",
                "COMPONENTE",
                "DESCRIPCION"
            )
            trFilas = TableRow(getActivity()!!.getBaseContext())
            for (i in 0..11) {
                textView = TextView(getActivity()!!.getBaseContext())
                textView!!.setGravity(Gravity.CENTER_HORIZONTAL)
                textView!!.setTextAppearance(getActivity(), R.style.estilo_celda)
                textView!!.setBackgroundResource(R.drawable.tabla_celda_cabecera)
                textView!!.setText(cadena[i])
                trFilas!!.addView(textView)
            }
            bg!!.tlRepCerco.addView(trFilas)
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
        val count = bg!!.tlRepCerco.getChildCount()
        for (i in 1..<count) {
            val child = bg!!.tlRepCerco.getChildAt(i)
            if (child is TableRow) (child as ViewGroup).removeAllViews()
        }
        db = conn!!.getReadableDatabase()
        try {
            sql =
                "SELECT " + dbEstructura.c_rcFecha + "," + dbEstructura.c_rcCencos + "," + dbEstructura.c_rcNomEvaluador +
                        "," + dbEstructura.c_rcTurno + "," + dbEstructura.c_rcEstadoDerecho + "," + dbEstructura.c_rcDescEstadoDer +
                        "," + dbEstructura.c_rcEstadoIzquierdo + "," + dbEstructura.c_rcDescEstadoIzq + "," + dbEstructura.c_rcHoraInicio +
                        "," + dbEstructura.c_rcHoraFinal + "," + dbEstructura.c_rcComponente + "," + dbEstructura.c_rcDescripcion +
                        " FROM " + dbEstructura.t_RCercoElectrico +
                        " WHERE " + dbEstructura.c_rcFecha + "='" + xFecha + "' AND " + dbEstructura.c_rcCencos + "='" + xCencos +
                        "'  ORDER BY " + dbEstructura.c_rcFecha + "," + dbEstructura.c_rcTurno + "," + dbEstructura.c_rcNomEvaluador

            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sFecha = (cursor.getString(0))
                        sCencos = (cursor.getString(1))
                        sColaborador = (cursor.getString(2))
                        sTurno = (cursor.getString(3))
                        sEstadoD = (cursor.getString(4))
                        sDescripcionDer = (cursor.getString(5))
                        sEstadoI = (cursor.getString(6))
                        sDescripcionIzq = (cursor.getString(7))
                        sHoraI = (cursor.getString(8))
                        sHoraF = (cursor.getString(9))
                        sComponente = (cursor.getString(10))
                        sDescripcion = (cursor.getString(11))

                        val cadena = arrayOf<String?>(
                            sFecha,
                            sCencos,
                            sColaborador,
                            sTurno,
                            sEstadoD,
                            sDescripcionDer,
                            sEstadoI,
                            sDescripcionIzq,
                            sHoraI,
                            sHoraF,
                            sComponente,
                            sDescripcion
                        )
                        trFilas = TableRow(getActivity()!!.getBaseContext())
                        for (i in 0..11) {
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
                        bg!!.tlRepCerco.addView(trFilas)
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