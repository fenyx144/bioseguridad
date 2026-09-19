package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transferencias

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hTransferenciaJson
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentBaseDatosExportacionBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar
import kotlin.math.max

class BaseDatosExportacionFragment : Fragment() {
    private var bg: FragmentBaseDatosExportacionBinding? = null

    var tempTabla: String? = null
    var sSaltoLinea: String? = null
    var sCadena: String? = null
    var sCadenaUrl: String? = null

    var sCodTipoPollo: String? = null
    var sNomTipoPollo: String? = null //Tipo de Pollo
    var sCodDestPollo: String? = null
    var sNomDestPollo: String? = null //Destino de Pollo
    var sFecha: String? = null
    var sFechaSaca: String? = null
    var sPlaca: String? = null //Placas de Camion

    var sCodUser: String? = null
    var sNomUser: String? = null
    var sDniUser: String? = null
    var sPerUser: String? = null
    var sIdAndroid: String? = null
    var sMensaje: String? = null

    //Recepcion de Aves
    var iDia: Int? = null
    var iMes: Int? = null
    var iAnio: Int? = null
    var sDia: String? = null
    var sMes: String? = null


    var miFecha: String? = null
    var miTime: String? = null
    var sMiApp: String? = null
    var sMiVersion: String? = null

    var iCount: Int = 0
    var iCountTipoPollo: Int = 0
    var iCountDestinoPollo: Int = 0
    var iCountPlacasCamion: Int = 0
    var iCountUsuarios: Int = 0

    var hP: hProcedimiento? = null
    var hV: hVariables? = null
var hTJ: hTransferenciaJson? = null

    var sNombreUsuario: String? = null
    var sCodigoUsuario: String? = null
    var sFechaActual: String? = null

    var conn: ConexionSQLiteHelper? = null
    var db: SQLiteDatabase? = null
    var sql: String? = null
    var insertar: String? = null
    var requestQueue: RequestQueue? = null
    var progressDialog: ProgressDialog? = null

    var iCouE: Int? = null
    var iCouI: Int? = null
    var iCouF: Int? = null
    var iNumError: Int = 0
    var jsonObjectDetalle: JsonObject? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_datos_exportacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bg = FragmentBaseDatosExportacionBinding.bind(view)
        conn = ConexionSQLiteHelper(getContext(), dbEstructura.miBaseDatos, null, 1)
        hP = hProcedimiento(getContext(), dbEstructura.miBaseDatos, null, 1)
        hTJ = hTransferenciaJson(getContext(), dbEstructura.miBaseDatos, null, 1)
        hM = hMetodos()

        sFechaActual = hMetodos.getfechaActual()

        sNombreUsuario = hP!!.getNombreUsuario()
        sCodigoUsuario = hP!!.getCodigoUsuario()

        bg!!.tvFecha.setText(sFechaActual)
        bg!!.tvNombreUsuario.setText(sNombreUsuario)


        jsonObjectDetalle = JsonObject()

        requestQueue = Volley.newRequestQueue(getContext())
        progressDialog = ProgressDialog(getContext())

        sSaltoLinea = "\n"

