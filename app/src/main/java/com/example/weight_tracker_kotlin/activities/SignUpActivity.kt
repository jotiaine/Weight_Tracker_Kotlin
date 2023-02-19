package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.weight_tracker_kotlin.DataHandler
import com.example.weight_tracker_kotlin.R
import com.example.weight_tracker_kotlin.UserBasicInfo
import com.example.weight_tracker_kotlin.UserMeasurements
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var dataHandler: DataHandler // data handler
    lateinit var auth: FirebaseAuth
    private lateinit var btnSignUp: Button // Sign up button
    lateinit var signUpUsernameText: EditText // username text
    lateinit var signUpPasswordText: EditText // password text

    fun validateSignUp(): Boolean {
        return when {
            signUpUsernameText.text.toString().isEmpty() -> {
                signUpUsernameText.error = "Please enter username"
                false
            }
            signUpPasswordText.text.toString().isEmpty() -> {
                signUpPasswordText.error = "Please enter password"
                false
            }
            else -> true
        }
    }

    fun signUp() {
        try {
            signUpUsernameText = findViewById(R.id.signUpUsernameText)
            signUpPasswordText = findViewById(R.id.signUpPasswordText)
            auth = FirebaseAuth.getInstance() // get instance of firebase auth
            dataHandler = DataHandler()

            // Validate sign up
            if (validateSignUp()) {
                auth.createUserWithEmailAndPassword(
                    signUpUsernameText.text.toString().trim(),
                    signUpPasswordText.text.toString().trim()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // print user info email, uid if exists
                            println("Signed up successfully")
                            println("User info: ${auth.currentUser?.email}")
                            println("User info: ${auth.currentUser?.uid}")
                            println("User info: ${auth.currentUser}")

                            // create user in firestore default values
                            val userBasicInfo = UserBasicInfo()
                            val userMeasurements = UserMeasurements()

                            // set uid from firebase auth
                            userBasicInfo.setUID(auth.currentUser?.uid.toString())
                            userMeasurements.setUID(auth.currentUser?.uid.toString())

                            // userbasicinfo
                            val uidBasicInfo = userBasicInfo.getUID()
                            val gender = userBasicInfo.getGender()
                            val age = userBasicInfo.getAge()
                            val height = userBasicInfo.getHeight()
                            val goal = userBasicInfo.getGoal()

                            // usermeasurements
//                            var uidMeasurements = userMeasurements.getUID()
                            val weight = userMeasurements.getWeight()
                            val bmi = userMeasurements.getBMI()
                            val circumference = userMeasurements.getCircumference()
                            val bodyFat = userMeasurements.getBodyFat()
                            val date = userMeasurements.getDate()


                            // add user default values to firestore
                            dataHandler.registerUser(
                                this, uidBasicInfo,
                                gender, age, height, goal, weight, bmi, circumference, bodyFat, date
                            )

                            // go to intro activity
                            val intent = Intent(this, IntroActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
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
            } else {
                println("Sign up failed")
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
            clearTextFields()
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            println("Sign up complete")
            clearTextFields()
            auth.signOut()
        }
    }

    fun userRegisteredSuccess() {
        if (!isFinishing) {
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("User registration success")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        auth.signOut()
    }

    fun userRegisteredFailed() {
        if (!isFinishing) {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("User registered failed")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        auth.signOut()
    }

    private fun clearTextFields() {
        signUpUsernameText.setText("")
        signUpPasswordText.setText("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnSignUp = findViewById(R.id.btnSignUp) // Get sign up button

        // back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call sign up method from data handler when sign up button is clicked
        btnSignUp.setOnClickListener {
            signUp()
        }

        //TODO: 1. add sign up functionality - DONE
        //TODO: 1.5 add user default values to database when sign up - DONE
        //TODO: 2. Create main activity, statistic activity and account layout activity - DONE
        //TODO: 2.5 Create Navigation
        //TODO: 3. Create logout in account activity
        //TODO: 4. Create weight entry in main activity
        //TODO: 5. Create weight summary in statistic activity
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // go to intro activity on back button click
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}