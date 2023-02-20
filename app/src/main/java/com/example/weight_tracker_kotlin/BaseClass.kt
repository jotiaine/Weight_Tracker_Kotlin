package com.example.weight_tracker_kotlin

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.weight_tracker_kotlin.activities.IntroActivity
import com.example.weight_tracker_kotlin.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class BaseClass : AppCompatActivity() {
    private lateinit var fireStore: FirebaseFirestore // Firebase Firestore
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

    private fun getCurrentUserID(): String {
        // if auth is not initialized, initialize it
        if (!::auth.isInitialized) {
            auth = FirebaseAuth.getInstance()
        }
        return auth.currentUser?.uid.toString()
    }


    protected fun signUp(signUpUsernameText: String, signUpPasswordText: String) {
        try {
            // if auth is not initialized, initialize it
            if (!::auth.isInitialized) {
                auth = FirebaseAuth.getInstance()
            }

            // init userBasicInfo and userMeasurements if not initialized
            if (!::userBasicInfo.isInitialized) {
                userBasicInfo = UserBasicInfo()
            }

            if (!::userMeasurements.isInitialized) {
                userMeasurements = UserMeasurements()
            }

            // init firestore if not initialized
            if (!::fireStore.isInitialized) {
                fireStore = Firebase.firestore
            }

            // Show loading dialog
            showProgressBar()

            auth.createUserWithEmailAndPassword(
                signUpUsernameText,
                signUpPasswordText
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // hide progress bar
                        hideProgressBar()

                        // print user info email, uid if exists
                        println("Signed up successfully")
                        println("User info: ${auth.currentUser?.email}")
                        println("User info: ${auth.currentUser?.uid}")
                        println("User info: ${auth.currentUser}")


                        // add user default values to firestore
                        registerUser()

                        // go to intro activity
                        val intent = Intent(this, IntroActivity::class.java)
                        startActivity(intent)
                        finish()
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
            fireStore = Firebase.firestore

            // set uid from firebase auth
            userBasicInfo.setUID(getCurrentUserID())
            userMeasurements.setUID(getCurrentUserID())

            // Creating user basic info document
            val userBasicInfoMap = hashMapOf(
                "uid" to userBasicInfo.getUID(),
                "gender" to userBasicInfo.getGender(),
                "age" to userBasicInfo.getAge(),
                "height" to userBasicInfo.getHeight(),
                "goal" to userBasicInfo.getGoal(),
                "startWeight" to userBasicInfo.getStartWeight(),
                "startBMI" to userBasicInfo.getStartBMI(),
                "startCircumference" to userBasicInfo.getStartCircumference(),
                "startBodyFat" to userBasicInfo.getStartBodyFat(),
                "startImage" to userBasicInfo.getStartImage(),
                "date" to userBasicInfo.getDate(),
            )
            println(userBasicInfoMap)

            fireStore.collection("userBasicInfo")
                .add(userBasicInfoMap)
                .addOnSuccessListener {
                    println("User basic info added successfully")
                }
                .addOnFailureListener {
                    println("Error: ${it.message}")
                }

            // Creating user measurements document
            val userMeasurementsMap = hashMapOf(
                "uid" to userMeasurements.getUID(),
                "weight" to userMeasurements.getWeight(),
                "bmi" to userMeasurements.getBMI(),
                "circumference" to userMeasurements.getCircumference(),
                "bodyFat" to userMeasurements.getBodyFat(),
                "date" to userMeasurements.getDate(),
            )

            println(userMeasurementsMap)
            fireStore.collection("userMeasurements")
                .add(userMeasurementsMap)
                .addOnSuccessListener {
                    println("User measurements added successfully")
                    userRegisteredSuccess()
                }
                .addOnFailureListener {
                    println("Error: ${it.message}")
                    userRegisteredFailed()
                }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            println("User registration complete")
            auth.signOut()
        }
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
}