package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.weight_tracker_kotlin.BaseClass
import com.example.weight_tracker_kotlin.R

class SignUpActivity : BaseClass() {
    private lateinit var btnSignUp: Button // Sign up button
    private lateinit var signUpUsernameText: EditText // username text
    private lateinit var signUpPasswordText: EditText // password text
    private lateinit var imbGoBackSignUp: ImageButton // Sign in button
    private lateinit var intent: Intent

    private fun validateSignUp() {
        when {
            signUpUsernameText.text.toString().isEmpty() -> {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
            }
            signUpPasswordText.text.toString().isEmpty() -> {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            }
            else -> {
                signUp(
                    signUpUsernameText.text.toString().trim(),
                    signUpPasswordText.text.toString().trim()
                )
            }
        }
    }


    private fun goBackToIntroActivity() {
        // go to intro activity on click
        intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearTextFields() {
        signUpUsernameText.setText("")
        signUpPasswordText.setText("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btnSignUp = findViewById(R.id.btnSignUp) // Get sign up button
        imbGoBackSignUp = findViewById(R.id.imbGoBackSignUp)

        // Call sign up method from data handler when sign up button is clicked
        btnSignUp.setOnClickListener {
            signUpUsernameText = findViewById(R.id.signUpUsernameText)
            signUpPasswordText = findViewById(R.id.signUpPasswordText)
            validateSignUp()
            clearTextFields()
//            goBackToIntroActivity()
        }

        // listening imbGoBack
        imbGoBackSignUp.setOnClickListener {
            goBackToIntroActivity()
        }

        //TODO: 1. add sign up functionality - DONE
        //TODO: 1.5 add user default values to database when sign up - DONE
        //TODO: 2. Create main activity, statistic activity and account layout activity - DONE
        //TODO: 2.5 Create Navigation - DONE
        //TODO: 3. Create logout in account activity - DONE
        //TODO: 4. Create weight entry in main activity
        //TODO: 4.5 Motivation card on top(RapidAPI)
        //TODO: 5. Create weight summary in statistic activity
        //TODO: 6. Create Delete account in account activity - DONE BUT is not working properly!
        //https://firebase.google.com/docs/firestore/manage-data/delete-data#kotlin+ktx_2
        //TODO: 7. Create forgot password functionality // Extra
        //TODO: 8. Create change password functionality in account fragment // Extra

    }
}