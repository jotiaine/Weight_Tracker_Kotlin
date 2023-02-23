package com.example.weight_tracker_kotlin.activities

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
                    signUpUsernameText.text.toString(),
                    signUpPasswordText.text.toString()
                )
            }
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
        imbGoBackSignUp = findViewById(R.id.imbGoBackSignUp)

        // Call sign up method from data handler when sign up button is clicked
        btnSignUp.setOnClickListener {
            signUpUsernameText = findViewById(R.id.signUpUsernameText)
            signUpPasswordText = findViewById(R.id.signUpPasswordText)
            validateSignUp()
            clearTextFields()
        }

        // listening imbGoBack
        imbGoBackSignUp.setOnClickListener {
            navigateToIntroActivity()
        }

        //TODO: 1. add sign up functionality - DONE
        //TODO: 1.5 add user default values to database when sign up - DONE
        //TODO: 2. Create main activity, statistic activity and account layout activity - DONE
        //TODO: 2.5 Create Navigation - DONE
        //TODO: 3. Create logout in account activity - DONE
        //TODO: 4. Create weight entry in main activity - DONE
        //TODO: 4.5 Motivation card on top(RapidAPI) - DONE
        //TODO: 5. Create weight summary in statistic activity and UI - DONE
        //TODO: 6. Create Delete account in account activity - DONE
        // Version 1 done, without forgot password, change password and image adding

        // EXTRA DEVELOPMENT
        //TODO: 7. FIX image buttons to add images to firebase storage
        //TODO: 8. Create forgot password functionality
        //TODO: 9. Create change password functionality in account fragment

    }
}