package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.weight_tracker_kotlin.R
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var txvForgotPassword: TextView // forgot password textview in sign in activity xml
    private lateinit var signInUsernameText: EditText // username text
    private lateinit var signInPasswordText: EditText // password text
    private lateinit var imbGoBackSignIn: ImageButton // Sign in button
    private lateinit var intent: Intent

    private fun validateSignIn(): Boolean {
        return when {
            signInUsernameText.text.toString().isEmpty() -> {
                signInUsernameText.error = "Please enter username"
                false
            }
            signInPasswordText.text.toString().isEmpty() -> {
                signInPasswordText.error = "Please enter password"
                false
            }
            else -> true
        }
    }

    private fun goBackToIntroActivity() {
        // go to intro activity on click
        intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signIn() {
        try {
            signInUsernameText = findViewById(R.id.signInUsernameText)
            signInPasswordText = findViewById(R.id.signInPasswordText)
            auth = FirebaseAuth.getInstance() // get instance of firebase auth

            // Validate sign in
            if (validateSignIn()) {
                auth.signInWithEmailAndPassword(
                    signInUsernameText.text.toString().trim(),
                    signInPasswordText.text.toString().trim()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // print user info email, uid if exists
                            println("Signed in successfully")
                            println("User info: ${auth.currentUser?.email}")
                            println("User info: ${auth.currentUser?.uid}")
                            println("User info: ${auth.currentUser}")

                            // go to main activity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign in fails
                            println("Error: ${task.exception?.message}")
                            AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Sign in failed")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                        // sign out user after handling the task results to prevent data leaks
                        auth.signOut()
                    }
            } else {
                println("Error: Validation failed")
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please enter username and password")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }


        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            println("Sign in complete")
            clearTextFields()
            // close sign in activity by signing out regardless of success or failure
            // this will prevent data leaks
            auth.signOut()
        }
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
            signIn()
        }

        // listening txvForgotPassword
        txvForgotPassword.setOnClickListener {
            // go to forgot password activity on click
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        // listening imbGoBack
        imbGoBackSignIn.setOnClickListener {
           goBackToIntroActivity()
        }
    }
}