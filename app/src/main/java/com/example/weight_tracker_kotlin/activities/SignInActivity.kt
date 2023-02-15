package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.weight_tracker_kotlin.R
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var txvForgotPassword: TextView // forgot password textview in sign in activity xml

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance() // get instance of firebase auth
        txvForgotPassword = findViewById(R.id.txvForgotPassword) // forgot password textview in sign in activity xml

        // sign in button in sign in activity xml
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            try {
                val signInUsernameText = findViewById<EditText>(R.id.signUpUsernameText)
                val signInPasswordText = findViewById<EditText>(R.id.signUpPasswordText)

                auth.signInWithEmailAndPassword(
                    signInUsernameText.text.toString(),
                    signInPasswordText.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success
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
                        }
                        // sign out user after handling the task results to prevent data leaks
                        auth.signOut()
                    }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                println("Finished")
                // close sign in activity by signing out regardless of success or failure
                // this will prevent data leaks
                auth.signOut()
            }
        }

        // listening txvForgotPassword
        txvForgotPassword.setOnClickListener {
            // go to forgot password activity on click
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}