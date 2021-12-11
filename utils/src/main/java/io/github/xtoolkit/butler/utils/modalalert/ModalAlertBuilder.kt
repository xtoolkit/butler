package io.github.xtoolkit.butler.utils.modalalert

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun modalAlertBuilder(context: Context, model: ModalAlertModel) {
    val modal = MaterialAlertDialogBuilder(context)
        .setTitle(model.title)
        .setMessage(model.message)
        .setNegativeButton("No") { _, _ -> model.onNegative?.invoke() }
    if (model.onPositive != null)
        modal.setPositiveButton("Yes") { _, _ -> model.onPositive.invoke() }
    modal.show()
}