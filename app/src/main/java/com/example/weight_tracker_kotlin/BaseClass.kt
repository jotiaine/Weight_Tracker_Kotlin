package com.example.weight_tracker_kotlin

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.weight_tracker_kotlin.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class BaseClass : AppCompatActivity() {
    private var fireStore = FirebaseFirestore.getInstance() // Firebase Firestore
    private lateinit var auth: FirebaseAuth // Firebase Auth
    private lateinit var userBasicInfo: UserBasicInfo
    private lateinit var userMeasurements: UserMeasurements
    private lateinit var loadingDialog: AlertDialog

    inner class UserBasicInfo {
        private var uid = "default" // default
        private var gender = "default" // default
        private var age = 0 // default
        private var height = 0.0 // default
        private var goal = 0.0 // default
        private var startWeight = 0.0 // default
        private var date = "default" // default
        private var startImage = "default" // default
        private var startBMI = 0.0 // default
        private var startCircumference = 0.0 // default
        private var startBodyFat = 0.0 // default

        // Setters
        fun setUID(uid: String) {
            this.uid = uid
        }

        fun setGender(gender: String) {
            this.gender = gender
        }

        fun setAge(age: Int) {
            this.age = age
        }

        fun setHeight(height: Double) {
            this.height = height
        }

        fun setGoal(goal: Double) {
            this.goal = goal
        }

        fun setStartWeight(startWeight: Double) {
            this.startWeight = startWeight
        }

        fun setDate(date: String) {
            this.date = date
        }

        fun setStartImage(startImage: String) {
            this.startImage = startImage
        }

        fun setStartBMI(startBMI: Double) {
            this.startBMI = startBMI
        }

        fun setStartCircumference(startCircumference: Double) {
            this.startCircumference = startCircumference
        }

        fun setStartBodyFat(startBodyFat: Double) {
            this.startBodyFat = startBodyFat
        }

        // Getters
        fun getUID(): String {
            return uid
        }

        fun getGender(): String {
            return gender
        }

        fun getAge(): Int {
            return age
        }

        fun getHeight(): Double {
            return height
        }

        fun getGoal(): Double {
            return goal
        }

        fun getStartWeight(): Double {
            return startWeight
        }

        fun getDate(): String {
            return date
        }

        fun getStartImage(): String {
            return startImage
        }

        fun getStartBMI(): Double {
            return startBMI
        }

        fun getStartCircumference(): Double {
            return startCircumference
        }

        fun getStartBodyFat(): Double {
            return startBodyFat
        }
    }

    inner class UserMeasurements {
        private var uid = "default" // default
        private var weight = 0.0 // default
        private var bmi = 0.0 // default
        private var circumference = 0.0 // default
        private var bodyFat = 0.0 // default
        private var date = "default" // default


        // Setters
        fun setUID(uid: String) {
            this.uid = uid
        }

        fun setWeight(weight: Double) {
            this.weight = weight
        }

        fun setBMI(bmi: Double) {
            this.bmi = bmi
        }

        fun setCircumference(circumference: Double) {
            this.circumference = circumference
        }

        fun setBodyFat(bodyFat: Double) {
            this.bodyFat = bodyFat
        }

        fun setDate(date: String) {
            this.date = date
        }

        // Getters
        fun getUID(): String {
            return uid
        }

        fun getWeight(): Double {
            return weight
        }

        fun getBMI(): Double {
            return bmi
        }

        fun getCircumference(): Double {
            return circumference
        }

        fun getBodyFat(): Double {
            return bodyFat
        }

        fun getDate(): String {
            return date
        }
    }

    protected fun getCurrentUserID(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    protected fun signUp(signUpUsernameText: String, signUpPasswordText: String) {
        try {
            // if auth is not initialized, initialize it
            auth = FirebaseAuth.getInstance()
            userBasicInfo = UserBasicInfo()
            userMeasurements = UserMeasurements()

            // Show loading dialog
            showProgressBar()

            auth.createUserWithEmailAndPassword(
                signUpUsernameText,
                signUpPasswordText
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        println("Sign up successful")
                        // add user default values to firestore
                        registerUser()
                    } else {
                        hideProgressBar()
                        // If sign up fails
                        println("Sign up failed: ${task.exception?.message}")
                        if (!isFinishing) {
                            AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("User registration failed")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }
                }

        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            println("Sign up complete")
        }
    }

    private fun registerUser() {
        try {
            auth = FirebaseAuth.getInstance()
            fireStore = FirebaseFirestore.getInstance()

            // Create a new document with a generated ID
            val userDocRef = fireStore.collection("users").document()

            // Create a user object with some example data
            val user = hashMapOf(
                "uid" to getCurrentUserID(),
                "name" to "John Smith",
                "email" to "john.smith@example.com",
                "age" to 30,
                "height" to 170.0,
                "goal" to 70.0,
                "startWeight" to 80.0
            )

            // Add the user object to the document
            userDocRef.set(user)
                .addOnSuccessListener {
                    // The document was successfully written
                    println("DocumentSnapshot written with ID: ${userDocRef.id}")
                }
                .addOnFailureListener { e ->
                    // There was an error writing the document
                    println("Error adding document: $e")
                }

            // You can add more collections and documents here as needed

        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            println("User registration complete")
            auth.signOut()
        }
    }

    protected fun signIn(signInUsernameText: String, signInPasswordText: String) {
        try {
            auth = FirebaseAuth.getInstance() // get instance of firebase auth

            // Show loading dialog
            showProgressBar()

            auth.signInWithEmailAndPassword(
                signInUsernameText,
                signInPasswordText
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        hideProgressBar() // hide loading dialog

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
                        hideProgressBar() // hide loading dialog
                        // If sign in fails
                        println("Error: ${task.exception?.message}")
                        AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("Sign in failed")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            println("Sign in complete")
            // close sign in activity by signing out regardless of success or failure
            // this will prevent data leaks
            auth.signOut()
        }
    }

    /*******************
     * Loading Dialog, Alerts, Toasts & errors
     *******************/
    private fun showProgressBar() {
        // Show loading from res/layout/loading.xml
        loadingDialog = AlertDialog.Builder(this)
            .setView(R.layout.loading)
            .setCancelable(false)
            .create()

        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.show()
    }

    private fun hideProgressBar() {
        loadingDialog.dismiss()
    }

    private fun userRegisteredSuccess() {
        if (!isFinishing) {
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("User registration success")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun userRegisteredFailed() {
        if (!isFinishing) {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("User registered failed")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}