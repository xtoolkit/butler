package com.shopping.utils.snackbar

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun snackBarBuilder(view: View, snackBarModel: SnackBarModel) {
    val model = Snackbar.make(view, snackBarModel.message, Snackbar.LENGTH_LONG)
    if (snackBarModel.btnTitle != null) model.setAction(snackBarModel.btnTitle) {
        snackBarModel.action?.invoke()
    }
    model.show()
}