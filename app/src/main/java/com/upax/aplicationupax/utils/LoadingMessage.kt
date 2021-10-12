package com.upax.aplicationupax.utils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.upax.aplicationupax.R

open class LoadingMessage : AppCompatActivity() {

    private val dialogLoading by lazy {
        val dialogAlertDialog = AlertDialog.Builder(this@LoadingMessage)
        dialogAlertDialog.setView(R.layout.loading)
        dialogAlertDialog.setCancelable(false)
        dialogAlertDialog.create()
    }

    fun showLoading(isShowing: Boolean) {
        if (isShowing)
            dialogLoading.show()
        else
            if (dialogLoading.isShowing)
                dialogLoading.dismiss()
    }
}