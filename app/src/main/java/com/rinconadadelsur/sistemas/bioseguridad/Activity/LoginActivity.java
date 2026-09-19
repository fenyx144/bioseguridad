package com.rinconadadelsur.sistemas.bioseguridad.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper;
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.DevDataSeeder;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos;
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables;
import com.rinconadadelsur.sistemas.bioseguridad.R;
import com.rinconadadelsur.sistemas.bioseguridad.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    /** TODO: poner en false antes de producción — omite Volley/SQLite de login para revisar UI. */
    private static final boolean DEV_OMITIR_LOGIN_SERVIDOR = true;

    ActivityLoginBinding bg;

    String sIdAdroid,sUsuario,sPassword;
    Integer iCount;
    String sValida;
    String stidandroid,stserie,sfecha,sestado;
    String sCodClv,sNomClv;


    dbEstructura dbE;
    hMetodos hM;
    hVariables hV;

    ConexionSQLiteHelper conn;
    SQLiteDatabase db;
    String sql,insertar;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bg = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bg.getRoot());

        //Todo: DATA
        dbE=new dbEstructura();
        hM=new hMetodos();
        conn=new ConexionSQLiteHelper(this, dbE.miBaseDatos,null,1);

        requestQueue= Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        sIdAdroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        bg.tvIdandroid.setText(sIdAdroid);

        bg.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DEV_OMITIR_LOGIN_SERVIDOR) {
                    DevDataSeeder.ensureDemoData(LoginActivity.this);
                    sUsuario = bg.etUsuario.getText().toString().trim();
                    if (sUsuario.isEmpty()) {
                        sUsuario = "DEV";
                    }
                    irAMenuPrincipal(sUsuario);
                    return;
                }
                hV = new hVariables();
                hV.sMiIpDispositivo = hM.getIP();
                hV.sMiIpConexion = hM.ipConexion(hV.sMiIpDispositivo);
                hV.sMiURL = hV.sMiSeg + hV.sMiIpConexion + "/" + hV.sMiCarpeta + "/" + hV.phpIngreso;

                String miHora, miDia, miVersion;
                miHora = hM.gethoraActual();
                miDia = hM.getfechaActual();
                miVersion = getString(R.string.sVersion);

                sUsuario = bg.etUsuario.getText().toString();
                sPassword = bg.etPassword.getText().toString();

                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle("Conectando al servidor");
                progressDialog.setMessage("Validando información...");
                progressDialog.show();

                validarUsuario(hV.sMiURL + "?codigo=" + sUsuario + "&password=" + sPassword + "&idandroid=" + sIdAdroid + "&fecha=" + miDia + "&hora=" + miHora + "&version=" + miVersion);
            }
        });

        bg.tvRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrarDispositivoAndroidActivity.class);
                startActivity(intent);
            }
        });



    }

    private void irAMenuPrincipal(String clv) {
        Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
        intent.putExtra("clv", clv);
        startActivity(intent);
        finish();
    }

    private void validarUsuario(String URL){
        String requestBody = "";



        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest( URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    JSONObject jsonObject = null;
                    iCount=0;
                    String sTabla,sMensaje;
                    db=conn.getWritableDatabase();
                    db.delete(dbE.t_TCencos, null, null);
                    for (int i = 0; i < response.length(); i++) {
                        iCount=iCount+1;
                        jsonObject = response.getJSONObject(i);
                        sTabla=(jsonObject.getString("miTabla"));
                        if (sTabla.equals("DIS")){
                            stidandroid=(jsonObject.getString("tidandroid"));
                            stserie=(jsonObject.getString("tserie"));
                            sfecha=(jsonObject.getString("fecha"));
                            sestado=(jsonObject.getString("testado"));
                            db.delete(dbE.t_android, null, null);
                            insertar="INSERT INTO "+dbE.t_android+
                                    "("+dbE.c_aCodigo+","+dbE.c_aSerie+","+dbE.c_aFecha+","+dbE.c_aEstado+")"+
                                    "VALUES('"+stidandroid+"','"+stserie+"','"+sfecha+"','"+sestado+"')";
                            db.execSQL(insertar);
                        }else if  (sTabla.equals("ccos")){
                            sCodClv=(jsonObject.getString("codigo"));
                            sNomClv=(jsonObject.getString("nombre"));

                                insertar="INSERT INTO "+dbE.t_TCencos+
                                        "("+dbE.c_tcCodigo+","+dbE.c_tcNombre+")"+
                                        "VALUES('"+sCodClv+"','"+sNomClv+"')";
                                db.execSQL(insertar);
                        }else if (sTabla.equals("usuario")) {
                            db.delete(dbE.TABLA_CLV, null, null);

                            sCodClv= (jsonObject.getString("codigo"));
                            sNomClv= (jsonObject.getString("nombre"));

                            insertar = "INSERT INTO " + dbE.TABLA_CLV +
                                    "(" + dbE.Campo_uCodClv + "," + dbE.Campo_uNomClv + ")" +
                                    "VALUES('" + sCodClv + "','" + sNomClv + "')";
                            db.execSQL(insertar);

                        }else if (sTabla.equals("ERROR")){
                            sMensaje =(jsonObject.getString("Mensaje"));
                            Toast.makeText(LoginActivity.this,sMensaje, Toast.LENGTH_SHORT).show();
                            iCount=0;
                        }
                    }
                    if (iCount==0){
                        bg.btnLogin.setEnabled(true);
                        progressDialog.dismiss();
                    }else{
                        bg.etPassword.setText("");
                        String dato = bg.etUsuario.getText().toString();
                        // Instancias
                        Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                        //empaquetar la informacion para enviar a la segunda pantalla
                        intent.putExtra("clv", dato);
                        Toast toast2 = Toast.makeText(getApplicationContext(), "Cargando", Toast.LENGTH_SHORT);
                        toast2.show();
                        startActivity(intent);
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,e.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_SHORT).show();

                bg.btnLogin.setEnabled(true);
                checkLoginLocal();
            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    private void checkLoginLocal(){
        db=conn.getReadableDatabase();
        try{
            sValida="N";
            //idAndroid = (Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            //utd.sMiIdAdroid=idAndroid;
            sql="SELECT * FROM "+dbE.t_android+"  ;";
            Cursor cursor1=db.rawQuery(sql,null);
            if (cursor1 != null) {
                if (cursor1.getCount() > 0) {
                    //cursor.moveToFirst();
                    while (cursor1.moveToNext()) {
                        sValida="A";
                    }
                    cursor1.close();
                }else {
                    sValida="N";
                }
            }

            if (sValida.equals("A")) {
                sql = "SELECT " + dbE.c_aCodigo + "," + dbE.c_uNombre + " FROM " + dbE.t_usuarios + " WHERE " + dbEstructura.c_aCodigo + "='" + bg.etUsuario.getText().toString().toUpperCase() + "' AND " + dbEstructura.c_uPassword + "='" + bg.etPassword.getText().toString() + "';";
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        //cursor.moveToFirst();
                        db.delete(dbE.TABLA_CLV, null, null);
                        while (cursor.moveToNext()) {

                            sCodClv= cursor.getString(0);
                            sNomClv= cursor.getString(1);

                            insertar = "INSERT INTO " + dbE.TABLA_CLV +
                                    "(" + dbE.Campo_uCodClv + "," + dbE.Campo_uNomClv + ")" +
                                    "VALUES('" + sCodClv + "','" + sNomClv + "')";
                            db.execSQL(insertar);

                            bg.etPassword.setText("");
                            String dato = bg.etUsuario.getText().toString();
                            // Instancias
                            Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                            //empaquetar la informacion para enviar a la segunda pantalla
                            intent.putExtra("clv", dato);
                            /* Mensaje de carga */
                            Toast toast2 = Toast.makeText(getApplicationContext(), "Cargando", Toast.LENGTH_SHORT);
                            toast2.show();
                            startActivity(intent);
                        }
                        cursor.close();
                        db.close();
                        progressDialog.dismiss();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "No se pudo validar el usuario", Toast.LENGTH_SHORT);
                        toast.show();
                        bg.btnLogin.setEnabled(true);
                        progressDialog.dismiss();
                    }
                }
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "Este aplicativo pertenece solo a Granja Rinconada del Sur S.A.", Toast.LENGTH_SHORT);
                toast.show();
                bg.btnLogin.setEnabled(true);
                progressDialog.dismiss();
            }

        }catch (Exception ex){
            Toast toast = Toast.makeText(getApplicationContext(),ex.getMessage() ,Toast.LENGTH_SHORT);
            toast.show();
            bg.btnLogin.setEnabled(true);
            progressDialog.dismiss();
        }
    }

}