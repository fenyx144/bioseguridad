package com.rinconadadelsur.sistemas.bioseguridad.Herramientas

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rinconadadelsur.sistemas.bioseguridad.R

/** UI de captura de fotos (solo presentación por ahora). */
object PhotoUploadUi {
    @JvmStatic
    fun bind(fragment: Fragment, root: View) {
        val slotIds = intArrayOf(R.id.photoSlot1, R.id.photoSlot2, R.id.photoSlot3)
        for (id in slotIds) {
            root.findViewById<View?>(id)?.setOnClickListener {
                Toast.makeText(
                    fragment.requireContext(),
                    "Adjuntar foto (próximamente)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
