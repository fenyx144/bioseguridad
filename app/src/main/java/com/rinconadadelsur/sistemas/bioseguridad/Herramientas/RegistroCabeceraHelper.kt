package com.rinconadadelsur.sistemas.bioseguridad.Herramientas

import android.app.DatePickerDialog
import android.database.sqlite.SQLiteDatabase
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rinconadadelsur.sistemas.bioseguridad.Conexion.ConexionSQLiteHelper
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import java.util.Calendar

object RegistroCabeceraHelper {
    @JvmStatic
    fun bindFecha(fragment: Fragment, tvFecha: TextView) {
        tvFecha.setOnClickListener {
            val cal = Calendar.getInstance()
            val parts = tvFecha.text?.toString()?.split("/")
            if (parts?.size == 3) {
                cal.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
            }
            DatePickerDialog(
                fragment.requireContext(),
                { _, y, m, d ->
                    tvFecha.text = String.format("%02d/%02d/%04d", d, m + 1, y)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    @JvmStatic
    fun bindCencosDropdown(
        fragment: Fragment,
        conn: ConexionSQLiteHelper,
        etCencos: AutoCompleteTextView,
        onSelectionChanged: Runnable? = null
    ) {
        val labels = ArrayList<String>()
        val codes = ArrayList<String>()
        val names = ArrayList<String>()
        val db: SQLiteDatabase = conn.readableDatabase
        try {
            val q = "SELECT ${dbEstructura.c_cCodigo},${dbEstructura.c_cNombre} FROM ${dbEstructura.t_Cencos}"
            db.rawQuery(q, null)?.use { cursor ->
                while (cursor.moveToNext()) {
                    codes.add(cursor.getString(0))
                    names.add(cursor.getString(1))
                    labels.add("${cursor.getString(0)} - ${cursor.getString(1)}")
                }
            }
        } catch (_: Exception) {
        }

        val adapter = UiAdapters.spinner(fragment.requireContext(), labels)
        etCencos.setAdapter(adapter)
        if (codes.isNotEmpty()) {
            etCencos.setText("${codes[0]}|${names[0]}", false)
        }

        etCencos.setOnItemClickListener { _, _, position, _ ->
            if (position in codes.indices) {
                etCencos.setText("${codes[position]}|${names[position]}", false)
            }
            onSelectionChanged?.run()
        }
    }

    /** Primeros 6 caracteres del código de cencos, compatible con layouts antiguos. */
    @JvmStatic
    fun cencosCode(raw: CharSequence?): String {
        if (raw.isNullOrBlank()) return ""
        val token = raw.toString().trim().substringBefore("|").substringBefore(" - ")
        return if (token.length >= 6) token.substring(0, 6) else token
    }
}
