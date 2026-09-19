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
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eProcesos
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eReferencias
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eRegEliminar
import com.rinconadadelsur.sistemas.bioseguridad.Entidades.eTipo
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hMetodos
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hVariables
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.FragmentFomitesBinding
import java.util.Calendar
import kotlin.math.max

class FomitesFragment : Fragment(), View.OnClickListener {
    private var bg: FragmentFomitesBinding? = null

    /*<!-- TODO: VARIABLES G -->*/
    var iPosiRef: Int = -1
    var iPosiTur: Int = -1
    var iPosiPro: Int = -1
    var iId: Int? = null
    var iPosEli: Int = -1
    var iHora: Int? = null
    var iMinutos: Int? = null

    /*<!-- TODO: VARIABLES CABECERA -->*/
    var sFechaActual: String? = null
    var sNombreUsuario: String? = null
    var sSerieDispositivo: String? = null
    var sDoc: String? = null
    var sSerie: String? = null
    var sNumero: String? = null

    var sHora: String? = null
    var sMinutos: String? = null
    var sql: String? = null
    var sObservacion: String? = null
    var sValidaReg: String? = null
    var sDepredador: String? = null
    var sTipo: String? = null
    var sCantidad: String? = null
    var sEstado: String? = null
    var sId: String? = null
    var sFecha: String? = null
    var sCencos: String? = null
    var sTurno: String? = null
    var miDate: String? = null
    var miTime: String? = null
    var sReferencia: String? = null
    var sProceso: String? = null
    var sColaborador: String? = null


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
    var listReferencia: ArrayList<String?>? = null
    var ReferenciaList: ArrayList<eReferencias?>? = null
    var listProceso: ArrayList<String?>? = null
    var ProcesoList: ArrayList<eProcesos?>? = null
    var listResponsable: ArrayList<String?>? = null
    var listZ: ArrayList<String?>? = null
    var ZList: ArrayList<eTipo?>? = null
    var listP: ArrayList<String?>? = null
    var PList: ArrayList<eTipo?>? = null
    var listG: ArrayList<String?>? = null
    var GList: ArrayList<eTipo?>? = null
    var listL: ArrayList<String?>? = null
    var LList: ArrayList<eTipo?>? = null
    var listGa: ArrayList<String?>? = null
    var GaList: ArrayList<eTipo?>? = null
    var listC: ArrayList<String?>? = null
    var CList: ArrayList<eTipo?>? = null
    var listPa: ArrayList<String?>? = null
    var PaList: ArrayList<eTipo?>? = null

    var listaRegEliminar: ArrayList<String?>? = null
    var regEliminarList: ArrayList<eRegEliminar?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fomites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bg = FragmentFomitesBinding.bind(view)

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
        buscarReferencia()
        buscarProceso()
        buscarZ()
        buscarP()
        buscarG()
        buscarL()
        buscarGa()
        buscarC()
        buscarPa()

        /*<!-- TODO: BOTONES -->*/
        bg!!.swtZorro.setOnClickListener(this)
        bg!!.swtPerro.setOnClickListener(this)
        bg!!.swtGallinazo.setOnClickListener(this)
        bg!!.swtLechuza.setOnClickListener(this)
        bg!!.swtGato.setOnClickListener(this)
        bg!!.swtConejo.setOnClickListener(this)
        bg!!.swtPaloma.setOnClickListener(this)
        bg!!.btnAgregar.setOnClickListener(this)

