package com.rinconadadelsur.sistemas.bioseguridad.Activity

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.databinding.ActivityRegistrarDispositivoAndroidBinding

class RegistrarDispositivoAndroidActivity : AppCompatActivity() {
    var bg: ActivityRegistrarDispositivoAndroidBinding? = null

    var sMensaje: String? = ""
    var sMensajeError: String? = ""
    var tempTabla: String? = null
    var sSaltoLinea: String? = null
    var sCadena: String? = null
    var sCerrar: String? = null
    var sIdAndroid: String? = null
    var sFabricante: String? = null
    var sFamilia: String? = null
    var sModelo: String? = null
    var sDescripcion: String? = null

    var requestQueue: RequestQueue? = null
    var progressDialog: ProgressDialog? = null

    var iCouE: Int? = null
    var iCouI: Int? = null
    var iCouF: Int? = null

    var hV: hVariables? = null
    var hM: hMetodos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bg = ActivityRegistrarDispositivoAndroidBinding.inflate(getLayoutInflater())
        setContentView(bg!!.getRoot())

        sIdAndroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
        sFabricante = Build.MANUFACTURER //Xiaomi
        sFamilia = Build.BRAND //Redmi
        sModelo = Build.MODEL //mk

        sSaltoLinea = "\n"
        bg!!.tietIdAndroid.setText(sIdAndroid)
        bg!!.tietFabricante.setText(sFabricante)
        bg!!.tietFamilia.setText(sFamilia)
        bg!!.tietModelo.setText(sModelo)

        requestQueue = Volley.newRequestQueue(this)
        progressDialog = ProgressDialog(this)

        bg!!.btnRegistrar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                sDescripcion = bg!!.tietDescrpcion.getText().toString()

                if (sDescripcion!!.trim { it <= ' ' }.length <= 0) {
                    bg!!.tvRespuestaServidor.setText("Ingrese una descripcion valida")
                } else {
                    progressDialog!!.setCanceledOnTouchOutside(false)
                    progressDialog!!.setTitle("Conectando al servidor")
                    progressDialog!!.setMessage("Guardado información...")
                    progressDialog!!.show()

                    sCadena = ""
                    bg!!.tvRespuestaServidor.setText("")


                    hVariables.sMiIpDispositivo = hMetodos.getIP()
                    hVariables.sMiIpConexion = hMetodos.ipConexion(hVariables.sMiIpDispositivo)
                    hVariables.sMiURL =
                        hVariables.sMiSeg + hVariables.sMiIpConexion + "/" + hVariables.sMiCarpeta + "/" + hVariables.phpRegistrarDispositivo


                    registrarDispositivo(hVariables.sMiURL, sDescripcion)
                }
            }
        })
    }

    private fun registrarDispositivo(xUrl: String?, xDescripcion: String?) {
        sMensaje = ""
        iCouE = 0
        iCouI = 0
        iCouF = 0
        try {
            val parametros: MutableMap<String?, String?> = HashMap<String?, String?>()
            parametros.put("idandroid", sIdAndroid)
            parametros.put("fabricante", sFabricante)
            parametros.put("familia", sFamilia)
            parametros.put("modelo", sModelo)
            parametros.put("descripcion", xDescripcion)

            progressDialog!!.show()
            ProcessRequest(parametros, xUrl, sCerrar)
        } catch (ex: Exception) {
            sMensaje = ex.message
            sCadena = sCadena + sMensaje + sSaltoLinea
            bg!!.tvRespuestaServidor.setText(sCadena)
            progressDialog!!.dismiss()
        }
    }

    private fun ProcessRequest(
        parameters: MutableMap<String?, String?>?,
        URL: String?,
        sCerrar: String?
    ) {
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, URL, object : Response.Listener<String?> {
                override fun onResponse(response: String) {
                    if (response.contains("Existente")) {
                        iCouE = iCouE!! + 1
                    } else if (response == "Insertado") {
                        iCouI = iCouI!! + 1
                    } else {
                        iCouF = iCouF!! + 1
                        sMensajeError = response
                    }
                    if (iCouI!! > 0) {
                        sCadena =
                            sCadena + "Se han Insertado: " + iCouI.toString() + " Registros " + sSaltoLinea
                    }
                    if (iCouE!! > 0) {
                        if (response.contains("Estado Activo")) {
                            sCadena =
                                sCadena + "Dispositivo : " + iCouE.toString() + " Existente :Estado Activo " + sSaltoLinea
                        } else {
                            sCadena =
                                sCadena + "Dispositivo : " + iCouE.toString() + " Existente :Estado Inactivo " + sSaltoLinea
                        }
                    }
                    if (iCouF!! > 0) {
                        sCadena =
                            sCadena + "No se pudieron Completar: " + iCouF.toString() + " Registros " + sSaltoLinea
                        sCadena = sCadena + "Información: " + sMensajeError + sSaltoLinea
                    }
                    sCadena = sCadena + sMensaje + sSaltoLinea
                    bg!!.tvRespuestaServidor.setText(sCadena)

                    progressDialog!!.dismiss()
                    if (iCouI!! > 0) {
                        finish()
                    }
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    sMensaje = error.message.toString()
                    sCadena = sCadena + sMensaje + sSaltoLinea
                    bg!!.tvRespuestaServidor.setText(sCadena)
                    progressDialog!!.dismiss()
                }
            }) {
                override fun getParams(): MutableMap<String?, String?>? {
                    return parameters
                }
            }

        stringRequest.setRetryPolicy(
            DefaultRetryPolicy(
                hVariables.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        requestQueue!!.add<String?>(stringRequest)
    }
}