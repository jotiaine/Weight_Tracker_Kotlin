package com.example.weight_tracker_kotlin.activities

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.weight_tracker_kotlin.R

open class BaseClass : AppCompatActivity() {
    private lateinit var loadingDialog: AlertDialog

    fun showProgressBar() {
        // Show loading from res/layout/loading.xml
        loadingDialog = AlertDialog.Builder(this)
            .setView(R.layout.loading)
            .setCancelable(false)
            .create()

        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.show()
    }

    fun hideProgressBar() {
        loadingDialog.dismiss()
    }
}