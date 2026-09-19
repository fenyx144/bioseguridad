package com.rinconadadelsur.sistemas.bioseguridad.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.DevDataSeeder.ensureDemoData
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.ActivityLoginBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

class LoginActivity : AppCompatActivity() {
    var bg: ActivityLoginBinding? = null

    var sIdAdroid: String? = null
    var sUsuario: String? = null
    var sPassword: String? = null
    var iCount: Int? = null
    var sValida: String? = null
    var stidandroid: String? = null
    var stserie: String? = null
    var sfecha: String? = null
    var sestado: String? = null
    var sCodClv: String? = null
    var sNomClv: String? = null


    var dbE: dbEstructura? = null
    var hM: hMetodos? = null
    var hV: hVariables? = null

    var conn: ConexionSQLiteHelper? = null
    var db: SQLiteDatabase? = null
    var sql: String? = null
    var insertar: String? = null

    var requestQueue: RequestQueue? = null
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bg = ActivityLoginBinding.inflate(getLayoutInflater())
        setContentView(bg!!.getRoot())

        //Todo: DATA
        dbE = dbEstructura()
        hM = hMetodos()
        conn = ConexionSQLiteHelper(this, dbEstructura.miBaseDatos, null, 1)

        requestQueue = Volley.newRequestQueue(this)
        progressDialog = ProgressDialog(this)
        sIdAdroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)

        bg!!.tvIdandroid.setText(sIdAdroid)

        bg!!.btnLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (DEV_OMITIR_LOGIN_SERVIDOR) {
                    ensureDemoData(this@LoginActivity)
                    sUsuario = bg!!.etUsuario.getText().toString().trim { it <= ' ' }
                    if (sUsuario!!.isEmpty()) {
                        sUsuario = "DEV"
                    }
                    irAMenuPrincipal(sUsuario)
                    return
                }
                hV = hVariables()
                hVariables.sMiIpDispositivo = hMetodos.getIP()
                hVariables.sMiIpConexion = hMetodos.ipConexion(hVariables.sMiIpDispositivo)
                hVariables.sMiURL =
                    hVariables.sMiSeg + hVariables.sMiIpConexion + "/" + hVariables.sMiCarpeta + "/" + hVariables.phpIngreso

                val miHora: String?
                val miDia: String?
                val miVersion: String?
                miHora = hMetodos.gethoraActual()
                miDia = hMetodos.getfechaActual()
                miVersion = getString(R.string.sVersion)

                sUsuario = bg!!.etUsuario.getText().toString()
                sPassword = bg!!.etPassword.getText().toString()

                progressDialog!!.setCanceledOnTouchOutside(false)
                progressDialog!!.setTitle("Conectando al servidor")
                progressDialog!!.setMessage("Validando información...")
                progressDialog!!.show()

                validarUsuario(hVariables.sMiURL + "?codigo=" + sUsuario + "&password=" + sPassword + "&idandroid=" + sIdAdroid + "&fecha=" + miDia + "&hora=" + miHora + "&version=" + miVersion)
            }
        })

        bg!!.tvRegistrate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val intent =
                    Intent(this@LoginActivity, RegistrarDispositivoAndroidActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun irAMenuPrincipal(clv: String?) {
        val intent = Intent(this@LoginActivity, MenuPrincipalActivity::class.java)
        intent.putExtra("clv", clv)
        startActivity(intent)
        finish()
    }

    private fun validarUsuario(URL: String?) {
        val requestBody = ""


        val jsonArrayRequest = JsonArrayRequest(URL, object : Response.Listener<JSONArray?> {
            override fun onResponse(response: JSONArray) {
                try {
                    var jsonObject: JSONObject? = null
                    iCount = 0
                    var sTabla: String?
                    var sMensaje: String?
                    db = conn!!.getWritableDatabase()
                    db!!.delete(dbEstructura.t_TCencos, null, null)
                    for (i in 0..<response.length()) {
                        iCount = iCount!! + 1
                        jsonObject = response.getJSONObject(i)
                        sTabla = (jsonObject.getString("miTabla"))
                        if (sTabla == "DIS") {
                            stidandroid = (jsonObject.getString("tidandroid"))
                            stserie = (jsonObject.getString("tserie"))
                            sfecha = (jsonObject.getString("fecha"))
                            sestado = (jsonObject.getString("testado"))
                            db!!.delete(dbEstructura.t_android, null, null)
                            insertar = "INSERT INTO " + dbEstructura.t_android +
                                    "(" + dbEstructura.c_aCodigo + "," + dbEstructura.c_aSerie + "," + dbEstructura.c_aFecha + "," + dbEstructura.c_aEstado + ")" +
                                    "VALUES('" + stidandroid + "','" + stserie + "','" + sfecha + "','" + sestado + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "ccos") {
                            sCodClv = (jsonObject.getString("codigo"))
                            sNomClv = (jsonObject.getString("nombre"))

                            insertar = "INSERT INTO " + dbEstructura.t_TCencos +
                                    "(" + dbEstructura.c_tcCodigo + "," + dbEstructura.c_tcNombre + ")" +
                                    "VALUES('" + sCodClv + "','" + sNomClv + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "usuario") {
                            db!!.delete(dbEstructura.TABLA_CLV, null, null)

                            sCodClv = (jsonObject.getString("codigo"))
                            sNomClv = (jsonObject.getString("nombre"))

                            insertar = "INSERT INTO " + dbEstructura.TABLA_CLV +
                                    "(" + dbEstructura.Campo_uCodClv + "," + dbEstructura.Campo_uNomClv + ")" +
                                    "VALUES('" + sCodClv + "','" + sNomClv + "')"
                            db!!.execSQL(insertar)
                        } else if (sTabla == "ERROR") {
                            sMensaje = (jsonObject.getString("Mensaje"))
                            Toast.makeText(this@LoginActivity, sMensaje, Toast.LENGTH_SHORT).show()
                            iCount = 0
                        }
                    }
                    if (iCount == 0) {
                        bg!!.btnLogin.setEnabled(true)
                        progressDialog!!.dismiss()
                    } else {
                        bg!!.etPassword.setText("")
                        val dato = bg!!.etUsuario.getText().toString()
                        // Instancias
                        val intent = Intent(this@LoginActivity, MenuPrincipalActivity::class.java)
                        //empaquetar la informacion para enviar a la segunda pantalla
                        intent.putExtra("clv", dato)
                        val toast2 =
                            Toast.makeText(getApplicationContext(), "Cargando", Toast.LENGTH_SHORT)
                        toast2.show()
                        startActivity(intent)
                        progressDialog!!.dismiss()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this@LoginActivity, e.toString(), Toast.LENGTH_SHORT).show()
                    progressDialog!!.dismiss()
                }
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Toast.makeText(this@LoginActivity, error.toString(), Toast.LENGTH_SHORT).show()

                bg!!.btnLogin.setEnabled(true)
                checkLoginLocal()
            }
        })

        requestQueue!!.add<JSONArray?>(jsonArrayRequest)
    }

    private fun checkLoginLocal() {
        db = conn!!.getReadableDatabase()
        try {
            sValida = "N"
            //idAndroid = (Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            //utd.sMiIdAdroid=idAndroid;
            sql = "SELECT * FROM " + dbEstructura.t_android + "  ;"
            val cursor1 = db!!.rawQuery(sql!!, null)
            if (cursor1 != null) {
                if (cursor1.getCount() > 0) {
                    //cursor.moveToFirst();
                    while (cursor1.moveToNext()) {
                        sValida = "A"
                    }
                    cursor1.close()
                } else {
                    sValida = "N"
                }
            }

            if (sValida == "A") {
                sql =
                    "SELECT " + dbEstructura.c_aCodigo + "," + dbEstructura.c_uNombre + " FROM " + dbEstructura.t_usuarios + " WHERE " + dbEstructura.c_aCodigo + "='" + bg!!.etUsuario.getText()
                        .toString().uppercase(
                            Locale.getDefault()
                        ) + "' AND " + dbEstructura.c_uPassword + "='" + bg!!.etPassword.getText()
                        .toString() + "';"
                val cursor = db!!.rawQuery(sql!!, null)
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        //cursor.moveToFirst();
                        db!!.delete(dbEstructura.TABLA_CLV, null, null)
                        while (cursor.moveToNext()) {
                            sCodClv = cursor.getString(0)
                            sNomClv = cursor.getString(1)

                            insertar = "INSERT INTO " + dbEstructura.TABLA_CLV +
                                    "(" + dbEstructura.Campo_uCodClv + "," + dbEstructura.Campo_uNomClv + ")" +
                                    "VALUES('" + sCodClv + "','" + sNomClv + "')"
                            db!!.execSQL(insertar)

                            bg!!.etPassword.setText("")
                            val dato = bg!!.etUsuario.getText().toString()
                            // Instancias
                            val intent =
                                Intent(this@LoginActivity, MenuPrincipalActivity::class.java)
                            //empaquetar la informacion para enviar a la segunda pantalla
                            intent.putExtra("clv", dato)
                            /* Mensaje de carga */
                            val toast2 = Toast.makeText(
                                getApplicationContext(),
                                "Cargando",
                                Toast.LENGTH_SHORT
                            )
                            toast2.show()
                            startActivity(intent)
                        }
                        cursor.close()
                        db!!.close()
                        progressDialog!!.dismiss()
                    } else {
                        val toast = Toast.makeText(
                            getApplicationContext(),
                            "No se pudo validar el usuario",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        bg!!.btnLogin.setEnabled(true)
                        progressDialog!!.dismiss()
                    }
                }
            } else {
                val toast = Toast.makeText(
                    getApplicationContext(),
                    "Este aplicativo pertenece solo a Granja Rinconada del Sur S.A.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                bg!!.btnLogin.setEnabled(true)
                progressDialog!!.dismiss()
            }
        } catch (ex: Exception) {
            val toast = Toast.makeText(getApplicationContext(), ex.message, Toast.LENGTH_SHORT)
            toast.show()
            bg!!.btnLogin.setEnabled(true)
            progressDialog!!.dismiss()
        }
    }

    companion object {
        /** TODO: poner en false antes de producción — omite Volley/SQLite de login para revisar UI.  */
        private const val DEV_OMITIR_LOGIN_SERVIDOR = true
    }
}