package com.example.weight_tracker_kotlin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User

class DataHandler {
    private lateinit var user: User // User class
    private lateinit var auth: FirebaseAuth // Firebase authentication

    fun getCurrentUserID(): String {
        return auth.currentUser!!.uid
    }
}
