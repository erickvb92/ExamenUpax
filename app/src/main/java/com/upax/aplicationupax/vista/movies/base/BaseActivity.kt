package com.upax.aplicationupax.vista.movies.base

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.upax.aplicationupax.R

open class BaseActivity: AppCompatActivity() {


    private val dialogLoading by lazy {
        val dialogAlertDialog = AlertDialog.Builder(this@BaseActivity)
        dialogAlertDialog.setView(R.layout.loading)
        dialogAlertDialog.setCancelable(false)
        dialogAlertDialog.create()
    }

    fun showLoading(isShowing: Boolean){
        if (isShowing)
            dialogLoading.show()
        else
            if (dialogLoading.isShowing)
                dialogLoading.dismiss()
    }

    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
    fun AppCompatEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}