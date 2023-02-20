package com.example.weight_tracker_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weight_tracker_kotlin.BaseClass
import com.example.weight_tracker_kotlin.R

class ForgotPasswordActivity : BaseClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
    }
}