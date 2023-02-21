package com.example.weight_tracker_kotlin

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.weight_tracker_kotlin.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class BaseClass : AppCompatActivity() {
    private val fireStore = FirebaseFirestore.getInstance() // Firebase Firestore
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
            // Show loading dialog
            showProgressBar()

            auth = FirebaseAuth.getInstance()
            userBasicInfo = UserBasicInfo()
            userMeasurements = UserMeasurements()

            auth.createUserWithEmailAndPassword(
                signUpUsernameText,
                signUpPasswordText
            )
                .addOnSuccessListener {
                    userBasicInfo.setUID(auth.currentUser!!.uid)
                    userMeasurements.setUID(auth.currentUser!!.uid)

                    val UID = getCurrentUserID()

                    println("Sign up successful")
                    // add user default values to firestore
                    val userBasicInfoMap = hashMapOf(
                        "uid" to userBasicInfo.getUID(),
                        "gender" to userBasicInfo.getGender(),
                        "age" to userBasicInfo.getAge(),
                        "height" to userBasicInfo.getHeight(),
                        "goal" to userBasicInfo.getGoal(),
                        "startWeight" to userBasicInfo.getStartWeight(),
                        "date" to userBasicInfo.getDate(),
                        "startImage" to userBasicInfo.getStartImage(),
                        "startBMI" to userBasicInfo.getStartBMI(),
                        "startCircumference" to userBasicInfo.getStartCircumference(),
                        "startBodyFat" to userBasicInfo.getStartBodyFat()
                    )

                    val userMeasurementsMap = hashMapOf(
                        "uid" to userMeasurements.getUID(),
                        "weight" to userMeasurements.getWeight(),
                        "bmi" to userMeasurements.getBMI(),
                        "circumference" to userMeasurements.getCircumference(),
                        "bodyFat" to userMeasurements.getBodyFat(),
                        "date" to userMeasurements.getDate()
                    )

                    // Add the userBasicInfo to firestore
                    fireStore.collection("userBasicInfo")
                        .document(UID)
                        .set(userBasicInfo)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }

                    // add the userMeasurements to firestore
                    fireStore.collection("userMeasurements")
                        .document(UID)
                        .set(userMeasurements)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT)
                                .show()
                        }

                    hideProgressBar()
                }
                .addOnFailureListener {
                    hideProgressBar()
                    Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        } finally {
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