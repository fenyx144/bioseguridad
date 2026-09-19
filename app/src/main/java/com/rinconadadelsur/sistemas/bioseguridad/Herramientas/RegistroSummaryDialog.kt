package com.rinconadadelsur.sistemas.bioseguridad.Herramientas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.button.MaterialButton
import com.rinconadadelsur.sistemas.bioseguridad.R

class RegistroSummaryDialog : AppCompatDialogFragment() {
    interface Listener {
        fun onConfirmSave()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Bioseguridad_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.dialog_registro_resumen, container, false)
        val title = requireArguments().getString(ARG_TITLE).orEmpty()
        root.findViewById<TextView>(R.id.tvResumenTitulo).text = title

        val labels = requireArguments().getStringArrayList(ARG_LABELS).orEmpty()
        val values = requireArguments().getStringArrayList(ARG_VALUES).orEmpty()
        val containerLines = root.findViewById<LinearLayout>(R.id.llResumenLineas)
        for (i in labels.indices) {
            val row = inflater.inflate(R.layout.item_resumen_linea, containerLines, false)
            row.findViewById<TextView>(R.id.tvLabel).text = labels[i]
            val value = values.getOrNull(i).orEmpty()
            row.findViewById<TextView>(R.id.tvValue).text = value.ifBlank { "—" }
            containerLines.addView(row)
        }

        root.findViewById<MaterialButton>(R.id.btnCerrarResumen).setOnClickListener { dismiss() }
        root.findViewById<MaterialButton>(R.id.btnConfirmarGuardar).setOnClickListener {
            (activity as? Listener)?.onConfirmSave()
            dismiss()
        }
        return root
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_LABELS = "labels"
        private const val ARG_VALUES = "values"

        @JvmStatic
        fun newInstance(title: String, lines: List<Pair<String, String>>): RegistroSummaryDialog {
            return RegistroSummaryDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putStringArrayList(ARG_LABELS, ArrayList(lines.map { it.first }))
                    putStringArrayList(ARG_VALUES, ArrayList(lines.map { it.second }))
                }
            }
        }
    }
}
