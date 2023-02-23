package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import com.example.weight_tracker_kotlin.BaseClass
import com.example.weight_tracker_kotlin.R

class IntroActivity : BaseClass() {
    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Check if currentUser exists then go to main directly
        // Automatic login
//        try {
//            var currentUserID = getCurrentUserID()
//            if (currentUserID.isNotEmpty()) {
//                intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        } catch (e: Exception) {
//            Log.d("IntroActivity", "Error: ${e.message}")
//        }

        // full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btnSignIn = findViewById<Button>(R.id.btnSignInActivity)
        btnSignUp = findViewById<Button>(R.id.btnSignUpActivity)

        btnSignIn.setOnClickListener {
            intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignUp.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}