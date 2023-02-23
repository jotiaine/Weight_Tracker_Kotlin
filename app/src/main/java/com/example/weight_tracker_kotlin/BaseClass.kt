package com.example.weight_tracker_kotlin

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.weight_tracker_kotlin.activities.ForgotPasswordActivity
import com.example.weight_tracker_kotlin.activities.IntroActivity
import com.example.weight_tracker_kotlin.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

open class BaseClass : AppCompatActivity() {
    private val fireStore = FirebaseFirestore.getInstance() // Firebase Firestore
    private lateinit var auth: FirebaseAuth // Firebase Auth
    private lateinit var userBasicInfo: UserBasicInfo
    private lateinit var userMeasurements: UserMeasurements
    private lateinit var loadingDialog: AlertDialog
    private lateinit var intent: Intent

    inner class UserBasicInfo {
        private var uid = "default" // default
        private var gender = "default" // default
        private var age = 0 // default
        private var height = 0.0 // default
        private var targetWeight = 0.0 // default
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
            this.targetWeight = goal
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
            return targetWeight
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
        private var image = "default" // default
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

        fun setImage(image: String) {
            this.image = image
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

        fun getImage(): String {
            return image
        }

        fun getDate(): String {
            return date
        }
    }

    private fun getCurrentUserID(): String {
        return try {
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val currentUserID = currentUser!!.uid
            currentUserID
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            ""
        } finally {
            Log.d("Success", "getCurrentUserID() success")
        }
    }

    fun getCurrentTimeStamp(): String {
        return try {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            now.format(formatter)
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            ""
        } finally {
            Log.d("Success", "getCurrentTimeStamp() success")
        }
    }

    fun getCurrentWeekday(): String {
        return try {
            val now = LocalDateTime.now()
            val eet = now.atZone(ZoneId.of("Europe/Helsinki"))
            return eet.dayOfWeek.toString()
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            ""
        } finally {
            Log.d("Success", "getCurrentWeekday() success")
        }
    }

    protected fun signUp(signUpUsernameText: String, signUpPasswordText: String) {
        try {
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
                        userBasicInfo.setUID(auth.currentUser!!.uid)
                        userMeasurements.setUID(auth.currentUser!!.uid)
                        userBasicInfo.setDate(getCurrentTimeStamp())

                        val UID = getCurrentUserID()

                        println("Sign up successful")
                        // Add the userBasicInfo to firestore
                        fireStore.collection("userBasicInfo")
                            .document(UID)
                            .set(userBasicInfo)
                            .addOnSuccessListener {
                                showToast("Sign up successful")
                            }
                            .addOnFailureListener {
                                showToast("Sign up failed")
                            }

                        // add the userMeasurements to firestore
                        fireStore.collection("userMeasurements")
                            .document(UID)
                            .set(userMeasurements)
                            .addOnSuccessListener {
                                showToast("Sign up successful")
                            }
                            .addOnFailureListener {
                                showToast("Sign up failed")
                            }

                        // go to main activity
                        val intent = Intent(this, IntroActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToast("Sign up failed")
                    }
                    hideProgressBar()
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
                        // go to main activity
                        navigateToMainActivity()
                    } else {
                        // If sign in fails
                        showAlertDialog("Error", "Sign in failed")
                    }
                    hideProgressBar()
                }
        } catch (e: Exception) {
            Log.e("Error", "signIn() error: ${e.message.toString()}")
        } finally {
            // close sign in activity by signing out regardless of success or failure
            // this will prevent data leaks
            auth.signOut()
        }
    }

    /*******************
     * Navigation
     *******************/
    protected fun navigateToIntroActivity() {
        try {
            // go to intro activity on click
            intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("Error", "navigateToIntroActivity() error: ${e.message.toString()}")
        }
    }

    private fun navigateToMainActivity() {
        try {
            // go to main activity
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("Error", "navigateToMainActivity() error: ${e.message.toString()}")
        }
    }

    protected fun navigateToForgotPasswordActivity() {
        try {
            // go to forgot password activity
            intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("Error", "navigateToForgotPasswordActivity() error: ${e.message.toString()}")
        }
    }

    /*******************
     * Loading Dialog, Alerts, Toasts & errors
     *******************/
    private fun showProgressBar() {
        try {
            // Show loading from res/layout/loading.xml
            // delay
            loadingDialog = AlertDialog.Builder(this)
                .setView(R.layout.loading)
                .setCancelable(false)
                .create()

            loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            loadingDialog.show()
        } catch (e: Exception) {
            Log.e("Error", "Failed to show progress bar: ${e.message}", e)
        }
    }

    private fun hideProgressBar() {
        try {
            if (loadingDialog != null && loadingDialog!!.isShowing && !isFinishing) {
                loadingDialog!!.dismiss()
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to hide progress bar: ${e.message}", e)
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        try {
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } catch (e: Exception) {
            Log.e("Error", "showAlertDialog() error: ${e.message.toString()}")
        }
    }

    private fun showToast(message: String) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Error", "showToast() error: ${e.message.toString()}")
        }
    }
}