package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.weight_tracker_kotlin.R
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var btnSignUp: Button // Sign up button
    private lateinit var signUpUsernameText: EditText // username text
    private lateinit var signUpPasswordText: EditText // password text

    private fun validateSignUp(): Boolean {
        return when {
            signUpUsernameText.text.toString().isEmpty() -> {
                signUpUsernameText.error = "Please enter username"
                false
            }
            signUpPasswordText.text.toString().isEmpty() -> {
                signUpPasswordText.error = "Please enter password"
                false
            }
            else -> true
        }
    }

    private fun signUp() {
        try {
            signUpUsernameText = findViewById(R.id.signUpUsernameText)
            signUpPasswordText = findViewById(R.id.signUpPasswordText)
            auth = FirebaseAuth.getInstance() // get instance of firebase auth

            // Validate sign up
            if (validateSignUp()) {
                auth.createUserWithEmailAndPassword(
                    signUpUsernameText.text.toString().trim(),
                    signUpPasswordText.text.toString().trim()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // print user info email, uid if exists
                            println("Signed up successfully")
                            println("User info: ${auth.currentUser?.email}")
                            println("User info: ${auth.currentUser?.uid}")
                            println("User info: ${auth.currentUser}")

                            // go to main activity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign up fails
                            AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Sign up failed")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please enter username and password")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            clearTextFields()
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            println("Sign up complete")
            clearTextFields()
            auth.signOut()
        }
    }

    private fun clearTextFields() {
        signUpUsernameText.setText("")
        signUpPasswordText.setText("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnSignUp = findViewById(R.id.btnSignUp) // Get sign up button

        // back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call sign up method from data handler when sign up button is clicked
        btnSignUp.setOnClickListener {
           signUp()
        }

        //TODO: 1. add sign up functionality - DONE
        //TODO: 2. Create main activity, statistic activity and account layout activity
        //TODO: 3. Create logout in account activity
        //TODO: 4. Create weight entry in main activity
        //TODO: 5. Create weight summary in statistic activity
    }
}