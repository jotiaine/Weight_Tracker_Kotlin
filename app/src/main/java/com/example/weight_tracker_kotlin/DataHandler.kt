package com.example.weight_tracker_kotlin

import com.example.weight_tracker_kotlin.activities.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class DataHandler {
    private val fireStore = FirebaseFirestore.getInstance() // Firebase Firestore

    private fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser?.uid.toString()
    }

    fun registerUser(
        activity: SignUpActivity,
        userBasicInfo: UserBasicInfo,
        userMeasurements: UserMeasurements
    ) {
        // Adding user basic info to firestore
        fireStore.collection("userBasicInfo").document(getCurrentUserID())
            .set(userBasicInfo, SetOptions.merge())
            .addOnSuccessListener {
                println("User basic info added successfully")
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener {
                println("Error: ${it.message}")
                activity.userRegisteredFailed()
            }

        // Adding user default measurements to firestore
        fireStore.collection("userMeasurements").document(getCurrentUserID())
            .set(userMeasurements, SetOptions.merge())
            .addOnSuccessListener {
                println("User measurements added successfully")
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener {
                println("Error: ${it.message}")
                activity.userRegisteredFailed()
            }
    }
}
