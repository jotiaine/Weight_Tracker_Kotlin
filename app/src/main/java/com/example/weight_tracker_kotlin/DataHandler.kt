package com.example.weight_tracker_kotlin

import com.google.firebase.auth.FirebaseAuth

class DataHandler {
    private lateinit var auth: FirebaseAuth // Firebase authentication

    fun getCurrentUserID(): String {
        return auth.currentUser!!.uid
    }

    // create interface for signup
    interface SignupInterface {
        fun onSignupSuccess()
        fun onSignupFailure()
    }

    // Create a new user suspended function
    suspend fun signup(email: String, password: String, signupInterface: SignupInterface) {
        try {
            auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("Signup successful")
                        signupInterface.onSignupSuccess()
                    } else {
                        println("Signup failed")
                        signupInterface.onSignupFailure()
                    }
                }
        } catch (e: Exception) {
            println("Signup failed")
            signupInterface.onSignupFailure()
        }
    }

}
