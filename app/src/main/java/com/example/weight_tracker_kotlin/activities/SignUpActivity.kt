package com.example.weight_tracker_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.weight_tracker_kotlin.DataHandler
import com.example.weight_tracker_kotlin.R

class SignUpActivity : AppCompatActivity() {
    private lateinit var dataHandler: DataHandler // Data handler
    private lateinit var btnSignUp: Button // Sign up button
    private lateinit var signUpUsernameText: EditText // username text
    private lateinit var signUpPasswordText: EditText // password text

    private fun clearTextFields() {
        signUpUsernameText.setText("")
        signUpPasswordText.setText("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        dataHandler = DataHandler() // Initialize data handler

        btnSignUp = findViewById(R.id.btnSignUp) // Get sign up button

        // back button
        val actionBar = supportActionBar
        actionBar!!.title = "Sign up"
        actionBar.setDisplayHomeAsUpEnabled(true)


        // Call sign up method from data handler when sign up button is clicked
        btnSignUp.setOnClickListener {
            signUpUsernameText = findViewById(R.id.signUpUsernameText)
            signUpPasswordText = findViewById(R.id.signUpPasswordText)

        }

        //TODO: 1. add sign up functionality
        //TODO: 2. Create main activity, statistic activity and account layout activity
        //TODO: 3. Create logout in account activity
        //TODO: 4. Create weight entry in main activity
        //TODO: 5. Create weight summary in statistic activity
    }
}