package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.weight_tracker_kotlin.BaseClass
import com.example.weight_tracker_kotlin.R

class SignInActivity : BaseClass() {
    private lateinit var txvForgotPassword: TextView // forgot password textview in sign in activity xml
    private lateinit var signInUsernameText: EditText // username text
    private lateinit var signInPasswordText: EditText // password text
    private lateinit var imbGoBackSignIn: ImageButton // Sign in button
    private lateinit var intent: Intent

    private fun validateSignIn() {
       when {
            signInUsernameText.text.toString().isEmpty() -> {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()

            }
            signInPasswordText.text.toString().isEmpty() -> {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            }
            else -> signIn(signInUsernameText.text.toString().trim(), signInPasswordText.text.toString().trim())
        }
    }

    private fun goBackToIntroActivity() {
        // go to intro activity on click
        intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearTextFields() {
        signInUsernameText.setText("")
        signInPasswordText.setText("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.hide() // hide action bar
        // full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        txvForgotPassword =
            findViewById(R.id.txvForgotPassword) // forgot password textview in sign in activity xml
        imbGoBackSignIn = findViewById(R.id.imbGoBackSignIn)


        // sign in button in sign in activity xml
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            signInUsernameText = findViewById(R.id.signInUsernameText)
            signInPasswordText = findViewById(R.id.signInPasswordText)
            validateSignIn()
            clearTextFields()
        }

        // listening txvForgotPassword
        txvForgotPassword.setOnClickListener {
            // go to forgot password activity on click
            intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        // listening imbGoBack
        imbGoBackSignIn.setOnClickListener {
            goBackToIntroActivity()
        }
    }
}