package com.example.weight_tracker_kotlin

import com.example.weight_tracker_kotlin.activities.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DataHandler {
    private val fireStore = FirebaseFirestore.getInstance() // Firebase Firestore

    private fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser?.uid.toString()
    }

    fun registerUser(
        activity: SignUpActivity,
        uid: String,
        gender: String,
        age: Int,
        height: Double,
        goal: Double,
        weight: Double,
        bmi: Double,
        circumference: Double,
        bodyFat: Double,
        date: String
    ) {
        try {
            // Creating user basic info document
            val userBasicInfo = hashMapOf(
                "uid" to uid,
                "gender" to gender,
                "age" to age,
                "height" to height,
                "goal" to goal
            )
            fireStore.collection("userBasicInfo").document(uid)
                .set(userBasicInfo)
                .addOnSuccessListener {
                    println("User basic info added successfully")
                    activity.userRegisteredSuccess()
                }
                .addOnFailureListener {
                    println("Error: ${it.message}")
                    activity.userRegisteredFailed()
                }

            // Creating user measurements document
            val userMeasurements = hashMapOf(
                "uid" to uid,
                "weight" to weight,
                "bmi" to bmi,
                "circumference" to circumference,
                "bodyFat" to bodyFat,
                "date" to date
            )
            fireStore.collection("userMeasurements").document(uid)
                .set(userMeasurements)
                .addOnSuccessListener {
                    println("User measurements added successfully")
                    activity.userRegisteredSuccess()
                }
                .addOnFailureListener {
                    println("Error: ${it.message}")
                    activity.userRegisteredFailed()
                }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            activity.userRegisteredFailed()
        } finally {
            println("User registration complete")
        }
    }

}
