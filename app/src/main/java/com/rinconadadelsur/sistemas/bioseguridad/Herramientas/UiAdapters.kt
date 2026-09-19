package com.rinconadadelsur.sistemas.bioseguridad.Herramientas

import android.content.Context
import android.widget.ArrayAdapter
import com.rinconadadelsur.sistemas.bioseguridad.R

object UiAdapters {
    @JvmStatic
    fun spinner(context: Context, items: List<String?>?): ArrayAdapter<String> {
        val safeItems = (items ?: emptyList()).map { it.orEmpty() }
        return ArrayAdapter(context, R.layout.items_list, safeItems)
    }
}