        /*<!-- TODO: GENERAR FORMATO PARA CAPTURA DE HORA -->*/
        bg!!.etZorroHora.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etZorroHora.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })
        bg!!.etPerroHora.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etPerroHora.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })
        bg!!.etGallinazoHora.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etGallinazoHora.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })
        bg!!.etLechuzaHora.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etLechuzaHora.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })
        bg!!.etGatoHora.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etGatoHora.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })
        bg!!.etConejoHora.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etConejoHora.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })
        bg!!.etPalomaHora.setOnClickListener(object : View.OnClickListener {
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
                        bg!!.etPalomaHora.setText(sHora + ":" + sMinutos)
                    }
                }, iHora!!, iMinutos!!, true)
                timePickerDialog.show()
            }
        })

        /*<!-- TODO: CARGA DE DATA SPINNER ARRAY -->*/
        val listt: MutableList<String?> = ArrayList<String?>()

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
                sFecha = bg!!.tvFecha.getText().toString()
                sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
                mostrarTotales(sFecha, sCencos)
                listaItems(sFecha, sCencos)
            }
        })
        bg!!.spnReferencia.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                iPosiRef = position
            }
        })
        bg!!.spnProceso.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
                iPosiPro = position
            }
        })

        /*<!-- TODO: HABILITAR CAMPOS CUANDO HAY SELECCION -->*/
        bg!!.spnZorro.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    bg!!.etZorro.setEnabled(false)
                    bg!!.etZorroHora.setEnabled(false)
                    bg!!.swtZorro.setEnabled(false)
                    bg!!.etZorro.setText("")
                    bg!!.etZorroHora.setText("")
                    if (bg!!.swtZorro.getText().toString() == "Si") {
                        bg!!.swtZorro.setChecked(false)
                        bg!!.swtZorro.setText("No")
                    }
                } else {
                    bg!!.etZorro.setText("")
                    bg!!.etZorroHora.setText("")
                    bg!!.etZorro.setEnabled(true)
                    bg!!.etZorroHora.setEnabled(true)
                    bg!!.swtZorro.setEnabled(true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
        bg!!.spnPerro.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    bg!!.etPerro.setEnabled(false)
                    bg!!.etPerroHora.setEnabled(false)
                    bg!!.swtPerro.setEnabled(false)
                    bg!!.etPerro.setText("")
                    bg!!.etPerroHora.setText("")
                    if (bg!!.swtPerro.getText().toString() == "Si") {
                        bg!!.swtPerro.setChecked(false)
                        bg!!.swtPerro.setText("No")
                    }
                } else {
                    bg!!.etPerro.setText("")
                    bg!!.etPerroHora.setText("")
                    bg!!.etPerro.setEnabled(true)
                    bg!!.etPerroHora.setEnabled(true)
                    bg!!.swtPerro.setEnabled(true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
        bg!!.spnGallinazo.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    bg!!.etGallinazo.setEnabled(false)
                    bg!!.etGallinazoHora.setEnabled(false)
                    bg!!.swtGallinazo.setEnabled(false)
                    bg!!.etGallinazo.setText("")
                    bg!!.etGallinazoHora.setText("")
                    if (bg!!.swtGallinazo.getText().toString() == "Si") {
                        bg!!.swtGallinazo.setChecked(false)
                        bg!!.swtGallinazo.setText("No")
                    }
                } else {
                    bg!!.etGallinazo.setText("")
                    bg!!.etGallinazoHora.setText("")
                    bg!!.etGallinazo.setEnabled(true)
                    bg!!.etGallinazoHora.setEnabled(true)
                    bg!!.swtGallinazo.setEnabled(true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
        bg!!.spnLechuza.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    bg!!.etLechuza.setEnabled(false)
                    bg!!.etLechuzaHora.setEnabled(false)
                    bg!!.swtLechuza.setEnabled(false)
                    bg!!.etLechuza.setText("")
                    bg!!.etLechuzaHora.setText("")
                    if (bg!!.swtLechuza.getText().toString() == "Si") {
                        bg!!.swtLechuza.setChecked(false)
                        bg!!.swtLechuza.setText("No")
                    }
                } else {
                    bg!!.etLechuza.setText("")
                    bg!!.etLechuzaHora.setText("")
                    bg!!.etLechuza.setEnabled(true)
                    bg!!.etLechuzaHora.setEnabled(true)
                    bg!!.swtLechuza.setEnabled(true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
        bg!!.spnGato.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    bg!!.etGato.setEnabled(false)
                    bg!!.etGatoHora.setEnabled(false)
                    bg!!.swtGato.setEnabled(false)
                    bg!!.etGato.setText("")
                    bg!!.etGatoHora.setText("")
                    if (bg!!.swtGato.getText().toString() == "Si") {
                        bg!!.swtGato.setChecked(false)
                        bg!!.swtGato.setText("No")
                    }
                } else {
                    bg!!.etGato.setText("")
                    bg!!.etGatoHora.setText("")
                    bg!!.etGato.setEnabled(true)
                    bg!!.etGatoHora.setEnabled(true)
                    bg!!.swtGato.setEnabled(true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
        bg!!.spnConejo.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    bg!!.etConejo.setEnabled(false)
                    bg!!.etConejoHora.setEnabled(false)
                    bg!!.swtConejo.setEnabled(false)
                    bg!!.etConejo.setText("")
                    bg!!.etConejoHora.setText("")
                    if (bg!!.swtConejo.getText().toString() == "Si") {
                        bg!!.swtConejo.setChecked(false)
                        bg!!.swtConejo.setText("No")
                    }
                } else {
                    bg!!.etConejo.setText("")
                    bg!!.etConejoHora.setText("")
                    bg!!.etConejo.setEnabled(true)
                    bg!!.etConejoHora.setEnabled(true)
                    bg!!.swtConejo.setEnabled(true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
        bg!!.spnPaloma.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    bg!!.etPaloma.setEnabled(false)
                    bg!!.etPalomaHora.setEnabled(false)
                    bg!!.swtPaloma.setEnabled(false)
                    bg!!.etPaloma.setText("")
                    bg!!.etPalomaHora.setText("")
                    if (bg!!.swtPaloma.getText().toString() == "Si") {
                        bg!!.swtPaloma.setChecked(false)
                        bg!!.swtPaloma.setText("No")
                    }
                } else {
                    bg!!.etPaloma.setText("")
                    bg!!.etPalomaHora.setText("")
                    bg!!.etPaloma.setEnabled(true)
                    bg!!.etPalomaHora.setEnabled(true)
                    bg!!.swtPaloma.setEnabled(true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })


        /*<!-- TODO: CREAR CABECERA TABLA CONSUMO ALIMENTO: -->*/
        try {
            val cadena = arrayOf<String?>(
                "ID",
                "Fecha",
                "Turno",
                "Referencia",
                "Proceso",
                "Colaborador",
                "Depredador",
                "Tipo",
                "Cantidad",
                "Hora",
                "Estado",
                "Observacion"
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
            bg!!.tlRegFomites.addView(trFilas)
        } catch (e: Exception) {
            val toast = Toast.makeText(
                getActivity()!!.getApplicationContext(),
                e.message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }

        /*<!-- TODO: DESACTIVAR LOS CAMPOS CON SWITCHS -->*/
        bg!!.etZorro.setEnabled(false)
        bg!!.etPerro.setEnabled(false)
        bg!!.etGallinazo.setEnabled(false)
        bg!!.etLechuza.setEnabled(false)
        bg!!.etGato.setEnabled(false)
        bg!!.etConejo.setEnabled(false)
        bg!!.etPaloma.setEnabled(false)
        bg!!.etZorroHora.setEnabled(false)
        bg!!.etPerroHora.setEnabled(false)
        bg!!.etGallinazoHora.setEnabled(false)
        bg!!.etLechuzaHora.setEnabled(false)
        bg!!.etGatoHora.setEnabled(false)
        bg!!.etConejoHora.setEnabled(false)
        bg!!.etPalomaHora.setEnabled(false)
        bg!!.swtZorro.setEnabled(false)
        bg!!.swtPerro.setEnabled(false)
        bg!!.swtGallinazo.setEnabled(false)
        bg!!.swtLechuza.setEnabled(false)
        bg!!.swtGato.setEnabled(false)
        bg!!.swtConejo.setEnabled(false)
        bg!!.swtPaloma.setEnabled(false)


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

        sFecha = bg!!.tvFecha.getText().toString()
        sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
        mostrarTotales(sFecha, sCencos)
        listaItems(sFecha, sCencos)

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
                                dbEstructura.t_RFomites,
                                dbEstructura.c_rfFecha,
                                slFecha,
                                dbEstructura.c_rfCencos,
                                slCencos,
                                dbEstructura.c_rfId,
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
    }

    private fun inicializarCampos() {
        val r: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listReferencia)
        val p: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listProceso)
        val rb: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listResponsable)
        val zo: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listZ)
        val pe: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listP)
        val ga: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listG)
        val le: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listL)
        val gat: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listGa)
        val co: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listC)
        val pal: ArrayAdapter<CharSequence?> =
            ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listPa)


        bg!!.spnTurno.setSelection(0)
        bg!!.spnTurno.setEnabled(true)

        bg!!.spnReferencia.setAdapter<ArrayAdapter<CharSequence?>?>(r)
        bg!!.spnReferencia.setEnabled(true)

        bg!!.spnProceso.setAdapter<ArrayAdapter<CharSequence?>?>(p)
        bg!!.spnProceso.setEnabled(true)


        for (i in 1..7) {
            when (i) {
                1 -> {
                    sDepredador = "Zorro"
                    sTipo = bg!!.spnZorro.getSelectedItem().toString()
                    sValidaReg = "Dato"
                    if (sTipo == "Selec") {
                        sValidaReg = "Sin Dato"
                    } else {
                        bg!!.spnZorro.setAdapter(zo)
                        bg!!.spnZorro.setEnabled(true)
                        bg!!.etZorro.setText("")
                        bg!!.etZorroHora.setText("")
                        if (bg!!.swtZorro.getText().toString() == "Si") {
                            bg!!.swtZorro.setChecked(false)
                            bg!!.swtZorro.setText("No")
                        }
                    }
                }

                2 -> {
                    sDepredador = "Perro"
                    sTipo = bg!!.spnPerro.getSelectedItem().toString()
                    sValidaReg = "Dato"
                    if (sTipo == "Selec") {
                        sValidaReg = "Sin Dato"
                    } else {
                        bg!!.spnPerro.setAdapter(pe)
                        bg!!.spnPerro.setEnabled(true)
                        bg!!.etPerro.setText("")
                        bg!!.etPerroHora.setText("")
                        if (bg!!.swtPerro.getText().toString() == "Si") {
                            bg!!.swtPerro.setChecked(false)
                            bg!!.swtPerro.setText("No")
                        }
                    }
                }

                3 -> {
                    sDepredador = "Gallinazo"
                    sTipo = bg!!.spnGallinazo.getSelectedItem().toString()
                    sValidaReg = "Dato"
                    if (sTipo == "Selec") {
                        sValidaReg = "Sin Dato"
                    } else {
                        bg!!.spnGallinazo.setAdapter(ga)
                        bg!!.spnGallinazo.setEnabled(true)
                        bg!!.etGallinazo.setText("")
                        bg!!.etGallinazoHora.setText("")
                        if (bg!!.swtGallinazo.getText().toString() == "Si") {
                            bg!!.swtGallinazo.setChecked(false)
                            bg!!.swtGallinazo.setText("No")
                        }
                    }
                }

                4 -> {
                    sDepredador = "Lechuza"
                    sTipo = bg!!.spnLechuza.getSelectedItem().toString()
                    sValidaReg = "Dato"
                    if (sTipo == "Selec") {
                        sValidaReg = "Sin Dato"
                    } else {
                        bg!!.spnLechuza.setAdapter(le)
                        bg!!.spnLechuza.setEnabled(true)
                        bg!!.etLechuza.setText("")
                        bg!!.etLechuzaHora.setText("")
                        if (bg!!.swtLechuza.getText().toString() == "Si") {
                            bg!!.swtLechuza.setChecked(false)
                            bg!!.swtLechuza.setText("No")
                        }
                    }
                }

                5 -> {
                    sDepredador = "Gato"
                    sTipo = bg!!.spnGato.getSelectedItem().toString()
                    sValidaReg = "Dato"
                    if (sTipo == "Selec") {
                        sValidaReg = "Sin Dato"
                    } else {
                        bg!!.spnGato.setAdapter(gat)
                        bg!!.spnGato.setEnabled(true)
                        bg!!.etGato.setText("")
                        bg!!.etGatoHora.setText("")
                        if (bg!!.swtGato.getText().toString() == "Si") {
                            bg!!.swtGato.setChecked(false)
                            bg!!.swtGato.setText("No")
                        }
                    }
                }

                6 -> {
                    sDepredador = "Conejo"
                    sTipo = bg!!.spnConejo.getSelectedItem().toString()
                    sValidaReg = "Dato"
                    if (sTipo == "Selec") {
                        sValidaReg = "Sin Dato"
                    } else {
                        bg!!.spnConejo.setAdapter(co)
                        bg!!.spnConejo.setEnabled(true)
                        bg!!.etConejo.setText("")
                        bg!!.etConejoHora.setText("")
                        if (bg!!.swtConejo.getText().toString() == "Si") {
                            bg!!.swtConejo.setChecked(false)
                            bg!!.swtConejo.setText("No")
                        }
                    }
                }

                7 -> {
                    sDepredador = "Paloma"
                    sTipo = bg!!.spnPaloma.getSelectedItem().toString()
                    sValidaReg = "Dato"
                    if (sTipo == "Selec") {
                        sValidaReg = "Sin Dato"
                    } else {
                        bg!!.spnPaloma.setAdapter(pal)
                        bg!!.spnPaloma.setEnabled(true)
                        bg!!.etPaloma.setText("")
                        bg!!.etPalomaHora.setText("")
                        if (bg!!.swtPaloma.getText().toString() == "Si") {
                            bg!!.swtPaloma.setChecked(false)
                            bg!!.swtPaloma.setText("No")
                        }
                    }
                }
            }
        }
        bg!!.etObservacion.setText("")
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

    private fun buscarReferencia() {
        db = conn!!.getReadableDatabase()
        var referencias: eReferencias? = null
        ReferenciaList = ArrayList<eReferencias?>()
        try {
            sql =
                "SELECT cod,des from(SELECT " + dbEstructura.c_rfCodigo + " as cod," + dbEstructura.c_rfDescripcion + " as des FROM " + dbEstructura.t_RefFomites +
                        " UNION" +
                        " SELECT " + dbEstructura.c_cgGalpon + " as cod," + dbEstructura.c_cgGalpon + " as des FROM " + dbEstructura.t_CencosGalpon + ") as tab ORDER BY des"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listReferencia = ArrayList<String?>()

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        referencias = eReferencias()
                        referencias.set_codigo(cursor.getString(0))
                        referencias.set_descripcion(cursor.getString(1))
                        ReferenciaList!!.add(referencias)
                    }

                    for (i in ReferenciaList!!.indices) {
                        listReferencia!!.add(ReferenciaList!!.get(i)!!.get_descripcion())
                    }
                    val r: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listReferencia)
                    bg!!.spnReferencia.setAdapter<ArrayAdapter<CharSequence?>?>(r)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Referencia no existe!!",
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

    private fun buscarProceso() {
        db = conn!!.getReadableDatabase()
        var procesos: eProcesos? = null
        ProcesoList = ArrayList<eProcesos?>()
        try {
            sql =
                "SELECT * FROM " + dbEstructura.t_ProcesoFomites + "  ORDER BY " + dbEstructura.c_pDescripcion + ";"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listProceso = ArrayList<String?>()

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        procesos = eProcesos()
                        procesos.set_codigo(cursor.getString(0))
                        procesos.set_descripcion(cursor.getString(1))
                        ProcesoList!!.add(procesos)
                    }

                    for (i in ProcesoList!!.indices) {
                        listProceso!!.add(ProcesoList!!.get(i)!!.get_descripcion())
                    }
                    val p: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listProceso)
                    bg!!.spnProceso.setAdapter<ArrayAdapter<CharSequence?>?>(p)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Proceso no existe!!",
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

    private fun buscarZ() {
        db = conn!!.getReadableDatabase()
        var z: eTipo? = null
        ZList = ArrayList<eTipo?>()
        try {
            sql =
                "SELECT * FROM " + dbEstructura.t_TipoFomites + "  ORDER BY " + dbEstructura.c_tDescripcion + ";"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listZ = ArrayList<String?>()
                listZ!!.add("Selec")
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        z = eTipo()
                        z.set_codigo(cursor.getString(0))
                        z.set_descripcion(cursor.getString(1))
                        ZList!!.add(z)
                    }

                    for (i in ZList!!.indices) {
                        listZ!!.add(ZList!!.get(i)!!.get_descripcion())
                    }
                    val zo: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listZ)
                    bg!!.spnZorro.setAdapter(zo)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Tipo no Existe",
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

    private fun buscarP() {
        db = conn!!.getReadableDatabase()
        var p: eTipo? = null
        PList = ArrayList<eTipo?>()
        try {
            sql =
                "SELECT * FROM " + dbEstructura.t_TipoFomites + "  ORDER BY " + dbEstructura.c_tDescripcion + ";"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listP = ArrayList<String?>()
                listP!!.add("Selec")
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        p = eTipo()
                        p.set_codigo(cursor.getString(0))
                        p.set_descripcion(cursor.getString(1))
                        PList!!.add(p)
                    }

                    for (i in PList!!.indices) {
                        listP!!.add(PList!!.get(i)!!.get_descripcion())
                    }
                    val pe: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listP)
                    bg!!.spnPerro.setAdapter(pe)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Tipo no Existe",
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

    private fun buscarG() {
        db = conn!!.getReadableDatabase()
        var g: eTipo? = null
        GList = ArrayList<eTipo?>()
        try {
            sql =
                "SELECT * FROM " + dbEstructura.t_TipoFomites + "  ORDER BY " + dbEstructura.c_tDescripcion + ";"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listG = ArrayList<String?>()
                listG!!.add("Selec")
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        g = eTipo()
                        g.set_codigo(cursor.getString(0))
                        g.set_descripcion(cursor.getString(1))
                        GList!!.add(g)
                    }

                    for (i in GList!!.indices) {
                        listG!!.add(GList!!.get(i)!!.get_descripcion())
                    }
                    val ga: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listG)
                    bg!!.spnGallinazo.setAdapter(ga)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Tipo no Existe",
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

    private fun buscarL() {
        db = conn!!.getReadableDatabase()
        var l: eTipo? = null
        LList = ArrayList<eTipo?>()
        try {
            sql =
                "SELECT * FROM " + dbEstructura.t_TipoFomites + "  ORDER BY " + dbEstructura.c_tDescripcion + ";"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listL = ArrayList<String?>()
                listL!!.add("Selec")
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        l = eTipo()
                        l.set_codigo(cursor.getString(0))
                        l.set_descripcion(cursor.getString(1))
                        LList!!.add(l)
                    }

                    for (i in LList!!.indices) {
                        listL!!.add(LList!!.get(i)!!.get_descripcion())
                    }
                    val le: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listL)
                    bg!!.spnLechuza.setAdapter(le)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Tipo no Existe",
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

    private fun buscarGa() {
        db = conn!!.getReadableDatabase()
        var ga: eTipo? = null
        GaList = ArrayList<eTipo?>()
        try {
            sql =
                "SELECT * FROM " + dbEstructura.t_TipoFomites + "  ORDER BY " + dbEstructura.c_tDescripcion + ";"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listGa = ArrayList<String?>()
                listGa!!.add("Selec")
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        ga = eTipo()
                        ga.set_codigo(cursor.getString(0))
                        ga.set_descripcion(cursor.getString(1))
                        GaList!!.add(ga)
                    }

                    for (i in GaList!!.indices) {
                        listGa!!.add(GaList!!.get(i)!!.get_descripcion())
                    }
                    val gat: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listGa)
                    bg!!.spnGato.setAdapter(gat)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Tipo no Existe",
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

    private fun buscarC() {
        db = conn!!.getReadableDatabase()
        var c: eTipo? = null
        CList = ArrayList<eTipo?>()
        try {
            sql =
                "SELECT * FROM " + dbEstructura.t_TipoFomites + "  ORDER BY " + dbEstructura.c_tDescripcion + ";"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listC = ArrayList<String?>()
                listC!!.add("Selec")
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        c = eTipo()
                        c.set_codigo(cursor.getString(0))
                        c.set_descripcion(cursor.getString(1))
                        CList!!.add(c)
                    }

                    for (i in CList!!.indices) {
                        listC!!.add(CList!!.get(i)!!.get_descripcion())
                    }
                    val co: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listC)
                    bg!!.spnConejo.setAdapter(co)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Tipo no Existe",
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

    private fun buscarPa() {
        db = conn!!.getReadableDatabase()
        var pa: eTipo? = null
        PaList = ArrayList<eTipo?>()
        try {
            sql =
                "SELECT * FROM " + dbEstructura.t_TipoFomites + "  ORDER BY " + dbEstructura.c_tDescripcion + ";"
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                listPa = ArrayList<String?>()
                listPa!!.add("Selec")
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        pa = eTipo()
                        pa.set_codigo(cursor.getString(0))
                        pa.set_descripcion(cursor.getString(1))
                        PaList!!.add(pa)
                    }

                    for (i in PaList!!.indices) {
                        listPa!!.add(PaList!!.get(i)!!.get_descripcion())
                    }
                    val pal: ArrayAdapter<CharSequence?> =
                        ArrayAdapter<Any?>(getContext()!!, R.layout.items_list, listPa)
                    bg!!.spnPaloma.setAdapter(pal)
                    cursor.close()
                    db!!.close()
                } else {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Tipo no Existe",
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
        sColaborador = hP!!.getNombreUsuario() //bg.tvNombreUsuario.getText().toString();
        db = conn!!.getWritableDatabase()

        //Todo:MAXIMO REGISTRO
        try {
            sql =
                "SELECT MAX(ABS(" + dbEstructura.c_rfId + "))  FROM " + dbEstructura.t_RFomites + " WHERE " + dbEstructura.c_rfFecha + "='" + bg!!.tvFecha.getText()
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

            //Todo:Validamos campo vacio de Observacion:
            sObservacion = bg!!.etObservacion.getText().toString()
            if (sObservacion == "") {
                sValidaReg = "Sin Dato"
            } else {
                sValidaReg = "Dato"
            }

            //Todo:Agregamos campos generales a la Tabla
            sFecha = bg!!.tvFecha.getText().toString()
            sCencos = bg!!.etCencos.getText().toString().substring(0, 6)
            sTurno = bg!!.spnTurno.getText().toString()
            sReferencia = ReferenciaList!!.get(iPosiRef)!!.get_descripcion()
            sProceso = ProcesoList!!.get(iPosiPro)!!.get_descripcion()

            for (i in 1..7) {
                when (i) {
                    1 -> {
                        sDepredador = "Zorro"
                        sTipo = bg!!.spnZorro.getSelectedItem().toString()
                        sValidaReg = "Dato"
                        if (sTipo == "Selec") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sCantidad = bg!!.etZorro.getText().toString()
                            sHora = bg!!.etZorroHora.getText().toString()
                            sEstado = bg!!.swtZorro.getText().toString()
                        }
                    }

                    2 -> {
                        sDepredador = "Perro"
                        sTipo = bg!!.spnPerro.getSelectedItem().toString()
                        sValidaReg = "Dato"
                        if (sTipo == "Selec") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sCantidad = bg!!.etPerro.getText().toString()
                            sHora = bg!!.etPerroHora.getText().toString()
                            sEstado = bg!!.swtPerro.getText().toString()
                        }
                    }

                    3 -> {
                        sDepredador = "Gallinazo"
                        sTipo = bg!!.spnGallinazo.getSelectedItem().toString()
                        sValidaReg = "Dato"
                        if (sTipo == "Selec") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sCantidad = bg!!.etGallinazo.getText().toString()
                            sHora = bg!!.etGallinazoHora.getText().toString()
                            sEstado = bg!!.swtGallinazo.getText().toString()
                        }
                    }

                    4 -> {
                        sDepredador = "Lechuza"
                        sTipo = bg!!.spnLechuza.getSelectedItem().toString()
                        sValidaReg = "Dato"
                        if (sTipo == "Selec") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sCantidad = bg!!.etLechuza.getText().toString()
                            sHora = bg!!.etLechuzaHora.getText().toString()
                            sEstado = bg!!.swtLechuza.getText().toString()
                        }
                    }

                    5 -> {
                        sDepredador = "Gato"
                        sTipo = bg!!.spnGato.getSelectedItem().toString()
                        sValidaReg = "Dato"
                        if (sTipo == "Selec") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sCantidad = bg!!.etGato.getText().toString()
                            sHora = bg!!.etGatoHora.getText().toString()
                            sEstado = bg!!.swtGato.getText().toString()
                        }
                    }

                    6 -> {
                        sDepredador = "Conejo"
                        sTipo = bg!!.spnConejo.getSelectedItem().toString()
                        sValidaReg = "Dato"
                        if (sTipo == "Selec") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sCantidad = bg!!.etConejo.getText().toString()
                            sHora = bg!!.etConejoHora.getText().toString()
                            sEstado = bg!!.swtConejo.getText().toString()
                        }
                    }

                    7 -> {
                        sDepredador = "Paloma"
                        sTipo = bg!!.spnPaloma.getSelectedItem().toString()
                        sValidaReg = "Dato"
                        if (sTipo == "Selec") {
                            sValidaReg = "Sin Dato"
                        } else {
                            sCantidad = bg!!.etPaloma.getText().toString()
                            sHora = bg!!.etPalomaHora.getText().toString()
                            sEstado = bg!!.swtPaloma.getText().toString()
                        }
                    }
                }
                if (sValidaReg == "Dato") {
                    val CodUser = hP!!.getCodigoUsuario()
                    try {
                        iId = iId!! + 1
                        miTime = hMetodos.gethoraActual()
                        miDate = hMetodos.getfechaActual()

                        querys = "INSERT INTO " + dbEstructura.t_RFomites +
                                "(" + dbEstructura.c_rfId + "," + dbEstructura.c_rfUsuario + "," + dbEstructura.c_rfDate +
                                "," + dbEstructura.c_rfTime + "," + dbEstructura.c_rfFecha + "," + dbEstructura.c_rfCencos +
                                "," + dbEstructura.c_rfTurno + "," + dbEstructura.c_rfReferencia + "," + dbEstructura.c_rfProceso +
                                "," + dbEstructura.c_rfNomColaborador + "," + dbEstructura.c_rfDepredador + "," + dbEstructura.c_rfTipo +
                                "," + dbEstructura.c_rfCantidad + "," + dbEstructura.c_rfHora + "," + dbEstructura.c_rfDentroCerco +
                                "," + dbEstructura.c_rfObservacion + ")" +
                                "VALUES('" + iId.toString() + "','" + CodUser + "','" + miDate +
                                "','" + miTime + "','" + sFecha + "','" + sCencos +
                                "','" + sTurno + "','" + sReferencia + "','" + sProceso +
                                "','" + sColaborador + "','" + sDepredador + "','" + sTipo +
                                "','" + sCantidad + "','" + sHora + "','" + sEstado +
                                "','" + sObservacion + "')"
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
        val count = bg!!.tlRegFomites.getChildCount()
        for (i in 1..<count) {
            val child = bg!!.tlRegFomites.getChildAt(i)
            if (child is TableRow) (child as ViewGroup).removeAllViews()
        }
        db = conn!!.getReadableDatabase()
        //PARA FILTRAR TABLA
        sTurno = bg!!.spnTurno.getText().toString()
        sFecha = bg!!.tvFecha.getText().toString()
        try {
            sql =
                "SELECT " + dbEstructura.c_rfId + "," + dbEstructura.c_rfFecha + "," + dbEstructura.c_rfTurno +
                        "," + dbEstructura.c_rfReferencia + "," + dbEstructura.c_rfProceso + "," + dbEstructura.c_rfNomColaborador +
                        "," + dbEstructura.c_rfDepredador + "," + dbEstructura.c_rfTipo + "," + dbEstructura.c_rfCantidad +
                        "," + dbEstructura.c_rfHora + "," + dbEstructura.c_rfDentroCerco + "," + dbEstructura.c_rfObservacion +
                        " FROM " + dbEstructura.t_RFomites +
                        " WHERE " + dbEstructura.c_rfFecha + "='" + xFecha + "' AND " + dbEstructura.c_rfCencos + "='" + xCencos + "'AND  " + dbEstructura.c_rfTurno + "='" + sTurno +
                        "'  ORDER BY " + dbEstructura.c_rfFecha + "," + dbEstructura.c_rfCencos + "," + dbEstructura.c_rfTurno
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0))
                        sFecha = (cursor.getString(1))
                        sTurno = (cursor.getString(2))
                        sReferencia = (cursor.getString(3))
                        sProceso = (cursor.getString(4))
                        sColaborador = (cursor.getString(5))
                        sDepredador = (cursor.getString(6))
                        sTipo = (cursor.getString(7))
                        sCantidad = (cursor.getString(8))
                        sHora = (cursor.getString(9))
                        sEstado = (cursor.getString(10))
                        sObservacion = (cursor.getString(11))

                        val cadena = arrayOf<String?>(
                            sId,
                            sFecha,
                            sTurno,
                            sReferencia,
                            sProceso,
                            sColaborador,
                            sDepredador,
                            sTipo,
                            sCantidad,
                            sHora,
                            sEstado,
                            sObservacion
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
                        bg!!.tlRegFomites.addView(trFilas)
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
        sFecha = bg!!.tvFecha.getText().toString()

        var regEliminar: eRegEliminar? = null
        regEliminarList = ArrayList<eRegEliminar?>()
        bg!!.spnEliminar.setAdapter(null)
        bg!!.spnEliminar.setText("Seleccione")
        bg!!.btnEliminar.setEnabled(false)
        iPosEli = -1
        try {
            sql =
                "SELECT " + dbEstructura.c_rfId + "," + dbEstructura.c_rfFecha + "," + dbEstructura.c_rfTurno +
                        "," + dbEstructura.c_rfReferencia + "," + dbEstructura.c_rfProceso + "," + dbEstructura.c_rfNomColaborador +
                        "," + dbEstructura.c_rfDepredador + "," + dbEstructura.c_rfTipo + "," + dbEstructura.c_rfCantidad +
                        "," + dbEstructura.c_rfHora + "," + dbEstructura.c_rfDentroCerco + "," + dbEstructura.c_rfObservacion +
                        " FROM " + dbEstructura.t_RFomites +
                        " WHERE " + dbEstructura.c_rfFecha + "='" + xFecha + "' AND " + dbEstructura.c_rfCencos + "='" + xCencos + "'AND  " + dbEstructura.c_rfTurno + "='" + sTurno +
                        "'  ORDER BY " + dbEstructura.c_rfFecha + "," + dbEstructura.c_rfCencos + "," + dbEstructura.c_rfTurno
            val cursor = db!!.rawQuery(sql!!, null)
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    listaRegEliminar = ArrayList<String?>()
                    while (cursor.moveToNext()) {
                        sId = (cursor.getString(0))
                        sFecha = (cursor.getString(1))
                        sTurno = (cursor.getString(2))
                        sReferencia = (cursor.getString(3))
                        sProceso = (cursor.getString(4))
                        sColaborador = (cursor.getString(5))
                        sDepredador = (cursor.getString(6))
                        sTipo = (cursor.getString(7))
                        sCantidad = (cursor.getString(8))
                        sHora = (cursor.getString(9))
                        sEstado = (cursor.getString(10))
                        sObservacion = (cursor.getString(11))

                        regEliminar = eRegEliminar()
                        regEliminar.set_id(sId)
                        regEliminar.set_descripcion(sTurno + "|" + sReferencia + "|" + sProceso)
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
            R.id.swtZorro -> if (bg!!.swtZorro.isChecked()) {
                bg!!.swtZorro.setText("Si")
            } else {
                bg!!.swtZorro.setText("No")
            }

            R.id.swtPerro -> if (bg!!.swtPerro.isChecked()) {
                bg!!.swtPerro.setText("Si")
            } else {
                bg!!.swtPerro.setText("No")
            }

            R.id.swtGallinazo -> if (bg!!.swtGallinazo.isChecked()) {
                bg!!.swtGallinazo.setText("Si")
            } else {
                bg!!.swtGallinazo.setText("No")
            }

            R.id.swtLechuza -> if (bg!!.swtLechuza.isChecked()) {
                bg!!.swtLechuza.setText("Si")
            } else {
                bg!!.swtLechuza.setText("No")
            }

            R.id.swtGato -> if (bg!!.swtGato.isChecked()) {
                bg!!.swtGato.setText("Si")
            } else {
                bg!!.swtGato.setText("No")
            }

            R.id.swtConejo -> if (bg!!.swtConejo.isChecked()) {
                bg!!.swtConejo.setText("Si")
            } else {
                bg!!.swtConejo.setText("No")
            }

            R.id.swtPaloma -> if (bg!!.swtPaloma.isChecked()) {
                bg!!.swtPaloma.setText("Si")
            } else {
                bg!!.swtPaloma.setText("No")
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
                } else if (iPosiRef == -1) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta seleccionar una Referencia!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (iPosiPro == -1) {
                    val toast = Toast.makeText(
                        getActivity()!!.getApplicationContext(),
                        "Falta seleccionar un Proceso!!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if ((bg!!.spnZorro.getSelectedItemPosition() > 0)) {
                    if (bg!!.etZorro.getText().toString() == "" || bg!!.etZorroHora.getText()
                            .toString() == ""
                    ) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta completar campos!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        guardarDatos()
                        mostrarTotales(sFecha, sCencos)
                        listaItems(sFecha, sCencos)
                        inicializarCampos()
                    }
                } else if ((bg!!.spnPerro.getSelectedItemPosition() > 0)) {
                    if (bg!!.etPerro.getText().toString() == "" || bg!!.etPerroHora.getText()
                            .toString() == ""
                    ) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta completar campos!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        guardarDatos()
                        mostrarTotales(sFecha, sCencos)
                        listaItems(sFecha, sCencos)
                        inicializarCampos()
                    }
                } else if ((bg!!.spnGallinazo.getSelectedItemPosition() > 0)) {
                    if (bg!!.etGallinazo.getText()
                            .toString() == "" || bg!!.etGallinazoHora.getText().toString() == ""
                    ) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta completar campos!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        guardarDatos()
                        mostrarTotales(sFecha, sCencos)
                        listaItems(sFecha, sCencos)
                        inicializarCampos()
                    }
                } else if ((bg!!.spnGallinazo.getSelectedItemPosition() > 0)) {
                    if (bg!!.etGallinazo.getText()
                            .toString() == "" || bg!!.etGallinazoHora.getText().toString() == ""
                    ) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta completar campos!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        guardarDatos()
                        mostrarTotales(sFecha, sCencos)
                        listaItems(sFecha, sCencos)
                        inicializarCampos()
                    }
                } else if ((bg!!.spnLechuza.getSelectedItemPosition() > 0)) {
                    if (bg!!.etLechuza.getText().toString() == "" || bg!!.etLechuzaHora.getText()
                            .toString() == ""
                    ) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta completar campos!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        guardarDatos()
                        mostrarTotales(sFecha, sCencos)
                        listaItems(sFecha, sCencos)
                        inicializarCampos()
                    }
                } else if ((bg!!.spnGato.getSelectedItemPosition() > 0)) {
                    if (bg!!.etGato.getText().toString() == "" || bg!!.etGatoHora.getText()
                            .toString() == ""
                    ) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta completar campos!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        guardarDatos()
                        mostrarTotales(sFecha, sCencos)
                        listaItems(sFecha, sCencos)
                        inicializarCampos()
                    }
                } else if ((bg!!.spnConejo.getSelectedItemPosition() > 0)) {
                    if (bg!!.etConejo.getText().toString() == "" || bg!!.etConejoHora.getText()
                            .toString() == ""
                    ) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta completar campos!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        guardarDatos()
                        mostrarTotales(sFecha, sCencos)
                        listaItems(sFecha, sCencos)
                        inicializarCampos()
                    }
                } else if ((bg!!.spnPaloma.getSelectedItemPosition() > 0)) {
                    if (bg!!.etPaloma.getText().toString() == "" || bg!!.etPalomaHora.getText()
                            .toString() == ""
                    ) {
                        val toast = Toast.makeText(
                            getActivity()!!.getApplicationContext(),
                            "Falta completar campos!!",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        guardarDatos()
                        inicializarCampos()
                        mostrarTotales(sFecha, sCencos)
                        listaItems(sFecha, sCencos)
                    }
                }
            }
        }
    }
}