        bg!!.tvExportar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                sFecha = bg!!.tvFecha.getText().toString()
                if (sFecha!!.trim { it <= ' ' }.length <= 0) {
                    bg!!.tvRespuestaServidor.setText("Ingrese una fecha valida")
                } else {
                    progressDialog!!.setCanceledOnTouchOutside(false)
                    progressDialog!!.setTitle("Conectando al servidor")
                    progressDialog!!.setMessage("Guardado información...")
                    progressDialog!!.show()

                    sCadena = ""
                    bg!!.tvRespuestaServidor.setText("")
                    sIdAndroid = hP!!.getIdAndroid()

                    sMiApp = getString(R.string.app_name)
                    sMiVersion = getString(R.string.sVersion)


                    hVariables.sMiIpDispositivo = hMetodos.getIP()
                    hVariables.sMiIpConexion = hMetodos.ipConexion(hVariables.sMiIpDispositivo)
                    hVariables.sMiURL =
                        hVariables.sMiSeg + hVariables.sMiIpConexion + "/" + hVariables.sMiCarpeta + "/" + hVariables.phpExportar


                    //exportarDatos(hV.sMiURL, sFecha, sIdAndroid);
                    val asyncTaskRecepcionAvesVivas: AsyncTaskRecepcionAvesVivas?
                    asyncTaskRecepcionAvesVivas =
                        AsyncTaskRecepcionAvesVivas(sFecha!!, hVariables.sMiURL)
                    asyncTaskRecepcionAvesVivas.execute("")
                }
            }
        })

        bg!!.tvFecha.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val c = Calendar.getInstance()
                iDia = c.get(Calendar.DAY_OF_MONTH)
                iMes = c.get(Calendar.MONTH)
                iAnio = c.get(Calendar.YEAR)

                val datePickerDialog = DatePickerDialog(getContext()!!, object : OnDateSetListener {
                    override fun onDateSet(datePicker: DatePicker?, i: Int, i1: Int, i2: Int) {
                        sDia = "00" + i2
                        sDia = sDia!!.substring(max(0, sDia!!.length - 2))
                        sMes = "00" + (i1 + 1)
                        sMes = sMes!!.substring(max(0, sMes!!.length - 2))
                        bg!!.tvFecha.setText(i.toString() + "-" + sMes + "-" + sDia)
                    }
                }, iAnio!!, iMes!!, iDia!!)
                datePickerDialog.show()
            }
        })
    }

    inner class AsyncTaskRecepcionAvesVivas(var atFecha: String, var atUrl: String) :
        AsyncTask<String?, String?, String?>() {
        var atPD_mensaje: String = ""
        var atPD_data: String = ""
        override fun onPreExecute() {
            progressDialog!!.show()
        }

        override fun doInBackground(vararg params: String?): String {
            //Variables de Conexion
            iNumError = 0
            if (atFecha.trim { it <= ' ' } == "") {
                atPD_mensaje = "Ingrese una fecha valida"
            } else {
                var jsonArrayData = JsonArray()
                //Todo: Codigo para guardar el Historial
                jsonObjectDetalle!!.addProperty("tabla", "t_bitacora_exportacion")
                jsonObjectDetalle!!.addProperty("tuser", sCodigoUsuario)
                jsonObjectDetalle!!.addProperty("tapp", sMiApp)
                jsonObjectDetalle!!.addProperty("tversion", sMiVersion)
                jsonObjectDetalle!!.addProperty("tidandroid", sIdAndroid)
                jsonObjectDetalle!!.addProperty("tfecpro", atFecha)
                jsonArrayData.add(jsonObjectDetalle)
                //Todo: Codigo para Transferir la Data
                val resp01 =
                    hTJ!!.crearDataGarita(atFecha, sCodigoUsuario, sIdAndroid, jsonArrayData)
                atPD_mensaje = atPD_mensaje + resp01!![0] + sSaltoLinea
                atPD_data = resp01[1]
                iNumError = iNumError + resp01[2].toInt()

                val resp02 =
                    hTJ!!.crearDataMantenimiento(atFecha, sCodigoUsuario, sIdAndroid, jsonArrayData)
                atPD_mensaje = atPD_mensaje + resp02!![0] + sSaltoLinea
                atPD_data = resp02[1]
                iNumError = iNumError + resp02[2].toInt()

                val resp03 = hTJ!!.crearDataCercoElectrico(
                    atFecha,
                    sCodigoUsuario,
                    sIdAndroid,
                    jsonArrayData
                )
                atPD_mensaje = atPD_mensaje + resp03!![0] + sSaltoLinea
                atPD_data = resp03[1]
                iNumError = iNumError + resp03[2].toInt()

                val resp04 =
                    hTJ!!.crearDataFomites(atFecha, sCodigoUsuario, sIdAndroid, jsonArrayData)
                atPD_mensaje = atPD_mensaje + resp04!![0] + sSaltoLinea
                atPD_data = resp04[1]
                iNumError = iNumError + resp04[2].toInt()


                jsonArrayData = JsonParser.parseString(atPD_data)
                    .getAsJsonArray() //JsonParser.parseString(atPD_data);// JSON.parse(resp01[1]);

                //Todo: Codigo de Respuesta del Servidor
                val respServer: Array<String>? = makeRequest(atUrl, jsonArrayData.toString())
                atPD_mensaje = atPD_mensaje + respServer!![0] + sSaltoLinea
                iNumError = iNumError + respServer[1].toInt()
            }
            return atPD_mensaje
        }

        override fun onPostExecute(r: String?) {
            progressDialog!!.dismiss()
            bg!!.tvRespuestaServidor.setText(r)
            if (iNumError > 0) {
                bg!!.tvRespuestaServidor.setTextColor(Color.parseColor("#ff0000"))
            } else {
                bg!!.tvRespuestaServidor.setTextColor(Color.parseColor("#008000"))
            }
        }
    }


    companion object {
        var hM: hMetodos? = null
        fun makeRequest(uri: String?, json: String?): Array<String> {
            val urlConnection: HttpURLConnection
            var url: String?
            val data = json
            var result: String? = null
            var sNumError: String? = "0"
            try {
                //Connect
                urlConnection = ((URL(uri).openConnection())) as HttpURLConnection
                urlConnection.setDoOutput(true)
                urlConnection.setRequestProperty("Content-Type", "application/json")
                urlConnection.setRequestProperty("Accept", "application/json")
                urlConnection.setRequestMethod("POST")
                urlConnection.connect()

                //Write
                val outputStream = urlConnection.getOutputStream()
                val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                writer.write(data)
                writer.close()
                outputStream.close()

                //Read
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.getInputStream(), "UTF-8"))

                var line: String? = null
                val sb = StringBuilder()

                while ((bufferedReader.readLine().also { line = it }) != null) {
                    if (line!!.contains("NUMERODEERRORES=")) { //16
                        sNumError =
                            hMetodos.rightCadena(line.toString(), line.toString().length - 16)
                    } else {
                        sb.append(line + "\n") //Respuesta del Servidor
                    }
                }

                bufferedReader.close()
                result = sb.toString()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                result = e.message
                sNumError = "1"
            } catch (e: IOException) {
                e.printStackTrace()
                result = e.message
                sNumError = "1"
            }
            return arrayOf(result ?: "", sNumError ?: "0")
        }
    }
}