package com.rinconadadelsur.sistemas.bioseguridad.Herramientas

import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.rinconadadelsur.sistemas.bioseguridad.R
import java.io.File

class PhotoUploadHelper(private val fragment: Fragment) {
    private val slotIds = intArrayOf(R.id.photoSlot1, R.id.photoSlot2, R.id.photoSlot3)
    private val previewIds = intArrayOf(R.id.ivPhotoPreview1, R.id.ivPhotoPreview2, R.id.ivPhotoPreview3)
    private var activeSlot = 0
    private var cameraUri: Uri? = null

    private val pickGallery = fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) applyUri(activeSlot, uri)
    }

    private val takePicture = fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) cameraUri?.let { applyUri(activeSlot, it) }
    }

    fun bind(root: View) {
        for (i in slotIds.indices) {
            val slot = root.findViewById<View>(slotIds[i]) ?: continue
            val index = i
            slot.setOnClickListener { openChooser(index) }
        }
    }

    private fun openChooser(slotIndex: Int) {
        activeSlot = slotIndex
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Adjuntar imagen")
            .setItems(arrayOf("Cámara", "Galería")) { _, which ->
                if (which == 0) launchCamera() else pickGallery.launch("image/*")
            }
            .show()
    }

    private fun launchCamera() {
        val ctx = fragment.requireContext()
        val file = File(ctx.cacheDir, "capture_${System.currentTimeMillis()}.jpg")
        cameraUri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
        takePicture.launch(cameraUri)
    }

    private fun applyUri(slotIndex: Int, uri: Uri) {
        val root = fragment.view ?: return
        val slot = root.findViewById<FrameLayout>(slotIds[slotIndex]) ?: return
        val preview = slot.findViewById<ImageView>(previewIds[slotIndex]) ?: return
        preview.setImageURI(uri)
        preview.visibility = View.VISIBLE
        for (i in 0 until slot.childCount) {
            val child = slot.getChildAt(i)
            if (child is LinearLayout) {
                child.visibility = View.GONE
            }
        }
    }
}
