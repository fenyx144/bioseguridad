package com.rinconadadelsur.sistemas.bioseguridad.Fragment.Transferencias
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.UiAdapters

import android.app.ProgressDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eCencos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentBaseDatosImportacionBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class BaseDatosImportacionFragment : Fragment() {
    private var bg: FragmentBaseDatosImportacionBinding? = null

    /*<!-- TODO: VARIABLES G -->*/
    var iPosiCen: Int = -1
    var xCencos: String? = null
    var sCencos: String? = null

    /*<!-- TODO: PROCEDIMIENTOS -->*/
    var sNombreUsuario: String? = null
    var sFechaActual: String? = null
    var sIdAndroid: String? = null

    /*<!-- TODO: IMPORTACION -->*/
    var tempTabla: String? = null
    var sSaltoLinea: String? = null
    var sCadena: String? = null
    var sCadenaUrl: String? = null
    var iCount: Int = 0
    var iCountCencos: Int = 0
    var iCountAnomalias: Int = 0
    var iCountReferencias: Int = 0
    var iCountProcesos: Int = 0
    var iCountTipoFomites: Int = 0
    var iCountGalpones: Int = 0

    /*<!-- TODO: Cencos -->*/
    var sCodCcos: String? = null
    var sNomCcos: String? = null

    /*<!-- TODO: Anomalías -->*/
    var sCodAnomalia: String? = null
    var sAnomalia: String? = null

    /*<!-- TODO: Referencias -->*/
    var sCodRef: String? = null
    var sReferencia: String? = null

    /*<!-- TODO: Procesos -->*/
    var sCodProc: String? = null
    var sProceso: String? = null

    /*<!-- TODO: TipoFomites -->*/
    var sCodFom: String? = null
    var sTipoFomites: String? = null

    /*<!-- TODO: Galpones -->*/
    var sCodCcosGal: String? = null
    var sGalpon: String? = null

    /*<!-- TODO: CONEXION -->*/
var conn: ConexionSQLiteHelper? = null
    var db: SQLiteDatabase? = null
    var insertar: String? = null

    /*<!-- TODO: VOLLEY -->*/
    var requestQueue: RequestQueue? = null
    var progressDialog: ProgressDialog? = null

    /*<!-- TODO: HERRAMIENTAS -->*/
    var hP: hProcedimiento? = null
    var hM: hMetodos? = null
    var hV: hVariables? = null

    /*<!-- TODO: LISTAS-ENTIDADES -->*/
    var listaCencos: ArrayList<String?>? = null
    var cencosList: ArrayList<eCencos?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_datos_importacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bg = FragmentBaseDatosImportacionBinding.bind(view)

        /*<!-- TODO: DATA -->*/
        conn = ConexionSQLiteHelper(getContext(), dbEstructura.miBaseDatos, null, 1)
        hP = hProcedimiento(getContext(), dbEstructura.miBaseDatos, null, 1)
        hM = hMetodos()

        /*<!-- TODO: PROCEDEMIENTOS -->*/
        sFechaActual = hMetodos.getfechaActual()
        sNombreUsuario = hP!!.getNombreUsuario()

        /*<!-- TODO: ASIGNAR VARIABLES -->*/
        bg!!.tvFecha.setText(sFechaActual)
        bg!!.tvNombreUsuario.setText(sNombreUsuario)

        /*<!-- TODO: DESACTIVAR BOTONES AL INICIO -->*/
        bg!!.btnImportar.setEnabled(false)

        poblarCencos()
        sSaltoLinea = "\n"

        requestQueue = Volley.newRequestQueue(getContext())
        progressDialog = ProgressDialog(getContext())


        /*<!-- TODO: CAPTURA DE POSICION EXCLUYENDO LA PRIMERA  -->*/
        bg!!.spnCencos.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                if (position != 0) {
                    iPosiCen = position - 1
                    bg!!.btnImportar.setEnabled(true)
                    xCencos = cencosList!![iPosiCen]!!.c_cencos!!.substring(0, 6)
                    sCencos = xCencos
                } else {
                    iPosiCen = -1
                    bg!!.btnImportar.setEnabled(false)
                }
            }
        })

        /*<!-- TODO: CLICK AL BOTON AGREGAR -->*/
        bg!!.btnImportar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                progressDialog!!.setCanceledOnTouchOutside(false)
                progressDialog!!.setTitle("Conectando al servidor")
                progressDialog!!.setMessage("Importando información...")
                progressDialog!!.show()

                bg!!.tvRespuestaServidor.setText("")
                sCadena = ""
                sCadenaUrl = "?codigo=" + sCencos

                hVariables.sMiIpDispositivo = hMetodos.getIP()
                hVariables.sMiIpConexion = hMetodos.ipConexion(hVariables.sMiIpDispositivo)
                hVariables.sMiURL =
                    hVariables.sMiSeg + hVariables.sMiIpConexion + "/" + hVariables.sMiCarpeta + "/" + hVariables.phpImportar + sCadenaUrl
                importarDatos(hVariables.sMiURL)
            }
        })
    }

    private fun poblarCencos() {
        db = conn!!.getReadableDatabase()
        var cencos: eCencos? = null
        cencosList = ArrayList<eCencos?>()
        try {
            insertar =
                "SELECT " + dbEstructura.c_tcCodigo + "," + dbEstructura.c_tcNombre + " FROM " + dbEstructura.t_TCencos + " ORDER BY " + dbEstructura.c_tcCodigo
            val cursor = db!!.rawQuery(insertar!!, null)
            if (cursor != null) {
                listaCencos = ArrayList<String?>()
                listaCencos!!.add("Seleccione")
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        cencos = eCencos()
                        cencos.c_cencos = cursor.getString(0)
                        cencos.c_nombre = cursor.getString(1)
                        cencosList!!.add(cencos)
                    }

                    for (i in cencosList!!.indices) {
                        listaCencos!!.add(
                            cencosList!!.get(i)!!.c_cencos + " | " + cencosList!!.get(i)!!
                                .c_nombre
                        )
                    }

                    val adaptador = UiAdapters.spinner(requireContext(), listaCencos)
                    bg!!.spnCencos.setAdapter(adaptador)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Cencos no encontrados",
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

    private fun importarDatos(URL: String?) {
        val requestBody = ""

        val jsonArrayRequest = JsonArrayRequest(URL, object : Response.Listener<JSONArray> {
            override fun onResponse(response: JSONArray) {
                try {
                    var jsonObject: JSONObject? = null
                    var sTabla: String?
                    var sMensaje: String?
                    db = conn!!.getWritableDatabase()

                    sMensaje = ""
                    tempTabla = ""
                    iCount = 0

                    iCountCencos = 0
                    iCountAnomalias = 0
                    iCountReferencias = 0
                    iCountProcesos = 0
                    iCountTipoFomites = 0
                    iCountGalpones = 0

                    for (i in 0..<response.length()) {
                        iCount = iCount + 1
                        jsonObject = response.getJSONObject(i)
                        sTabla = (jsonObject.getString("miTabla"))

                        if (sTabla == tempTabla) {
                        } else {
                            tempTabla = sTabla
                            iCount = 1
                        }

                        if (sTabla == "Cencos") {
                            iCountCencos = iCountCencos + 1
                            if (iCount == 1) {
                                db!!.delete(dbEstructura.t_Cencos, null, null)
                            }

                            sCodCcos = (jsonObject.getString("codigo"))
                            sNomCcos = (jsonObject.getString("nombre"))

                            insertar = "INSERT INTO " + dbEstructura.t_Cencos +
                                    "(" + dbEstructura.c_cCodigo + "," + dbEstructura.c_cNombre + ")" +
                                    "VALUES('" + sCodCcos + "','" + sNomCcos + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "Anomalia") {
                            iCountAnomalias = iCountAnomalias + 1
                            if (iCount == 1) {
                                db!!.delete(dbEstructura.t_AnomaliaGarita, null, null)
                            }
                            sCodAnomalia = (jsonObject.getString("codigo"))
                            sAnomalia = (jsonObject.getString("descripcion"))

                            insertar = "INSERT INTO " + dbEstructura.t_AnomaliaGarita +
                                    "(" + dbEstructura.c_anCodigo + "," + dbEstructura.c_anDescripcion + ")" +
                                    "VALUES('" + sCodAnomalia + "','" + sAnomalia + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "Referencia") {
                            iCountReferencias = iCountReferencias + 1
                            if (iCount == 1) {
                                db!!.delete(dbEstructura.t_RefFomites, null, null)
                            }
                            sCodRef = (jsonObject.getString("codigo"))
                            sReferencia = (jsonObject.getString("descripcion"))

                            insertar = "INSERT INTO " + dbEstructura.t_RefFomites +
                                    "(" + dbEstructura.c_rfCodigo + "," + dbEstructura.c_rfDescripcion + ")" +
                                    "VALUES('" + sCodRef + "','" + sReferencia + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "Proceso") {
                            iCountProcesos = iCountProcesos + 1
                            if (iCount == 1) {
                                db!!.delete(dbEstructura.t_ProcesoFomites, null, null)
                            }
                            sCodProc = (jsonObject.getString("codigo"))
                            sProceso = (jsonObject.getString("descripcion"))

                            insertar = "INSERT INTO " + dbEstructura.t_ProcesoFomites +
                                    "(" + dbEstructura.c_pCodigo + "," + dbEstructura.c_pDescripcion + ")" +
                                    "VALUES('" + sCodProc + "','" + sProceso + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "TipFomites") {
                            iCountTipoFomites = iCountTipoFomites + 1
                            if (iCount == 1) {
                                db!!.delete(dbEstructura.t_TipoFomites, null, null)
                            }
                            sCodFom = (jsonObject.getString("codigo"))
                            sTipoFomites = (jsonObject.getString("descripcion"))

                            insertar = "INSERT INTO " + dbEstructura.t_TipoFomites +
                                    "(" + dbEstructura.c_tCodigo + "," + dbEstructura.c_tDescripcion + ")" +
                                    "VALUES('" + sCodFom + "','" + sTipoFomites + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "Galpon") {
                            iCountGalpones = iCountGalpones + 1
                            if (iCount == 1) {
                                db!!.delete(dbEstructura.t_CencosGalpon, null, null)
                            }
                            sCodCcosGal = (jsonObject.getString("tcencos"))
                            sGalpon = (jsonObject.getString("tcodint"))

                            insertar = "INSERT INTO " + dbEstructura.t_CencosGalpon +
                                    "(" + dbEstructura.c_cgCodigo + "," + dbEstructura.c_cgGalpon + ")" +
                                    "VALUES('" + sCodCcosGal + "','" + sGalpon + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "ERROR") {
                            sMensaje = (jsonObject.getString("Mensaje"))
                        }
                    }
                    sCadena =
                        sCadena + "Se han importado " + iCountCencos.toString() + " Cencos " + sSaltoLinea
                    sCadena =
                        sCadena + "Se han importado " + iCountAnomalias.toString() + " Anomalías " + sSaltoLinea
                    sCadena =
                        sCadena + "Se han importado " + iCountReferencias.toString() + " Referencias " + sSaltoLinea
                    sCadena =
                        sCadena + "Se han importado " + iCountProcesos.toString() + " Procesos " + sSaltoLinea
                    sCadena =
                        sCadena + "Se han importado " + iCountTipoFomites.toString() + " Tipo de Fomites " + sSaltoLinea
                    sCadena =
                        sCadena + "Se han importado " + iCountGalpones.toString() + " Galpones " + sSaltoLinea

                    sCadena = sCadena + sMensaje + sSaltoLinea
                    bg!!.tvRespuestaServidor.setText(sCadena)

                    progressDialog!!.dismiss()
                } catch (e: JSONException) {
                    sCadena = sCadena + e.message + sSaltoLinea
                    bg!!.tvRespuestaServidor.setText(sCadena)
                    progressDialog!!.dismiss()
                }
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                sCadena = sCadena + error.message + sSaltoLinea
                bg!!.tvRespuestaServidor.setText(sCadena)
                progressDialog!!.dismiss()
            }
        })
        requestQueue!!.add<JSONArray?>(jsonArrayRequest)
    }
}